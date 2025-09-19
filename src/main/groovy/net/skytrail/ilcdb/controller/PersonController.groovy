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
class PersonController {
    
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ personList: Person.list( params ) ]
    }

    def show() {
        def person = Person.get( params.id )

        if(!person) {
            flash.message = "Person not found with id ${params.id}"
            redirect(action:"search")
        }
        else { return [ person : person ] }
    }

    def delete() {
        def person = Person.get( params.id )
        if(person) {
            person.delete()
            flash.message = "Person ${params.id} deleted"
            redirect(action:"search")
        }
        else {
            flash.message = "Person not found with id ${params.id}"
            redirect(action:"search")
        }
    }

    def edit() {
        def person = Person.get( params.id )

        if(!person) {
            flash.message = "Person not found with id ${params.id}"
            redirect(action:"search")
        }
        else {
            return [ person : person ]
        }
    }

    def update() {
        def person = Person.get( params.id )
        if(person) {
            person.properties = params
            if(!person.hasErrors() && person.save()) {
                flash.message = "Person ${params.id} updated"
                redirect(action:"show", id:person.id)
            }
            else {
                render(view:'edit', model:[person:person])
            }
        }
        else {
            flash.message = "Person not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def person = new Person()
        person.properties = params
        return ['person':person]
    }

    def save() {

        def person = new Person()
	
	person.properties = params

        if(!person.hasErrors() && person.save()) {
	    
	    flash.message = "Person ${person.id} created"
            redirect(action:"show", id:person.id)
        }
        else {
            render(view:'create', model:[person:person])
        }
    }
}

