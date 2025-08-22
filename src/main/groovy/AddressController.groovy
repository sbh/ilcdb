import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
//@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class AddressController {
    
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ addressList: Address.list( params ) ]
    }

    def show() {
        def address = Address.get( params.id )

        if(!address) {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:"list")
        }
        else { return [ address : address ] }
    }

    def delete() {
        def address = Address.get( params.id )
        if(address) {
            address.delete()
            flash.message = "Address ${params.id} deleted"
            redirect(action:"list")
        }
        else {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit() {
        def address = Address.get( params.id )

        if(!address) {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:"list")
        }
        else {
            return [ address : address ]
        }
    }

    def update() {
        def address = Address.get( params.id )
        if(address) {
            address.properties = params
            if(!address.hasErrors() && address.save()) {
                flash.message = "Address ${params.id} updated"
                redirect(action:"show", id:address.id)
            }
            else {
                render(view:'edit', model:[address:address])
            }
        }
        else {
            flash.message = "Address not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def address = new Address()
        address.properties = params
        return ['address':address]
    }

    def save() {
        def address = new Address(params)
        if(!address.hasErrors() && address.save()) {
            flash.message = "Address ${address.id} created"
            redirect(action:"show", id:address.id)
        }
        else {
            render(view:'create', model:[address:address])
        }
    }
}
