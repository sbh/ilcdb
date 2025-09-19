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
class ConflictController
{
    def index() { redirect(action:"list", params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list()
    {
        if(!params.max) params.max = 10
        [ conflictList: Conflict.list( params ) ]
    }

    def show()
    {
        def conflict = Conflict.get( params.id )

        if(!conflict)
        {
            flash.message = "Conflict not found with id ${params.id}"
            redirect(action:"list")
        }
        else
            [ conflict: conflict ]
    }

    def delete()
    {
        def conflict= Conflict.get( params.id )
        def client = conflict.client
        if(conflict)
        {
            conflict.delete()
            flash.message = "Conflict ${params.id} deleted"
            redirect(controller:'client', action:"edit", id:client.id)
        }
        else
            flash.message = "Conflict not found with id ${params.id}"
    }

    def edit()
    {
        def conflict = Conflict.get( params.id )

        if(!conflict)
        {
            flash.message = "Conflict not found with id ${params.id}"
            redirect(action:"list")
        }
        else
        {
            return [ conflict : conflict ]
        }
    }

    def update()
    {
        def conflict = Conflict.get( params.id )

        if(conflict)
        {
            conflict.properties = params
            if(!conflict.hasErrors() && conflict.save())
            {
                flash.message = "Conflict ${params.id} updated"
                redirect(controller:'client', action:"edit", id:params.client.id)
            }
            else
                render(view:'edit',model:[conflict:conflict])
        }
        else
        {
            flash.message = "Conflict not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create()
    {
        def conflict= new Conflict()
        conflict.properties = params
        return ['conflict' : conflict]
    }

    def save()
    {
        def conflict= new Conflict(params)

        if(!conflict.hasErrors() && conflict.save())
        {
            flash.message = "Conflict ${conflict.id} created"
            redirect(controller:'client', action:"edit", id:params.client.id)
        }
        else
            render(view:'create',model:[conflict:conflict])
    }
}
