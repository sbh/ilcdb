import java.util.Date;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

class Client implements Comparable<Client>
{
    static final long YEAR_IN_MILLIS = 365L*24L*60L*60L*1000L;
    private static final DateTimeFormatter briefDateFormat = DateTimeFormat.forPattern("MMM-dd-yyyy");

    Person client
    Date firstVisit
    AMI ami = new AMI("label":"UNSPECIFIED", "level":0)
    Integer householdIncomeLevel
    Integer numberInHousehold
    SortedSet notes
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

    public List<String> getStatiAchieved()
    {
        List<String> achievedList = new ArrayList();
        
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved() && clientCase.caseType.associatedStatus)
                achievedList.add(clientCase.caseType.associatedStatus + " : " + briefDateFormat.print(clientCase.completionDate?.getTime()))
        }
        
        return achievedList
    }

    public int compareTo(Client other)
    {
        return client.compareTo(other.client)
    }
    
    boolean hasAchievedStatus(StatusAchieved.Type statusType)
    {
        for (ClientCase clientCase : cases)
        {
            if (clientCase.isStatusAchieved() && clientCase.caseType.associatedStatus == String.valueOf(statusType))
                return true;
        }
        return false;
    }

    public boolean hasAchievedCitizenship()
    {
        hasAchievedStatus(StatusAchieved.Type.Citizenship)
    }
    
    public boolean hasAchievedDACA()
    {
        hasAchievedStatus(StatusAchieved.Type.DACA)
    }

    public boolean hasAchievedLPR()
    {
        hasAchievedStatus(StatusAchieved.Type.LPR)
    }

    public boolean hasAchievedLPRConditionsRemoved()
    {
        hasAchievedStatus(StatusAchieved.Type.LPRConditionsRemoved)
    }

    public boolean hasAchievedLPRCardRenewed()
    {
        hasAchievedStatus(StatusAchieved.Type.LPRCardRenewed)
    }

    public boolean hasAchievedTPS()
    {
        hasAchievedStatus(StatusAchieved.Type.TPS)
    }
    
    public boolean hasAchievedNoStatus()
    {
        return !hasAchievedAnyStatus()
    }
    
    public boolean hasAchievedAnyStatus()
    {
        return hasAchievedCitizenship() || hasAchievedDACA() || hasAchievedLPR() || hasAchievedLPRConditionsRemoved() || hasAchievedLPRCardRenewed() || hasAchievedTPS()
    }
}
