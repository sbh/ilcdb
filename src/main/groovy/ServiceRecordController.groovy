import org.springframework.security.access.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class ServiceRecordController 
{
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ serviceRecords: ServiceRecord.list( params ) ]
    }

    def show() {
        def serviceRecord = ServiceRecord.get( params.id )

        if(!serviceRecord) {
            flash.message = "No Service Record found with id ${params.id}"
            redirect(action:"list")
        }
        else { return [ serviceRecord : serviceRecord ] }
    }

    def delete() {
        def serviceRecord = ServiceRecord.get( params.id )
        if(serviceRecord) {
            serviceRecord.delete()
            flash.message = "Service Record ${params.id} deleted"
            redirect(action:"list")
        }
        else {
            flash.message = "No Service Record found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit() {
        def serviceRecord = ServiceRecord.get( params.id )

        if(!serviceRecord) {
            flash.message = "No Service Record found with id ${params.id}"
            redirect(action:"list")
        }
        else {
            return [ serviceRecord : serviceRecord ]
        }
    }

    def update() {
        def serviceRecord = ServiceRecord.get( params.id )
        if(serviceRecord) {
            serviceRecord.properties = params
            if(!serviceRecord.hasErrors() && serviceRecord.save()) {
                flash.message = "Service Record ${params.id} updated"
                redirect(action:"show", id:serviceRecord.id)
            }
            else {
                render(view:'edit',model:[serviceRecord:serviceRecord])
            }
        }
        else {
            flash.message = "No Service Record found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def serviceRecord = new ServiceRecord()
        serviceRecord.properties = params
        return ['serviceRecord':serviceRecord]
    }

    def save() {
        def serviceRecord = new ServiceRecord(params)
        if(!serviceRecord.hasErrors() && serviceRecord.save()) {
            flash.message = "Service Record ${serviceRecord.id} created"
            redirect(action:"show", id:serviceRecord.id)
        }
        else {
            render(view:'create',model:[serviceRecord:serviceRecord])
        }
    }
}
