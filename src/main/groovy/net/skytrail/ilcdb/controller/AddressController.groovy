package net.skytrail.ilcdb.controller;
import net.skytrail.ilcdb.domain.AMI
import net.skytrail.ilcdb.domain.Address
import net.skytrail.ilcdb.domain.Appointment
import net.skytrail.ilcdb.domain.BirthPlace
import net.skytrail.ilcdb.domain.CaseResult
import net.skytrail.ilcdb.domain.CaseType
import net.skytrail.ilcdb.domain.Client
import net.skytrail.ilcdb.domain.ClientCase
import net.skytrail.ilcdb.domain.ClientSponsorRelation
import net.skytrail.ilcdb.domain.Conflict
import net.skytrail.ilcdb.domain.Country
import net.skytrail.ilcdb.domain.Note
import net.skytrail.ilcdb.domain.Person
import net.skytrail.ilcdb.domain.Requestmap
import net.skytrail.ilcdb.domain.Role
import net.skytrail.ilcdb.domain.ServiceRecord
import net.skytrail.ilcdb.domain.Sponsor
import net.skytrail.ilcdb.domain.StatusAchieved
import net.skytrail.ilcdb.domain.StatusType
import net.skytrail.ilcdb.domain.User
import net.skytrail.ilcdb.domain.UserRole

import org.springframework.security.access.annotation.Secured

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
