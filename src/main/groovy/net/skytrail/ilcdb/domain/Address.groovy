package net.skytrail.ilcdb.domain;
import net.skytrail.ilcdb.domain.AMI
import net.skytrail.ilcdb.domain.Address
import net.skytrail.ilcdb.domain.Appointment
import net.skytrail.ilcdb.domain.BirthPlace
import net.skytrail.ilcdb.domain.CaseResult
import net.skytrail.ilcdb.domain.CaseType
import net.skytrail.ilcdb.domain.Client
import net.skytrail.ilcdb.domain.ClientCase
import net.skytrail.ilcdb.domain.ClientSponsorRelation
import net.skytrail.ilcdb.domain.Conflict
import net.skytrail.ilcdb.domain.Country
import net.skytrail.ilcdb.domain.Note
import net.skytrail.ilcdb.domain.Person
import net.skytrail.ilcdb.domain.Requestmap
import net.skytrail.ilcdb.domain.Role
import net.skytrail.ilcdb.domain.ServiceRecord
import net.skytrail.ilcdb.domain.Sponsor
import net.skytrail.ilcdb.domain.StatusAchieved
import net.skytrail.ilcdb.domain.StatusType
import net.skytrail.ilcdb.domain.User
import net.skytrail.ilcdb.domain.UserRole

import grails.gorm.Entity;

class Address implements Entity {
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
