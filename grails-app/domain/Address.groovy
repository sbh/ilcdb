class Address
{
    String street
    String city
    String county
    String state
    Country country
    String postalCode

    static mapping =
    {
        cache true
    }
    
    static belongsTo = [ person:Person ]

    static constraints =
    {
        street(blank:false)
        city(blank:false)
    }

    def toMap() {
        [street: street,
         city: city,
         county: county,
         state: state,
         country: country.name,
         postalCode: postalCode]
    }

    String toShortString()
    {
        def stringList = []
        if(city != null && city.size() > 0)
            stringList += city
        else
            stringList += "n/a"
        if(county != null && county.size() > 0) 
            stringList += county
        else
            stringList += "n/a"
        if(state != null && state.size() > 0) 
            stringList += state
        else
            stringList += "n/a"
        return stringList.join(', ')
    }

    String toString()
    { 
        def stringList = []

        if(street != null && street.size() > 0)
            stringList += street

        if(city != null && city.size() > 0)
            stringList += city

        if(county != null && county.size() > 0)
            stringList += "${county} county"

        if(state != null && state.size() > 0)
            stringList += state

        stringList += country.name

        if(postalCode != null && postalCode.size() > 0)
            stringList += postalCode

        return stringList.join(', ')
    }
}
