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
class ClientSponsorRelationController {
    
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list() {
        if(!params.max) params.max = 10
        [ clientSponsorRelationList: ClientSponsorRelation.list( params ) ]
    }

    def show() {
        def clientSponsorRelation = ClientSponsorRelation.get( params.id )

        if(!clientSponsorRelation) {
            flash.message = "ClientSponsorRelation not found with id ${params.id}"
            redirect(action:"list")
        }
        else { return [ clientSponsorRelation : clientSponsorRelation ] }
    }

    def delete() {
        def clientSponsorRelation = ClientSponsorRelation.get( params.id )
        if(clientSponsorRelation) {
            clientSponsorRelation.delete()
            flash.message = "ClientSponsorRelation ${params.id} deleted"
            redirect(action:"list")
        }
        else {
            flash.message = "ClientSponsorRelation not found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit() {
        def clientSponsorRelation = ClientSponsorRelation.get( params.id )

        if(!clientSponsorRelation) {
            flash.message = "ClientSponsorRelation not found with id ${params.id}"
            redirect(action:"list")
        }
        else {
            return [ clientSponsorRelation : clientSponsorRelation ]
        }
    }

    def update() {
        def clientSponsorRelation = ClientSponsorRelation.get( params.id )
        if(clientSponsorRelation) {
            clientSponsorRelation.properties = params
            if(!clientSponsorRelation.hasErrors() && clientSponsorRelation.save()) {
                flash.message = "ClientSponsorRelation ${params.id} updated"
                redirect(action:"show", id:clientSponsorRelation.id)
            }
            else {
                render(view:'edit',model:[clientSponsorRelation:clientSponsorRelation])
            }
        }
        else {
            flash.message = "ClientSponsorRelation not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def clientSponsorRelation = new ClientSponsorRelation()
        clientSponsorRelation.properties = params
        return ['clientSponsorRelation':clientSponsorRelation]
    }

    def save() {
        def clientSponsorRelation = new ClientSponsorRelation(params)
        if(!clientSponsorRelation.hasErrors() && clientSponsorRelation.save()) {
            flash.message = "ClientSponsorRelation ${clientSponsorRelation.id} created"
            redirect(action:"show", id:clientSponsorRelation.id)
        }
        else {
            render(view:'create',model:[clientSponsorRelation:clientSponsorRelation])
        }
    }
}
