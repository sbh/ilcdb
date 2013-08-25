import java.text.SimpleDateFormat

class StatusAchieved
{
    static belongsTo = [ client:Client ]

    enum Type
    {
        Citizenship("Citizenship"),
        LPR("LPR"),
        DACA("DACA"),
        TPS("TPS"),
        LPRConditionsRemoved("LPR Conditions Removed"),
        LPRCardRenewed("LPR Card Renewed")

        private final String value;

        Type(String value)
        {
            this.value = value;
        }

        public String toString()
        {
            return value;
        }

        static list()
        {
            [Citizenship, DACA, LPR, LPRCardRenewed, LPRConditionsRemoved, TPS]
        }

        public static Type fromValue(String aStatusTypeValue)
        {
            for (Type statusType : EnumSet.allOf(Type.class))
            {
                if (aStatusTypeValue.equalsIgnoreCase(statusType.toString()))
                    return statusType;
            }
            return null;
        }
    };

    Type type
    Date date

    static constraints =
    {
    }

    static transients = ["statusAchievedDateString"]

    String getStatusAchievedDateString()
    {
        if (date instanceof Date)
            return (new SimpleDateFormat("yyyy-MM-dd").format(date));
        return "";
    }
    public String toString()
    {
        return "${type}: ${getStatusAchievedDateString()}";
    }
}
