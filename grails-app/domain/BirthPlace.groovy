class BirthPlace
{
    String city
    String state
    Country country;
    
    static mapping = {
        cache true
    }
    
    static constraints = 
    {
        city(blank:false)
    }
    
    String toString() 
    {
        def stringList = []
        
        if(city != null && city.size() > 0 && !"NONE".equals(city.trim()))
            stringList += city
        
        if(state != null && state.size() > 0 && !"NONE".equals(state.trim()))
            stringList += state
        
        stringList += country.name
        
        return stringList.join(', ')
    }
}
