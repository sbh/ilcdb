
class CaseType
{
    String type;
    boolean deprecated = false;
    String associatedStatus;

    public boolean equals(Object other)
    {
        boolean retval = false

        if (other instanceof CaseType)
            retval id == ((CaseType)other).id

        return retval
    }
    
    public int hashCode()
    {
        return id
    }
    
    public String toString()
    {
        return id+":"+type
    }
}
