class BootStrap
{
    def springSecurityService
    def dataSource
    
    def init = { servletContext ->
        Address.get(-1)
        BirthPlace.get(-1)
        
        for (Role.RoleType roleType : Role.RoleType.values())
        {
            def role = Role.findByAuthority(String.valueOf(roleType))
            if (role == null)
                new Role(authority: String.valueOf(roleType), displayName: roleType.displayName).save(flush: true)
        }

        def adminRole = Role.findByAuthority("ROLE_ADMIN")
        def adminUser = User.findByUsername("admin")

        if (adminUser == null)
        {
            adminUser = new User(username: "admin", enabled: true, password: "admin", passwordExpired: true, email:"admin@boulderayuda.org")
            if (adminUser.save(flush: true))
                UserRole.create(adminUser, adminRole, true)
            else
            {
                adminUser.errors.each { println it }
                System.exit(-1);
            }
        }
    }
     
    def destroy = {
    }
}