import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

class Note implements Comparable<Note>
{
    private static final DateTimeFormatter briefDateFormat = DateTimeFormat.forPattern("MMM-dd-yyyy");
    
    static mapping =
    {
        cache true
        text type:'text'
    }
    static belongsTo = [ client:Client, intake:ClientCase ]
    
    static transients = ["formattedDate"]
    
    String type    
    
    static constraints = {
        client(nullable:true)
        intake(nullable:true)
        type(inList:["client", "clientCase"])
    }
    
    String text
    Date createDate

    String toString() 
    { 
        def regex = (/<(\w|\/\w)[^>]*>/) //Regex to remove tags but leave the contents of tags
        def matcher = (text =~ /$regex/)
        def strippedTags = matcher.replaceAll('')
        return strippedTags.replaceAll("&nbsp;", "")
    }
    
    String getFormattedDate()
    {
        return briefDateFormat.print(createDate.getTime());
    }
    
    int compareTo(otherNote)
    {
        createDate.compareTo(otherNote.createDate)
    }

    public boolean equals(Object other)
    {
        if ( !(other instanceof Note) )
            return false;
            
        return id == ((Note)other).id
    }
    
    public int hashCode()
    {
        return id
    }
}

