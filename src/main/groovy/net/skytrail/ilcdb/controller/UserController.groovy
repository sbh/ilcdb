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

class UserController
{
    def springSecurityService // Dependency injection for the springSecurityService.

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index()
    {
        redirect(action: "search", params: params)
    }

    def list()
    {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [userInstanceList: User.list(params), userInstanceTotal: User.count()]
    }

    def create()
    {
        def userInstance = new User(params)
        return [userInstance: userInstance]
    }

    def save()
    {
        def role = Role.findByAuthority(params.role)
        def userInstance = new User(params)
        
        if (userInstance.save(flush: true))
        {
            UserRole.create(userInstance, role, true)
            flash.message = "${message(code: 'default.created.message', default:'User created', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
            redirect(action: "show", id: userInstance.id)
        }
        else
            render(view: "create", model: [userInstance: userInstance])
    }

    def show()
    {
        def userInstance = User.get(params.id)
        if (!userInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "search")
        }
        else
        {
            [userInstance: userInstance]
        }
    }
    
    def passwordExpired()
    {
        [username: session['SPRING_SECURITY_LAST_USERNAME']]
    }

    def updatePassword()
    {
        String username = session['SPRING_SECURITY_LAST_USERNAME']
        if (!username)
        {
           flash.message = 'Sorry, an error has occurred'
           redirect controller: 'login', action: 'auth'
           return
        }
     
         String password = params.password
         String newPassword = params.password_new
         String newPassword2 = params.password_new_2
     
         if (!password || !newPassword || !newPassword2 || newPassword != newPassword2)
         {
             flash.message = 'Please enter your current password and a valid new password'
             render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
             return
         }
     
         User user = User.findByUsername(username)
         if (!springSecurityService.passwordEncoder.isPasswordValid(user.password, password))
         {
             flash.message = 'Current password is incorrect'
             render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
             return
         }
     
         if (springSecurityService.passwordEncoder.isPasswordValid(user.password, newPassword))
         {
             flash.message = 'Please choose a different password from your current one'
             render view: 'passwordExpired', model: [username: session['SPRING_SECURITY_LAST_USERNAME']]
             return
        }
     
        user.password = newPassword
        user.passwordExpired = false
        user.save() // if you have password constraints check them here
     
        redirect controller: 'login', action: 'auth'
    }

    def edit()
    {
        def userInstance = User.get(params.id)
        if (!userInstance)
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "search")
        }
        else
        {
            return [userInstance: userInstance]
        }
    }

    def update()
    {
        def userInstance = User.get(params.id)
        if (userInstance)
        {
            if (params.version)
            {
                def version = params.version.toLong()
                if (userInstance.version > version) 
                {
                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'user.label', default: 'User')] as Object[], "Another user has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance])
                    return
                }
            }
            
            def passwd = params.password
            if (passwd == null || "".equals(passwd))
                params.remove("password")

            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save(flush: true))
            {
                def role = Role.findByAuthority(params.role)
                if (!role.equals(userInstance.roleAux))
                {
                    UserRole.remove(userInstance, userInstance.roleAux)
                    UserRole.create(userInstance, role, true)
                }

                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
                redirect(action: "show", id: userInstance.id)
            }
            else
            {
                render(view: "edit", model: [userInstance: userInstance])
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "search")
        }
    }

    def delete()
    {
        def userInstance = User.get(params.id)
        if (userInstance)
        {
            try
            {
                UserRole.remove(userInstance, userInstance.roleAux)
                userInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "search")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e)
            {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else
        {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "search")
        }
    }
}
