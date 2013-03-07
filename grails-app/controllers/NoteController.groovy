import grails.plugins.springsecurity.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class NoteController
{
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list =
    {
        if(!params.max) params.max = 10
        [ noteList: Note.list( params ) ]
    }

    def show =
    {
        def note = Note.get( params.id )

        if(!note)
        {
            flash.message = "Note not found with id ${params.id}"
            redirect(action:list)
        }
        else
        { 
            def returnValue = [ note : note, "noteType" : params.noteType ]
            if(params.noteType == "clientCase")
                returnValue[ "clientcaseid" ] = params.clientcaseid 
            else
                returnValue[ "clientid" ] = params.clientid
            return returnValue;    
        }
    }

    def delete =
    {
        def note = Note.get( params.id )
        if(note)
        {
            note.delete()
            flash.message = "Note ${params.id} deleted"
        }
        else
            flash.message = "Note not found with id ${params.id}"

        if (params.noteType == "clientCase")
            redirect(controller:'clientCase', action:edit, id:params.clientcaseid)
        else
            redirect(controller:'client', action:edit, id:params.clientid)
    }

    def edit =
    {
        def note = Note.get( params.id )

        if(!note)
        {
            flash.message = "Note not found with id ${params.id}"
            redirect(action:list)
        }
        else
        {
            if (note.type == 'client')
                return [ note : note, noteType:params['noteType'], clientid:params['clientid'] ]
            else
                return [ note : note, noteType:params['noteType'], clientcaseid:params['clientcaseid'] ]
        }
    }

    def update =
    {
        def note = Note.get( params.id )
        note.type = params.noteType

        if(note)
        {
            note.properties = params
            if(!note.hasErrors() && note.save())
            {
                flash.message = "Note ${params.id} updated"
                if(params.noteType == "clientCase")
                    redirect(controller:'clientCase', action:edit, id:params.clientcaseid)
                else
                    redirect(controller:'client', action:edit, id:params.clientid)
            }
            else
                render(view:'edit',model:[note:note])
        }
        else
        {
            flash.message = "Note not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create =
    {
        def note = new Note()
        note.properties = params
        def returnValue = ['note' : note, 'noteType' : params.noteType ]
        if(params.noteType == "clientCase")
            return [clientcaseid : params.clientcaseid, noteType : 'clientCase']
        else
            return [clientid : params.clientid, noteType : 'client']
    }

    def save =
    {
        if(params.noteType == "clientCase")
            params.intake = ClientCase.get(params.clientcaseid);
        else
            params.client = Client.get(params.clientid)    
        
        def note = new Note(params)
        note.type = params.noteType
        
        if(!note.hasErrors() && note.save())
        {
            flash.message = "Note ${note.id} created"
            if(params.noteType == "clientCase")
                redirect(controller:'clientCase', action:edit, id:params.clientcaseid)
            else
                redirect(controller:'client', action:edit, id:params.clientid)
        }
        else
            render(view:'create',model:[note:note])
    }
}
