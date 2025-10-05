package net.skytrail.ilcdb.domain;

import gorm.transform.Entity

@Entity
class User {
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

    /*
    def beforeInsert()
    {
        encodePassword()
    }

    def beforeUpdate()
    {
        if (isDirty('password'))
            encodePassword()
    }

    protected void encodePassword()
    {
        password = springSecurityService.encodePassword(password)
    }
    */
}
