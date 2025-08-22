class Conflict implements Comparable<Conflict>
{
    static mapping = 
    {
        cache true
    }
    
    static belongsTo = [ client:Client ]

    String firstName
    String lastName
    Date createDate
    String reason

    static constraints =
    {
    }

    def toMap() {
        [firstName : firstName,
         lastName  : lastName,
         createDate: createDate,
         reason    : reason]
    }

    String toString() { return lastName + ", " + firstName }

    int compareTo(Conflict o) {
        return toString().compareTo(o.toString())
    }
}
