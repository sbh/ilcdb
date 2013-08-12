import java.text.SimpleDateFormat

class StatusAchieved
 {
     static belongsTo = [ client:Client ]

     enum Type {
         Citizenship,
         LPR,
         DACA,
         TPS,
         LPRConditionsRemoved{
             public String toString(){
                 return "LPR Conditions Removed"
             }
         },
         LPRCardRenewed{
             public String toString(){
                 return "LPR Card Renewed"
             }
         };
     }

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
