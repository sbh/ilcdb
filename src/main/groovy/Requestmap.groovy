import grails.gorm.Entity;

/**
 * Domain class for Request Map.
 */
class Requestmap implements Entity {

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
