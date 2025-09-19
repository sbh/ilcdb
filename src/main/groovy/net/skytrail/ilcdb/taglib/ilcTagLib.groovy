package net.skytrail.ilcdb.taglib;
import grails.util.GrailsUtil;

class ilcTagLib {
    
    /* Defines a tag to create an address value in place.
     * EG <g:addressPicker name="placeOfBirth" value="${new Address}" />
     */
    
    def addressGenerator = { attribs ->
        
        def value = attribs['value']
        def name  = attribs['name'] 
        
        if(value == null) {
            value = new Address()
        } 
        else {
            if(!(value instanceof Address))
            throwTagError("Invalid value passed to addressGenerator tag")
        }
        
        out << render(template:"/templates/addressTemplate", model:[name:name,address:value])
    }
    
    def birthPlaceGenerator = { attribs ->
        
        def value = attribs['value']
        String name  = attribs['name'] 
        
        if(value == null) {
            value = new BirthPlace()
        } 
        else {
            if(!(value instanceof BirthPlace))
            throwTagError("Invalid value passed to birthPlaceGenerator tag")
        }
        
        out << render(template:"/templates/birthPlaceTemplate", model:[name:name, birthPlace:value])
    }
    
    def personGenerator = { attribs ->
        
        def value = attribs['value']
        def name  = attribs['name'] 
        
        if(value == null) {
            value = new Person()
        } 
        else {
            if(!(value instanceof Person))
            throwTagError("Invalid value passed to personGenerator tag")
        }
        
        out << render(template:"/templates/personTemplate", model:[name:name,person:value])
    }
    
    def personSelectorGenerator = { attribs ->
        
        def value = attribs['value']
        def name = attribs['name']
        
        if(name == null) {
            throwTagError("Invalid name passed to personSelectorGenerator tag")
        }
        
        if(value == null) {
            value = new Person()
        }
        else if(!(value instanceof Person)) { 
            throwTagError("Invalid value passed to personSelectorGenerator tag")
        }
        
        out << render(template:"/templates/personSelectorTemplate", model:[name:name, person:value])
    }
    def clientPreviousAppointment = { attribs ->
        
        def client = attribs['client']
        
        if((client == null) || !(client instanceof Client)) {
            return "Error: client ${client} does not exist!"
        }
        
        String query = '''
    FROM Appointment AS appointment 
    WHERE appointment.date < ? AND appointment.client.id = ? 
    ORDER BY appointment.date DESC
    '''
        
        def lastAppointment = Appointment.executeQuery( query, [ new Date(), client.id ])
        
        if(lastAppointment.size() > 0) {
            out << g.link(controller:'appointment', action:'show', id:"${lastAppointment[0].id}") { "${lastAppointment[0]}" }
        }
        else {
            out << "No previous appointment"
        }
    }
    
    def clientNextAppointment = { attribs ->
        
        def client = attribs['client']
        
        if((client == null) || !(client instanceof Client)) {
            return "Error: client ${client} does not exist!"
        }
        
        String query = '''
    FROM Appointment AS appointment 
    WHERE appointment.date > ? AND appointment.client.id = ? 
    ORDER BY appointment.date ASC
    '''
        
        def lastAppointment = Appointment.executeQuery( query, [ new Date(), client.id ])
        
        if(lastAppointment.size() > 0) {
            out << g.link(controller:'appointment', action:'show', id:"${lastAppointment[0].id}") { "${lastAppointment[0]}" }
        }
        else {
            out << "No upcoming appointment"
        }
    }
    
    /* Given an object, this will create a a link back to the controller show page for that object if
     * the object has a controller in the application.
     * This assumes, of course, that there *is* a show page.
     */
    def searchResultObject = { attribs ->
        
        def value = attribs['value']
        
        def type = value?.properties.class
        def controller //For storing the name of the controller 
        
        if(grailsApplication.controllerClasses.any{ controller = it; return it.name == type?.name }) 
        out.println "<span class=\"result\"><a href=\"show/${value?.id}\">${controller.name}: ${value}</a>" 
        else 
        out.println "<span>${value} (No link available)</span>"
        
    }
    
    //Generates links and dividers for the navigation bar
    def navLinks = { attribs, body -> 
       
        def clientid = attribs["clientid"]
        String divider = "|"
        def links = []
        
        links += render(template:"/templates/navDestinationLinks", bean:clientid)
          
        if(body())
        {
            links += body()
        }
		else
        {            
            divider = ""
        }
		
        links += render(template:"/templates/authLinks")
        out << links.join(divider)
		
		
    }
	def test = { attribs, body ->
    
		if(GrailsUtil.getEnvironment() != "production")
		{
			out << body();
		}	
	}
}
