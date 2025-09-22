package net.skytrail.ilcdb.domain;

import gorm.transform.Entity
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Entity
class Client implements Comparable<Client> {
    static final long YEAR_IN_MILLIS = 365L*24L*60L*60L*1000L;
    private static final DateTimeFormatter briefDateFormat = DateTimeFormat.forPattern("MMM-dd-yyyy");

    Person client
    Date firstVisit
    AMI ami = new AMI("label":"UNSPECIFIED", "level":0)
    Integer householdIncomeLevel
    Integer numberInHousehold
    SortedSet<Note> notes
    SortedSet serviceRecords
    SortedSet conflicts
    String fileLocation

    static hasMany = [notes:Note, cases:ClientCase, appointments:Appointment, sponsorRelations:ClientSponsorRelation,
                      serviceRecords:ServiceRecord, conflicts:Conflict]

    static mapping = {
        cache true
        cases sort:"startDate"
    }

    static fetchMode = [client:"eager", notes:"eager", cases:"eager",serviceRecords:"eager", conflicts:"eager"]

    static constraints = {
        firstVisit(nullable:true)
        householdIncomeLevel(nullable:true)
        numberInHousehold(nullable:true)
        fileLocation(nullable:true)
    }

    static transients = [ "statiAchievedStrings", "firstVisitString", "homeCountry", "shortAddress", "emailAddress", "openCase", "validCases", "attorney", "person", "intakes" ]

    def toMap() {
        [id : person.id,
         firstName           : person.firstName,
         lastName            : person.lastName,
         email               : person.emailAddress,
         dateOfBirth         : person.dateOfBirth,
         gender              : person.gender,
         englishProficiency  : person.englishProficiency,
         phone               : person.phoneNumber,
         race                : person.race,
         birthPlace          : person.placeOfBirth.toMap(),
         address             : person.address.toMap(),
         fileLocation        : fileLocation,
         ami                 : ami.toMap(),
         numberInHousehold   : numberInHousehold,
         householdIncomeLevel: householdIncomeLevel,
         cases               : cases.collect { it.toMap() },
         serviceRecords      : serviceRecords.collect { it.toMap() },
         conflicts           : conflicts.collect { it.toMap() },
         //sponsorRelations  : sponsorRelations.collect { it.toMap() },
         notes               : notes.collect { it.toMap() }]
    }

    String toString() {
        String returnValue
        if(client != null)
            returnValue = client.toString()
        else
            returnValue = "Client name undefined"
        return returnValue
    }

    String toDebugString() {
        return "client: $client, firstVisit: $firstVisit, householdIncomeLevel: $householdIncomeLevel, numberInHousehold: $numberInHousehold, address: $client.address";
    }

    String getFirstVisitString() {
        if (firstVisit instanceof Date)
            return (briefDateFormat.print(firstVisit.getTime()));
        return "";
    }

    String getHomeCountry() {
        return client?.placeOfBirth?.country
    }

    String getShortAddress() {
        return client.getAddress().toShortString()
    }

    String getEmailAddress() {
        return client.getEmailAddress()
    }

    Collection<String> getFileLocationAsList() {
        def locations = new TreeSet()
        if (fileLocation == null) {
            for (intake in cases) {
                if (intake.fileLocation != null && intake.fileLocation != "")
                    locations.add(intake.fileLocation)
            }
        }
        else
            locations.add(fileLocation)
        return locations
    }

    String getFileLocation() {
        if (fileLocation == null) {
            def locations = getFileLocationAsList()
            fileLocation = ""
            for (location in locations)
                fileLocation = fileLocation + location+"; "
            if (fileLocation == null)
                fileLocation = ""
            else
                fileLocation = fileLocation.replaceAll("; \$", "")
        }
        return fileLocation
    }

    public boolean equals(Object other) {
        return id == ((Client)other).id
    }

    int hashCode() {
        return id;
    }

    boolean isOpenCase() {
        cases.any{it.isOpen()}
    }

    Person getPerson() {
        return client;
    }

    String getAttorney() {
        String attorneys = ""
        List<ClientCase> openList = new ArrayList<ClientCase>();
        List<ClientCase> closedList = new ArrayList<ClientCase>()
        for (ClientCase clientCase : cases) {
            if (clientCase.isOpen())
                openList.add(clientCase)
            else
                closedList.add(clientCase)
        }

        if (openList.size() > 0) {
            for (ClientCase clientCase : openList) {
                if (clientCase.attorney == null || "-Choose-".equals(clientCase.attorney))
                    attorneys += "? (open)"+", "
                else
                    attorneys += clientCase.attorney+" (open), "
            }
        }
        else
        {
            if (closedList.size() > 0) {
                ClientCase newestCase
                for (ClientCase clientCase : closedList) {
                    if (newestCase == null || clientCase.completionDate > newestCase.completionDate)
                        newestCase = clientCase
                }
                attorneys = (newestCase.attorney == null) ? "?" : newestCase.attorney
            }
            else
                attorneys = "?"
        }
        return attorneys.replaceFirst(", \$", "")
    }

    boolean isValidCases() {
        for (ClientCase clientCase : cases) {
            if (!clientCase.isValid())
                return false;
        }
        return true;
    }

    // Called from client/edit.gsp
    List<String> getStatiAchieved() {
        List<String> achievedList = new ArrayList();

        cases.each{ clientCase ->
            if (clientCase.isStatusAchieved() && clientCase.caseType.associatedStatus)
                achievedList.add(clientCase.caseType.associatedStatus + " : " + briefDateFormat.print(clientCase.completionDate?.getTime()))
        }

        return achievedList
    }

    int compareTo(Client other) {
        return client.compareTo(other.client)
    }

    // Returns a 2 integer array with the first value the number of Staff Advises in the interval and
    // the second value the number of Staff Representations in the interval
    def intakeTypeCounts(interval) {
        return cases.inject([0, 0]) { acc, aCase  ->
//            println("client id: " + aCase.client.id + ", intake type: " + aCase.intakeType + "startDate: " + aCase.startDate + ", completionDate: " + aCase.completionDate)
            if (interval.contains(aCase.startDate.getTime()) || aCase.completionDate == null || interval.contains(aCase.completionDate.getTime())) {
                if (aCase.isStaffAdvise())
                    [acc[0] + 1, acc[1]]
                else
                    [acc[0], acc[1] + 1]
            }
            else
                acc
        }
    }

    boolean hasStaffAdvise(String intakeState, Interval interval) {
        cases.any{ it.isStaffAdvise() && (interval.contains(it.startDate.getTime()) || interval.contains(it.completionDate.getTime())) }
    }

    boolean hasStaffRepresentation(String intakeState, Interval interval) {
        if ("opened" == intakeState)
            cases.any { it.isStaffRepresentation() && interval.contains(it.startDate.getTime()) }
        else if ("closed" == intakeState)
            cases.any { it.isStaffRepresentation() && it.completionDate != null && interval.contains(it.completionDate.getTime()) }
        else
            cases.any { it.isStaffRepresentation() }
    }

    public static boolean hasOngoingStaffRepresentation(Client client, StatusAchieved.Type statusType, Interval interval) {
        client.cases.findAll { clientCase ->
            (statusType == StatusAchieved.Type.Any) ||
                (clientCase.caseType?.associatedStatus == String.valueOf(statusType) || clientCase.caseType?.type == String.valueOf(statusType))
        }.any { clientCase ->
            (clientCase.completionDate == null || interval.isBefore(clientCase.completionDate.getTime()))
        }
    }

    static boolean hasAttemptedStatus(Client client, StatusAchieved.Type statusType, Interval interval) {
        client.cases.any{ clientCase ->
            (clientCase.caseType?.associatedStatus == String.valueOf(statusType) || clientCase.caseType?.type == String.valueOf(statusType)) &&
                (interval.contains(clientCase.startDate.getTime()) || (clientCase.completionDate != null && interval.contains(clientCase.completionDate.getTime())) ||
                 (interval.isAfter(clientCase.startDate.getTime()) && (clientCase.completionDate != null && interval.isBefore(clientCase.completionDate.getTime()))))
        }
    }

    static boolean hasAchievedStatus(Client client, StatusAchieved.Type statusType, Interval interval) {
        return client.cases.any{ clientCase ->
            (clientCase.isStatusAchieved() || clientCase.isSuccessful()) &&
                (clientCase.caseType?.associatedStatus == String.valueOf(statusType) || clientCase.caseType?.type == String.valueOf(statusType)) &&
                (clientCase.completionDate != null && interval.contains(clientCase.completionDate.getTime()));
        }
    }

    public static boolean hasAchievedCitizenship(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.Citizenship, interval) }
    public static boolean hasAttemptedCitizenship(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.Citizenship, interval) }

    public static boolean hasAchievedDACA(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.DACA, interval) }
    public static boolean hasAttemptedDACA(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.DACA, interval) }

    public static boolean hasAchievedLPR(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.LPR, interval) }
    public static boolean hasAttemptedLPR(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.LPR, interval) }

    public static boolean hasAchievedLPRConditionsRemoved(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.LPRConditionsRemoved, interval) }
    public static boolean hasAttemptedLPRConditionsRemoved(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.LPRConditionsRemoved, interval) }

    public static boolean hasAchievedLPRCardRenewed(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.LPRCardRenewed, interval) }
    public static boolean hasAttemptedLPRCardRenewed(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.LPRCardRenewed, interval) }

    public static boolean hasAchievedTPS(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.TPS, interval) }
    public static boolean hasAttemptedTPS(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.TPS, interval) }

    public static boolean hasAchievedI90(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I90, interval) }
    public static boolean hasAttemptedI90(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I90, interval) }

    public static boolean hasAchievedEOIR(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.EOIR, interval) }
    public static boolean hasAttemptedEOIR(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.EOIR, interval) }

    public static boolean hasAchievedFOIA(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.FOIA, interval) }
    public static boolean hasAttemptedFOIA(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.FOIA, interval) }

    public static boolean hasAchievedI102(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I102, interval) }
    public static boolean hasAttemptedI102(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I102, interval) }

    public static boolean hasAchievedI129F(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I129F, interval) }
    public static boolean hasAttemptedI129F(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I129F, interval) }

    public static boolean hasAchievedI130(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I130IR, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I130nonIR, interval)}
    public static boolean hasAttemptedI130(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I130IR, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I130nonIR, interval)}

    public static boolean hasAchievedI131(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I131, interval) }
    public static boolean hasAttemptedI131(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I131, interval) }

    public static boolean hasAchievedI192(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I192, interval) }
    public static boolean hasAttemptedI192(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I192, interval) }

    public static boolean hasAchievedI360(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I360, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I360VAWA, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I360VAWAderivative, interval)}
    public static boolean hasAttemptedI360(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I360, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I360VAWA, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I360VAWAderivative, interval) }

    public static boolean hasAchievedI539(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I539, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I539VVisa, interval) }
    public static boolean hasAttemptedI539(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I539, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I539VVisa, interval) }

    public static boolean hasAchievedI601(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I601, interval) }
    public static boolean hasAttemptedI601(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I601, interval) }

    public static boolean hasAchievedI751(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I751, interval) }
    public static boolean hasAttemptedI751(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I751, interval) }

    public static boolean hasAchievedI765(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I765, interval) }
    public static boolean hasAttemptedI765(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I765, interval) }

    public static boolean hasAchievedI821(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I821, interval) }
    public static boolean hasAttemptedI821(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I821, interval) }

    public static boolean hasAchievedI824(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I824, interval) }
    public static boolean hasAttemptedI824(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I824, interval) }

    public static boolean hasAchievedI881(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I881, interval) }
    public static boolean hasAttemptedI881(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I881, interval) }

    public static boolean hasAchievedI912(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I912, interval) }
    public static boolean hasAttemptedI912(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I912, interval) }

    public static boolean hasAchievedI914(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I914, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I914SuppA, interval) }
    public static boolean hasAttemptedI914(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I914, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I914SuppA, interval) }

    public static boolean hasAchievedI918(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I918, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I918SuppA, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.I918SuppB, interval) }
    public static boolean hasAttemptedI918(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I918, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I918SuppA, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.I918SuppB, interval) }

    public static boolean hasAchievedI929(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.I929, interval) }
    public static boolean hasAttemptedI929(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.I929, interval) }

    public static boolean hasAchievedN336(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.N336, interval) }
    public static boolean hasAttemptedN336(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.N336, interval) }

    public static boolean hasAchievedN400(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.N400, interval) }
    public static boolean hasAttemptedN400(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.N400, interval) }

    public static boolean hasAchievedN565(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.N565, interval) }
    public static boolean hasAttemptedN565(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.N565, interval) }

    public static boolean hasAchievedN600(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.N600, interval) }
    public static boolean hasAttemptedN600(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.N600, interval) }

    public static boolean hasAchievedAOS(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.AOS, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.AOS_OneStep, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.AOS_T, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.AOS_U, interval) ||
                                                                             hasAchievedStatus(client, StatusAchieved.Type.AOS_VAWA, interval)}
    
    public static boolean hasAttemptedAOS(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.AOS, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.AOS_OneStep, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.AOS_T, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.AOS_U, interval) ||
                                                                              hasAttemptedStatus(client, StatusAchieved.Type.AOS_VAWA, interval)}

    public static boolean hasAchievedConsularProcessing(Client client, Interval interval) { hasAchievedStatus(client, StatusAchieved.Type.CP, interval) ||
                                                                                            hasAchievedStatus(client, StatusAchieved.Type.CP_T, interval) ||
                                                                                            hasAchievedStatus(client, StatusAchieved.Type.CP_U, interval) ||
                                                                                            hasAchievedStatus(client, StatusAchieved.Type.CP_Admin_Processing, interval) ||
                                                                                            hasAchievedStatus(client, StatusAchieved.Type.CP_I601_Waiver, interval) ||
                                                                                            hasAchievedStatus(client, StatusAchieved.Type.CP_I601A_Provisional_Waiver, interval)}
    
    public static boolean hasAttemptedConsularProcessing(Client client, Interval interval) { hasAttemptedStatus(client, StatusAchieved.Type.CP, interval) ||
                                                                                             hasAttemptedStatus(client, StatusAchieved.Type.CP_T, interval) ||
                                                                                             hasAttemptedStatus(client, StatusAchieved.Type.CP_U, interval) ||
                                                                                             hasAttemptedStatus(client, StatusAchieved.Type.CP_Admin_Processing, interval) ||
                                                                                             hasAttemptedStatus(client, StatusAchieved.Type.CP_I601_Waiver, interval) ||
                                                                                             hasAttemptedStatus(client, StatusAchieved.Type.CP_I601A_Provisional_Waiver, interval)}

    public static boolean hasAttemptedNoStatus(Client client, Interval interval) { return !hasAttemptedAnyStatus(client, interval) }

    public static boolean hasAchievedNoStatus(Client client, Interval interval) { return !hasAchievedAnyStatus(client, interval) }

    public static boolean hasAttemptedAnyStatus(Client client, Interval interval) {
        return client.hasStaffRepresentation("any", interval)
    }

    public static boolean hasAchievedAnyStatus(Client client, Interval interval) {
        return hasAchievedCitizenship(client, interval) ||
            hasAchievedDACA(client, interval) ||
            hasAchievedLPR(client, interval) ||
            hasAchievedLPRConditionsRemoved(client, interval) ||
            hasAchievedLPRCardRenewed(client, interval) ||
            hasAchievedTPS(client, interval)
    }

    def getIntakes() {
        StringBuffer sb = new StringBuffer()
        cases.collect { aCase ->
            if (aCase.caseType != null) sb.append(aCase.caseType?.type)
            else sb.append("SA")
            sb.append(", ")
        }

        return sb.toString().replaceAll("^, ", "").replaceAll(", \$", "")
    }
}
