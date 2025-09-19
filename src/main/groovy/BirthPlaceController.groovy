import org.springframework.security.access.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class BirthPlaceController {
    
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ birthPlaceList: BirthPlace.list( params ) ]
    }

    def show() {
        def birthPlace = BirthPlace.get( params.id )

        if(!birthPlace) {
            flash.message = "BirthPlace not found with id ${params.id}"
            redirect(action:"list")
        }
        else { return [ birthPlace : birthPlace ] }
    }

    def delete() {
        def birthPlace = BirthPlace.get( params.id )
        if(birthPlace) {
            birthPlace.delete()
            flash.message = "BirthPlace ${params.id} deleted"
            redirect(action:"list")
        }
        else {
            flash.message = "BirthPlace not found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit() {
        def birthPlace = BirthPlace.get( params.id )

        if(!birthPlace) {
            flash.message = "BirthPlace not found with id ${params.id}"
            redirect(action:"list")
        }
        else {
            return [ birthPlace : birthPlace ]
        }
    }

    def update() {
        def birthPlace = BirthPlace.get( params.id )
        if(birthPlace) {
            birthPlace.properties = params
            if(!birthPlace.hasErrors() && birthPlace.save()) {
                flash.message = "BirthPlace ${params.id} updated"
                redirect(action:"show", id:birthPlace.id)
            }
            else {
                render(view:'edit', model:[birthPlace:birthPlace])
            }
        }
        else {
            flash.message = "BirthPlace not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def birthPlace = new BirthPlace()
        birthPlace.properties = params
        return ['birthPlace':birthPlace]
    }

    def save() {
        def birthPlace = new BirthPlace(params)
        if(!birthPlace.hasErrors() && birthPlace.save()) {
            flash.message = "BirthPlace ${birthPlace.id} created"
            redirect(action:"show", id:birthPlace.id)
        }
        else {
            render(view:'create', model:[birthPlace:birthPlace])
        }
    }
}
