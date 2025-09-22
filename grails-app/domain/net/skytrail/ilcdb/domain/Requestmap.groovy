package net.skytrail.ilcdb.domain;
import gorm.transform.Entity


/**
 * Domain class for Request Map.
 */
@Entity
class Requestmap @Entity {

	String url
	String configAttribute

    static mapping = {
        cache true
    }

	static constraints = {
		url(blank: false, unique: true)
		configAttribute(blank: false)
	}
}
