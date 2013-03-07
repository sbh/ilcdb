class Conflict
{
    static mapping = 
    {
        cache true
    }
    
    static belongsTo = [ client:Client ]
    
    Person person
    Date createDate
    String reason

    static constraints =
    {
    }

    String toString() { return person.toString() }
}
