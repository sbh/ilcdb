package net.skytrail.ilcdb.domain;
import gorm.transform.Entity


@Entity
class Appointment @Entity {

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
