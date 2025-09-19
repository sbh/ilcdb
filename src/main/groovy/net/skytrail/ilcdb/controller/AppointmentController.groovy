package net.skytrail.ilcdb.controller;
import java.util.GregorianCalendar;
import org.springframework.security.access.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class AppointmentController {
    
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ appointmentList: Appointment.list( params ) ]
    }

    def show() {
        def appointment = Appointment.get( params.id )

        if(!appointment) {
            flash.message = "Appointment not found with id ${params.id}"
            redirect(action:"list")
        }
        else { return [ appointment : appointment ] }
    }

    def delete() {
        def appointment = Appointment.get( params.id )
        if(appointment) {
            appointment.delete()
            flash.message = "Appointment ${params.id} deleted"
            redirect(action:"list")
        }
        else {
            flash.message = "Appointment not found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit() {
        def appointment = Appointment.get( params.id )

        if(!appointment) {
            flash.message = "Appointment not found with id ${params.id}"
            redirect(action:"list")
        }
        else {
            return [ appointment : appointment ]
        }
    }

    def update() {
        def appointment = Appointment.get( params.id )
        if(appointment) {
            appointment.properties = params
            if(!appointment.hasErrors() && appointment.save()) {
                flash.message = "Appointment ${params.id} updated"
                redirect(action:"show", id:appointment.id)
            }
            else {
                render(view:'edit', model:[appointment:appointment])
            }
        }
        else {
            flash.message = "Appointment not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def appointment = new Appointment()
        appointment.properties = params
        return ['appointment':appointment]
    }

    def save() {
        def appointment = new Appointment(params)
        if(!appointment.hasErrors() && appointment.save()) {
            flash.message = "Appointment ${appointment.id} created"
            redirect(action:"show", id:appointment.id)
        }
        else {
            render(view:'create', model:[appointment:appointment])
        }
    }

    def search() { 
        def dateBinder = new DateBinder(params)
        if(!params.max) params.max = 10
        else params.max = params.max.toInteger()

        if(!params.offset) params.offset = 0
        else paramas.offset = params.offset.toInteger()

        def searchResults = []

        if(params.start != null && params.end != null) {

            String query = ''' 
	    FROM Appointment AS appointment 
	    WHERE appointment.date > ? AND appointment.date < ?
	    '''

            searchResults += Appointment.executeQuery(query, [dateBinder.start, dateBinder.end])

            searchResults = searchResults.sort().unique()
            params.count = searchResults.size()

            if(params.max < searchResults.size()) {
                def tempResults = []
                (params.offset ..< (params.offset + params.max)).each {
                    if(it < searchResults?.size()) {
                        tempResults += searchResults[it]
                    }
                }
                searchResults = tempResults
            }
            //if(searchResults.size() < 1) flash.message = 'No results found'
        }

        params.start = dateBinder.start
        params.end = dateBinder.end
        return [searchResults:searchResults, params:params ]

    }

    def quickview() {
        def params = [:]

        def start = new GregorianCalendar()
        params.start = 'struct'
        params.start_day = start.get(GregorianCalendar.DAY_OF_MONTH)
        params.start_month = start.get(GregorianCalendar.MONTH) + 1
        params.start_year = start.get(GregorianCalendar.YEAR)
        params.start_second = start.get(GregorianCalendar.SECOND)
        params.start_minute = start.get(GregorianCalendar.MINUTE)
        params.start_hour = start.get(GregorianCalendar.HOUR)

        def end = new GregorianCalendar()
        end.add(GregorianCalendar.DAY_OF_YEAR, 10)
        params.end = 'struct'
        params.end_day = end.get(GregorianCalendar.DAY_OF_MONTH)
        params.end_month = end.get(GregorianCalendar.MONTH) + 1
        params.end_year = end.get(GregorianCalendar.YEAR)
        params.end_second = end.get(GregorianCalendar.SECOND)
        params.end_minute = end.get(GregorianCalendar.MINUTE)
        params.end_hour = end.get(GregorianCalendar.HOUR)

        redirect(action:'search', params:params)
    }
}

class DateBinder {
    Date start
    Date end
}
