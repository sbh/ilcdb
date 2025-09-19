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
class SponsorController {

    def index() { redirect(action:"search", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ sponsorList: Sponsor.list( params ) ]
    }

    def show() {
        def sponsor = Sponsor.get( params.id )

        if(!sponsor) {
            flash.message = "Sponsor not found with id ${params.id}"
            redirect(action:"search")
        }
        else { return [ sponsor : sponsor ] }
    }

    def delete() {
        def sponsor = Sponsor.get( params.id )
        if(sponsor) {
            sponsor.delete()
            flash.message = "Sponsor ${params.id} deleted"
            redirect(action:"search")
        }
        else {
            flash.message = "Sponsor not found with id ${params.id}"
            redirect(action:"search")
        }
    }

    def edit() {
        def sponsor = Sponsor.get( params.id )

        if(!sponsor) {
            flash.message = "Sponsor not found with id ${params.id}"
            redirect(action:"search")
        }
        else {
            return [ sponsor : sponsor ]
        }
    }

    def update() {
        def sponsor = Sponsor.get( params.id )
        if(sponsor) {
            sponsor.properties = params
            if(!sponsor.hasErrors() && sponsor.save()) {
                flash.message = "Sponsor ${params.id} updated"
                redirect(action:"show", id:sponsor.id)
            }
            else {
                render(view:'edit', model:[sponsor:sponsor])
            }
        }
        else {
            flash.message = "Sponsor not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def sponsor = new Sponsor()
        sponsor.properties = params
        return ['sponsor':sponsor]
    }

    def save() {
        if(params.containsKey('personSource') && params['personSource'] == 'new') {
            //For generating a new person
            def sponsor = new Sponsor(params)
            def person = new Person(params.sponsor)
            def address = new Address(params.sponsor.address)

            if(!address.hasErrors() && address.validate()) {
                person.address = address
            }

            if(!person.hasErrors() && person.validate()) {
                sponsor.sponsor = person
            }

            if(!sponsor.hasErrors() && address.validate() &&
            person.validate() && sponsor.validate()) {

                address.save()
                person.save()
                sponsor.save()

                flash.message = "Sponsor ${sponsor.id} created"
                redirect(action:"show", id:sponsor.id)
            }
            else {
                person.address = address
                sponsor.sponsor = person
                render(view:'create',model:[sponsor:sponsor])
            }
        }
        else if(params.containsKey('personSource') && params['personSource'] == 'existing') {
            def sponsor = new Sponsor(params)
            if(!sponsor.hasErrors() && sponsor.save()) {
                flash.message = "Sponsor ${sponsor.id} created"
                redirect(action:"show", id:sponsor.id)
            }
            else {
                render(view:'create',model:[sponsor:sponsor])
            }
        }
        else {
            /*The user is passing custom parameters. Something bad is going on!
             *That or someone is modifying the application, in which case they
             *should see this. Consider logging the user out and noting an error?
             */
            render "Critical error. Please contact your administrator immediately."
        }
    }
}
