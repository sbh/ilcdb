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
