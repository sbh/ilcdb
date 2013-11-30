import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class StatusAchievedController
{
    def index() { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ statiAchieved: StatusAchieved.list( params ) ]
    }

    def show() {
        def statusAchieved = StatusAchieved.get( params.id )

        if(!statusAchieved) {
            flash.message = "No Status Achieved found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ statusAchieved: statusAchieved] }
    }

    def delete() {
        def statusAvhieved = StatusAchieved.get( params.id )
        if(statusAvhieved) {
            def clientId = statusAvhieved.client.id;
            statusAvhieved.delete()
            flash.message = "Status Achieved ${params.id} deleted"
            redirect(controller:"client", action:"edit", params: [id: clientId])
        }
        else {
            flash.message = "No Status Achieve found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit() {
        def statusAchieved = StatusAchieved.get( params.id )

        if(!statusAchieved) {
            flash.message = "No Status Achieved found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ statusAchieved: statusAchieved]
        }
    }

    def update() {
        def statusAchieved = StatusAchieved.get( params.id )
        if(statusAchieved) {
            statusAchieved.properties = params

            if(!statusAchieved.hasErrors() && statusAchieved.save()) {
                flash.message = "Status Achieved ${params.id} updated"
                redirect(controller: "client", action:edit, params:[id:statusAchieved.client.id] )
            }
            else {
                render(view:'edit',model:[statusAchieved:statusAchieved])
            }
        }
        else {
            flash.message = "No Status Achieved found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create() {
        def statusAchieved = new StatusAchieved()
        statusAchieved.properties = params
        return ['statusAchieved':statusAchieved]
    }

    def save() {
        params['type'] = StatusAchieved.Type.fromValue(params['type'])
        println("SatusAchievedController.save params: "+params)
        def statusAchieved = new StatusAchieved(params)
        def client = Client.get(statusAchieved.client.id)
        if (statusAchieved.type == StatusAchieved.Type.Citizenship && client.hasAchievedCitizenship())  {
            statusAchieved.errors.rejectValue("type", 'status.multi_citizenship', 'Client has already achieved citizenship' )
            render(view:'create', model:[statusAchieved: statusAchieved])
        }
        else if(!statusAchieved.hasErrors() && statusAchieved.save()) {
            flash.message = "Status Achieved ${statusAchieved.id} created"
            redirect(controller: "client", action:edit, params:[id:statusAchieved.client.id] )
        }
        else {
            render(view:'create',model:[statusAchieved: statusAchieved])
        }
    }
}
