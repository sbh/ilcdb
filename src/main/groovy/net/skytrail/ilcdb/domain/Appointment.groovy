package net.skytrail.ilcdb.domain;
import grails.gorm.Entity;

class Appointment implements Entity {

    Date date
    String note
    
    static mapping = {
        cache true
    }
    
    static belongsTo = [ client:Client ]
    
    String toString() {
	return date
    }
}
