package net.skytrail.ilcdb.domain;
import grails.gorm.Entity;

class BirthPlace implements Entity {
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

    def toMap() {
        [city   : city,
         state  : state,
         country: country.name]
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
