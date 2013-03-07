class Appointment {

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
