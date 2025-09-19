package net.skytrail.ilcdb.domain;
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
