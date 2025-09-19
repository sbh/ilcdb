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
