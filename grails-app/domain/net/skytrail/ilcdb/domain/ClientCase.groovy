package net.skytrail.ilcdb.domain;

import gorm.transform.Entity
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Entity
class ClientCase implements Comparable<ClientCase> {
    public static final String STAFF_ADVISE = "Staff Advise"
    public static final String STAFF_REPRESENTATION = "Staff Representation"
    private static final DateTimeFormatter briefDateFormat = DateTimeFormat.forPattern("MMM-dd-yyyy");


    //Can we make this configurable?
    public static final List ATTORNEYS = ["Belen", "Laurel", "Maria"]
    static belongsTo = [ client:Client ]
    static hasMany = [ notes : Note ]

    String coltafNumber = ""
    String caseNumber = ""
    String fileLocation = "Alpha"
    String intakeType
    Date startDate
    Date completionDate
    String attorney
    SortedSet notes
    CaseType caseType
    CaseResult caseResult
    String intensity = "1"

    static mapping =
    {
        cache true
    }

    static constraints =
    {
        attorney(inList: ATTORNEYS)
        intakeType(inList:[STAFF_ADVISE, STAFF_REPRESENTATION])
        startDate(nullable:true)
        completionDate(nullable:true)
        caseNumber(nullable:true)
        coltafNumber(nullable:true)
        attorney(nullable:true)
        intakeType(nullable:true)
        caseType(nullable:true, validator: { val, obj -> if (obj.intakeType.equals(STAFF_REPRESENTATION) && obj.caseType == null) return "notNull" })
        caseResult(nullable:true)
        intensity(inList:["-Choose-", "1", "2", "3", "4", "5", "n/a"])
    }

    static transients = [ "startDateString", "completionDateString", "open", "valid", "fileLocation" ]

    def toMap() {
        [coltafNumber  : coltafNumber,
         caseNumber    : caseNumber,
         fileLocation  : fileLocation,
         intakeType    : intakeType,
         startDate     : startDate,
         completionDate: completionDate,
         attorney      : attorney,
         notes         : notes.collect { it.toMap() },
         caseType      : caseType?.toMap(),
         caseResult    : caseResult?.toMap(),
         intensity     : intensity]
    }

    int compareTo(ClientCase otherIntake)
    {
        if (startDate == null || otherIntake.startDate == null)
            return id.compareTo(otherIntake.id)

        if (startDate.equals(otherIntake.startDate))
            return id.compareTo(otherIntake.id)
        return startDate.compareTo(otherIntake.startDate);
    }

    public String getStartDateString()
    {
        if (startDate == null)
            return "Ongoing"
        else
            return briefDateFormat.print(startDate.getTime())
    }

    public String getCompletionDateString()
    {
        if (completionDate == null)
            return "Ongoing";
        return briefDateFormat.print(completionDate.getTime())
    }

    public String getIntensity()
    {
        return intensity
    }

    public boolean isOpen()
    {
        return completionDate == null;
    }

    boolean isStaffAdvise() {
        return intakeType == STAFF_ADVISE
    }

    boolean isStaffRepresentation() {
        return intakeType == STAFF_REPRESENTATION
    }

    public isSuccessful()
    {
        return caseResult?.successfulResult
    }

    public isStatusAchieved()
    {
        return caseResult?.statusWasAchieved
    }

    public boolean isValid()
    {
        return STAFF_REPRESENTATION.equals(intakeType) || STAFF_ADVISE.equals(intakeType)
    }

    String toDebugString()
    {
        return "intake type: "+intakeType+", caseResult: "+caseResult+", coltafNumber: "+coltafNumber+", intensity: "+intensity
    }

    String toString()
    {
        String intakeTypeStr = ""
        if (intakeType)
            intakeTypeStr = "Intake Type: ${intakeType}"
        String caseResultStr = ""
        if (caseResult != null)
            caseResultStr = ", "+caseResult.result
        String caseTypeStr = ""
        if (STAFF_REPRESENTATION.equals(intakeType))
            caseTypeStr = ", Case Type: ${caseType}"
        String coltafNumberStr = ""
        if (coltafNumber)
            coltafNumberStr = ", COLTAF Number: ${coltafNumber}"
        String retval = "${intakeTypeStr}${caseTypeStr}${coltafNumberStr}${caseResultStr}".replaceAll(",+", ",").stripMargin(",")
        if (retval.equals(""))
            retval = "unknown"
        return retval
    }
}
