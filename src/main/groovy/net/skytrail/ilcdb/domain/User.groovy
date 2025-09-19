package net.skytrail.ilcdb.domain;
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

import grails.gorm.Entity;

class User implements Entity {
    def springSecurityService // Dependency injection for the springSecurityService.

    String username
    String password
    String email
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static constraints =
    {
        username blank: false, unique: true
        password blank: false
        email blank: false
    }

    static mapping =
    {
        password column: '`password`'
        table 'database_user'
    }

    static transients = [ "roleDisplayName", "roleAux" ]

    Set<Role> getAuthorities()
    {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }
    
    String getRoleDisplayName()
    {
        return getRoleAux().displayName
    }
    
    Role getRoleAux()
    {
        Set<Role> roles = getAuthorities()
        return roles.iterator().next()
    }

    def beforeInsert()
    {
        encodePassword()
    }

    def beforeUpdate()
    {
        if (isDirty('password'))
            encodePassword()
    }

    /*def getSalt()
    {
        return username+"::ilcdb"
    }*/

    protected void encodePassword()
    {
        password = springSecurityService.encodePassword(password)
    }
}
