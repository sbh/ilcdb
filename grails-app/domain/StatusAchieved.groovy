import java.util.Date;

public class StatusAchieved
{
    StatusType statusType
    Date dateAchieved
    
    static belongsTo = [ client:Client ]
}
