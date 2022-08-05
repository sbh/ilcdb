import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.Interval

class Client implements Comparable<Client>
{
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

    static mapping =
    {
        cache true
        cases sort:"startDate"
    }

    static fetchMode = [client:"eager", notes:"eager", cases:"eager",serviceRecords:"eager", conflicts:"eager"]

    static constraints =
    {
        firstVisit(nullable:true)
        householdIncomeLevel(nullable:true)
        numberInHousehold(nullable:true)
        fileLocation(nullable:true)
    }

    static transients = [ "statiAchievedStrings", "firstVisitString", "homeCountry", "shortAddress", "emailAddress", "openCase", "validCases", "attorney", "person", "intakes" ]

    def toMap() {
        [firstName           : person.firstName,
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

    String toString()
    {
        String returnValue
        if(client != null)
            returnValue = client.toString()
        else
            returnValue = "Client name undefined"
        return returnValue
    }

    String toDebugString()
    {
        return "client: $client, firstVisit: $firstVisit, householdIncomeLevel: $householdIncomeLevel, numberInHousehold: $numberInHousehold, address: $client.address";
    }

    String getFirstVisitString()
    {
        if (firstVisit instanceof Date)
            return (briefDateFormat.print(firstVisit.getTime()));
        return "";
    }

    String getHomeCountry()
    {
        return client?.placeOfBirth?.country
    }

    String getShortAddress()
    {
        return client.getAddress().toShortString()
    }

    String getEmailAddress()
    {
        return client.getEmailAddress()
    }

    Collection<String> getFileLocationAsList()
    {
        def locations = new TreeSet()
        if (fileLocation == null)
        {
            for (intake in cases)
            {
                if (intake.fileLocation != null && intake.fileLocation != "")
                    locations.add(intake.fileLocation)
            }
        }
        else
            locations.add(fileLocation)
        return locations
    }

    String getFileLocation()
    {
        if (fileLocation == null)
        {
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

    public boolean equals(Object other)
    {
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
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isOpen())
                openList.add(clientCase)
            else
                closedList.add(clientCase)
        }

        if (openList.size() > 0)
        {
            for (ClientCase clientCase : openList)
            {
                if (clientCase.attorney == null || "-Choose-".equals(clientCase.attorney))
                    attorneys += "? (open)"+", "
                else
                    attorneys += clientCase.attorney+" (open), "
            }
        }
        else
        {
            if (closedList.size() > 0)
            {
                ClientCase newestCase
                for (ClientCase clientCase : closedList)
                {
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

    boolean isValidCases()
    {
        for (ClientCase clientCase : cases)
        {
            if (!clientCase.isValid())
                return false;
        }
        return true;
    }

    // Called from client/edit.gsp
    List<String> getStatiAchieved()
    {
        List<String> achievedList = new ArrayList();

        cases.each{ clientCase ->
            if (clientCase.isStatusAchieved() && clientCase.caseType.associatedStatus)
                achievedList.add(clientCase.caseType.associatedStatus + " : " + briefDateFormat.print(clientCase.completionDate?.getTime()))
        }

        return achievedList
    }

    int compareTo(Client other)
    {
        return client.compareTo(other.client)
    }

    boolean hasStaffAdvise(String intakeState, Interval interval) {
        cases.any{ it.isStaffAdvise() && (interval.contains(it.startDate.getTime()) || interval.contains(it.endDate.getTime())) }
    }

    boolean hasStaffRepresentation(String intakeState, Interval interval) {
        if ("opened" == intakeState) {
            cases.any { it.isStaffRepresentation() && interval.contains(it.startDate.getTime()) }
        }
        else if ("closed" == intakeState) {
            cases.any { it.isStaffRepresentation() && interval.contains(it.completionDate.getTime()) }
        }
        else {
            cases.any { it.isStaffRepresentation() && (interval.contains(it.startDate.getTime()) || interval.contains(it.completionDate.getTime())) }
        }
    }

    boolean hasAttemptedStatus(StatusAchieved.Type statusType, Interval interval) {
        cases.any{ clientCase ->
            (clientCase.caseType?.associatedStatus == String.valueOf(statusType) || clientCase.caseType?.type == String.valueOf(statusType))
        }
    }

    boolean hasAchievedStatus(StatusAchieved.Type statusType, Interval interval) {
        cases.any{ clientCase ->
            clientCase.isStatusAchieved() &&
                    (clientCase.caseType?.associatedStatus == String.valueOf(statusType) || clientCase.caseType?.type == String.valueOf(statusType)) &&
                    interval.contains(clientCase.completionDate.getTime())
        }
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved() && (clientCase.caseType.associatedStatus == String.valueOf(statusType) || clientCase.caseType.type == String.valueOf(statusType)))
                return true
        }
        return false
    }

    public boolean hasAchievedCitizenship(Interval interval) { hasAchievedStatus(StatusAchieved.Type.Citizenship, interval) }
    public boolean hasAttemptedCitizenship(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.Citizenship, interval) }

    public boolean hasAchievedDACA(Interval interval) { hasAchievedStatus(StatusAchieved.Type.DACA, interval) }
    public boolean hasAttemptedDACA(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.DACA, interval) }

    public boolean hasAchievedLPR(Interval interval) { hasAchievedStatus(StatusAchieved.Type.LPR, interval) }
    public boolean hasAttemptedLPR(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.LPR, interval) }

    public boolean hasAchievedLPRConditionsRemoved(Interval interval) { hasAchievedStatus(StatusAchieved.Type.LPRConditionsRemoved, interval) }
    public boolean hasAttemptedLPRConditionsRemoved(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.LPRConditionsRemoved, interval) }

    public boolean hasAchievedLPRCardRenewed(Interval interval) { hasAchievedStatus(StatusAchieved.Type.LPRCardRenewed, interval) }
    public boolean hasAttemptedLPRCardRenewed(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.LPRCardRenewed, interval) }

    public boolean hasAchievedTPS(Interval interval) { hasAchievedStatus(StatusAchieved.Type.TPS, interval) }
    public boolean hasAttemptedTPS(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.TPS, interval) }

    public boolean hasAchievedI90(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I90, interval) }
    public boolean hasAttemptedI90(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I90, interval) }

    public boolean hasAchievedEOIR(Interval interval) { hasAchievedStatus(StatusAchieved.Type.EOIR, interval) }
    public boolean hasAttemptedEOIR(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.EOIR, interval) }

    public boolean hasAchievedFOIA(Interval interval) { hasAchievedStatus(StatusAchieved.Type.FOIA, interval) }
    public boolean hasAttemptedFOIA(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.FOIA, interval) }

    public boolean hasAchievedI102(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I102, interval) }
    public boolean hasAttemptedI102(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I102, interval) }

    public boolean hasAchievedI129F(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I129F, interval) }
    public boolean hasAttemptedI129F(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I129F, interval) }

    public boolean hasAchievedI130(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I130IR, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I130nonIR, interval)}
    public boolean hasAttemptedI130(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I130IR, interval) ||
        hasAttemptedStatus(StatusAchieved.Type.I130nonIR, interval)}

    public boolean hasAchievedI131(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I131, interval) }
    public boolean hasAttemptedI131(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I131, interval) }

    public boolean hasAchievedI192(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I192, interval) }
    public boolean hasAttemptedI192(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I192, interval) }

    public boolean hasAchievedI360(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I360, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I360VAWAderivative, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I360VAWAderivative, interval)}
    public boolean hasAttemptedI360(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I360, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I360VAWA, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I360VAWAderivative, interval) }

    public boolean hasAchievedI539(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I539, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I539VVisa, interval) }
    public boolean hasAttemptedI539(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I539, interval) ||
            hasAttemptedStatus(StatusAchieved.Type.I539VVisa, interval) }

    public boolean hasAchievedI601(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I601, interval) }
    public boolean hasAttemptedI601(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I601, interval) }

    public boolean hasAchievedI751(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I751, interval) }
    public boolean hasAttemptedI751(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I751, interval) }

    public boolean hasAchievedI765(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I765, interval) }
    public boolean hasAttemptedI765(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I765, interval) }

    public boolean hasAchievedI821(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I821, interval) }
    public boolean hasAttemptedI821(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I821, interval) }

    public boolean hasAchievedI824(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I824, interval) }
    public boolean hasAttemptedI824(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I824, interval) }

    public boolean hasAchievedI881(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I881, interval) }
    public boolean hasAttemptedI881(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I881, interval) }

    public boolean hasAchievedI912(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I912, interval) }
    public boolean hasAttemptedI912(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I912, interval) }

    public boolean hasAchievedI914(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I914, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I914SuppA, interval) }
    public boolean hasAttemptedI914(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I914, interval) ||
            hasAttemptedStatus(StatusAchieved.Type.I914SuppA, interval) }

    public boolean hasAchievedI918(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I918, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I918SuppA, interval) ||
            hasAchievedStatus(StatusAchieved.Type.I918SuppB, interval) }
    public boolean hasAttemptedI918(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I918, interval) ||
            hasAttemptedStatus(StatusAchieved.Type.I918SuppA, interval) ||
            hasAttemptedStatus(StatusAchieved.Type.I918SuppB, interval) }

    public boolean hasAchievedI929(Interval interval) { hasAchievedStatus(StatusAchieved.Type.I929, interval) }
    public boolean hasAttemptedI929(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.I929, interval) }

    public boolean hasAchievedN336(Interval interval) { hasAchievedStatus(StatusAchieved.Type.N336, interval) }
    public boolean hasAttemptedN336(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.N336, interval) }

    public boolean hasAchievedN400(Interval interval) { hasAchievedStatus(StatusAchieved.Type.N400, interval) }
    public boolean hasAttemptedN400(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.N400, interval) }

    public boolean hasAchievedN565(Interval interval) { hasAchievedStatus(StatusAchieved.Type.N565, interval) }
    public boolean hasAttemptedN565(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.N565, interval) }

    public boolean hasAchievedN600(Interval interval) { hasAchievedStatus(StatusAchieved.Type.N600, interval) }
    public boolean hasAttemptedN600(Interval interval) { hasAttemptedStatus(StatusAchieved.Type.N600, interval) }

    public boolean hasAttemptedNoStatus(Interval interval)
    {
        return !hasAttemptedAnyStatus(interval)
    }

    public boolean hasAchievedNoStatus(Interval interval)
    {
        return !hasAchievedAnyStatus(interval)
    }

    public boolean hasAchievedAnyStatus(Interval interval)
    {
        return hasAchievedCitizenship(interval) || hasAchievedDACA(interval) || hasAchievedLPR(interval) || hasAchievedLPRConditionsRemoved(interval) || hasAchievedLPRCardRenewed(interval) || hasAchievedTPS(interval)
    }

    public boolean hasAttemptedAnyStatus(Interval interval)
    {
        return hasAttemptedCitizenship(interval) || hasAttemptedDACA(interval) || hasAttemptedLPR(interval) || hasAttemptedLPRConditionsRemoved(interval) || hasAttemptedLPRCardRenewed(interval) || hasAttemptedTPS(interval)
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
