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

    static transients = [ "statiAchievedStrings", "firstVisitString", "homeCountry", "shortAddress", "emailAddress", "openCase", "validCases", "attorney", "person" ]

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

    public int hashCode()
    {
        return id;
    }

    public boolean isOpenCase()
    {
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isOpen())
                return true;
        }
        return false;
    }

    public Person getPerson()
    {
        return client;
    }

    public String getAttorney()
    {
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

    public boolean isValidCases()
    {
        for (ClientCase clientCase : cases)
        {
            if (!clientCase.isValid())
                return false;
        }
        return true;
    }

    // Called from client/edit.gsp
    public List<String> getStatiAchieved()
    {
        List<String> achievedList = new ArrayList();

        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved())
                println("id: " + client.toString() + ", status achieved: " + clientCase.caseType.associatedStatus)

            if (clientCase.isStatusAchieved() && clientCase.caseType.associatedStatus)
                achievedList.add(clientCase.caseType.associatedStatus + " : " + briefDateFormat.print(clientCase.completionDate?.getTime()))
        }

        return achievedList
    }

    public int compareTo(Client other)
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
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved() &&
                (clientCase.caseType.associatedStatus == String.valueOf(statusType) || clientCase.caseType.type == String.valueOf(statusType)) &&
                    interval.contains(clientCase.startDate.getTime())) return true

        }
        return false
    }

    boolean hasAchievedStatus(StatusAchieved.Type statusType, Interval interval)
    {
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved() && (clientCase.caseType.associatedStatus == String.valueOf(statusType) || clientCase.caseType.type == String.valueOf(statusType)))
                return true
        }
        return false
    }

    public boolean hasAchievedCitizenship(Interval interval) {
        hasAchievedStatus(StatusAchieved.Type.Citizenship)
    }

    public boolean hasAttemptedCitizenship(Interval interval) {
        hasAttemptedStatus(StatusAchieved.Type.Citizenship, interval)
    }

    public boolean hasAchievedDACA(Interval interval) {
        hasAchievedStatus(StatusAchieved.Type.DACA, interval)
    }

    public boolean hasAttemptedDACA(Interval interval) {
        hasAttemptedStatus(StatusAchieved.Type.DACA, interval)
    }

    public boolean hasAchievedLPR(Interval interval) {
        hasAchievedStatus(StatusAchieved.Type.LPR, interval)
    }

    public boolean hasAttemptedLPR(Interval interval) {
        hasAttemptedStatus(StatusAchieved.Type.LPR, interval)
    }

    public boolean hasAchievedLPRConditionsRemoved(Interval interval)
    {
        hasAchievedStatus(StatusAchieved.Type.LPRConditionsRemoved, interval)
    }

    public boolean hasAttemptedLPRConditionsRemoved(Interval interval)
    {
        hasAttemptedStatus(StatusAchieved.Type.LPRConditionsRemoved, interval)
    }

    public boolean hasAchievedLPRCardRenewed(Interval interval)
    {
        hasAchievedStatus(StatusAchieved.Type.LPRCardRenewed, interval)
    }

    public boolean hasAttemptedLPRCardRenewed(Interval interval)
    {
        hasAttemptedStatus(StatusAchieved.Type.LPRCardRenewed, interval)
    }

    public boolean hasAchievedTPS(Interval interval) {
        hasAchievedStatus(StatusAchieved.Type.TPS, interval)
    }

    public boolean hasAttemptedTPS(Interval interval) {
        hasAttemptedStatus(StatusAchieved.Type.TPS, interval)
    }

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
}
