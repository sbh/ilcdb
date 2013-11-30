import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class ClientCaseController
{
    
    def index() { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list()
    {
        if(!params.max) params.max = 10

        def casesList = []

        // Only display cases with specified type
        if(params.containsKey('type'))
        {
            def criteria = ClientCase.createCriteria()
            casesList = criteria.list
            {
                like('caseType', params['type'])
                if(params.containsKey('sort') && params.containsKey('order'))
                   order(params['sort'], params['order'])
            }
        }
        else //Return everything!
            casesList = ClientCase.list( params )

        return [ clientCaseList: casesList ]
    }

    def show()
    {
        def clientCase = ClientCase.get( params.id )

        if(!clientCase)
        {
            flash.message = "ClientCase not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ clientCase : clientCase ] }
    }

    def delete()
    {
        def clientCase = ClientCase.get( params.id )
        if(clientCase)
        {
            clientCase.delete()
            flash.message = "ClientCase ${params.id} deleted"
            redirect(controller:"client", action:edit, id:clientCase?.client?.id)
        }
        else
        {
            flash.message = "ClientCase not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit()
    {
        //println("ClientCaseController edit params: "+params)
        
        def clientCase = ClientCase.get( params.id )

        if(!clientCase)
        {
            flash.message = "ClientCase not found with id ${params.id}"
            redirect(action:list)
        }
        else
        {
            if (clientCase.startDate == null)
            {
                def client = Client.get(clientCase.client.id)
                clientCase.startDate = client.firstVisit
            }
            
            if (!clientCase.intakeType.equals(ClientCase.STAFF_ADVISE) && !clientCase.intakeType.equals(ClientCase.STAFF_REPRESENTATION))
            {
                clientCase.intakeType = ClientCase.STAFF_ADVISE
                clientCase.completionDate = clientCase.startDate
            }
            return [ clientCase : clientCase ]
        }
    }

    def update()
    {
        //println("ClientCaseController update params: "+params)

        def clientCase = ClientCase.get( params.id )
        if (clientCase)
        {
            def caseTypeId = params.caseType
            params.caseType = null
            def caseResultId = params.caseResult
            params.caseResult = null

            clientCase.properties = params
            def client = clientCase.client
            
            // if an intake gets changed from staff represent to staff advise,
            // need to clean up the extra fields that we don't need
            if (clientCase.intakeType.equals(ClientCase.STAFF_ADVISE))
            {
                clientCase.completionDate = clientCase.startDate;
                clientCase.caseType = null;    
                clientCase.caseResult = null;
            }
            else
            {
                if (caseTypeId != null && !"null".equals(caseTypeId))
                    clientCase.caseType = CaseType.get(caseTypeId)
                else
                    clientCase.caseType = null

                if (caseResultId != null && !"null".equals(caseResultId))
                    clientCase.caseResult = CaseResult.get(caseResultId)
                else
                    clientCase.caseResult = null
            }
            
            client.fileLocation = params['fileLocation']
            
            if (!clientCase.hasErrors() && clientCase.save())
            {
                client.save()
                flash.message = "ClientCase ${params.id} updated"
                redirect(controller:"client", action:edit, id:clientCase?.client?.id)
            }
            else
            {
                render(view:'edit',model:[clientCase:clientCase])
            }
        }
        else
        {
            flash.message = "ClientCase not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create()
    {
        def clientCase = new ClientCase()
        clientCase.properties = params
        return ['clientCase':clientCase]
    }

    def save()
    {
        //println("ClientCaseController save params: "+params)

        def client = Client.get( params.clientId )
        
        def caseTypeId = params.caseType
        params.caseType = null
        def caseResultId = params.caseResult
        params.caseResult = null

        def clientCase = new ClientCase(params)
        clientCase.client = client
        client.fileLocation = params['fileLocation']
        
        // if an intake gets changed from staff represent to staff advise,
        // need to clean up the extra fields that we don't need
        if(clientCase.intakeType.equals(ClientCase.STAFF_ADVISE))
        {
            clientCase.completionDate = clientCase.startDate;
            clientCase.caseType = null;
            clientCase.caseResult = null;
        }
        else
        {
            if (caseTypeId != null && !"null".equals(caseTypeId))
                clientCase.caseType = CaseType.get(caseTypeId)
            else
                clientCase.caseType = null

            if (caseResultId != null && !"null".equals(caseResultId))
                clientCase.caseResult = CaseResult.get(caseResultId)
            else
                clientCase.caseResult = null
        }

        if(!clientCase.hasErrors() && clientCase.save())
        {
            client.save()
            flash.message = "ClientCase ${clientCase.id} created"
            redirect(controller:"client", action:edit, id:client.id)
        }
        else
            render(view:'create',model:[clientCase:clientCase])
    }
}
