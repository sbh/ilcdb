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

class Sponsor implements Entity {

    static mapping = {
        cache true
    }

    static hasMany = [clientRelations:ClientSponsorRelation]
    Person sponsor
    String income

    static constraints = {

        income(inList:[
            'Less than $10,000',
            '$10,000 - $20,000',
            '$20,000 - $30,000',
            '$30,000 - $40,000',
            '$40,000 - $50,000',
            '$50,000 - $60,000',
            '$60,000 - $70,000',
            '$70,000 - $80,000',
            '$80,000 - $90,000',
            '$90,000 - $100,000',
            'Greater than $100,000',
        ])
    }

    String toString() { return sponsor.toString() }
}
