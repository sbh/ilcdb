class BootStrap
{
    def springSecurityService
    def dataSource

    def init = { servletContext ->
        // Skip role/user initialization in test environment - tests create their own data
        if (grails.util.Environment.current == grails.util.Environment.TEST) {
            return
        }

        def sql = new groovy.sql.Sql(dataSource)

        // Migration: drop removed intensity column from client_case
        try {
            sql.execute("ALTER TABLE client_case DROP COLUMN intensity")
        } catch (e) {
            // Column already dropped or doesn't exist
        }

        // Migration: populate attorney_id from old attorney string column
        try {
            // Check if old attorney varchar column exists
            def hasOldColumn = sql.firstRow(
                "SELECT COUNT(*) as cnt FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'client_case' " +
                "AND COLUMN_NAME = 'attorney' AND DATA_TYPE = 'varchar'"
            )

            if (hasOldColumn?.cnt > 0) {
                // Map old attorney string values to Attorney IDs
                // Handle Laurel/Melissa -> Laurel, Mary/Melissa -> Mary
                def laurel = Attorney.findByFirstName('Laurel')
                def mary = Attorney.findByFirstName('Mary')
                def unassigned = Attorney.findByFirstName('Unassigned')

                if (laurel) {
                    sql.executeUpdate("UPDATE client_case SET attorney_id = ? WHERE attorney IN ('Laurel', 'Laurel/Melissa')", [laurel.id])
                }
                if (mary) {
                    sql.executeUpdate("UPDATE client_case SET attorney_id = ? WHERE attorney IN ('Mary', 'Mary/Melissa')", [mary.id])
                }

                // Map remaining attorneys by exact name match
                ['Belen', 'Maria', 'Itzel', 'Diego', 'Mairi'].each { name ->
                    def attorney = Attorney.findByFirstName(name)
                    if (attorney) {
                        sql.executeUpdate("UPDATE client_case SET attorney_id = ? WHERE attorney = ?", [attorney.id, name])
                    }
                }

                // Map NULL, -Choose-, ----, and any remaining unmapped to Unassigned
                if (unassigned) {
                    sql.executeUpdate(
                        "UPDATE client_case SET attorney_id = ? WHERE attorney_id IS NULL OR attorney IS NULL OR attorney IN ('-Choose-', '----', '')",
                        [unassigned.id]
                    )
                }

                // Drop old attorney column after successful migration
                sql.execute("ALTER TABLE client_case DROP COLUMN attorney")
            }
        } catch (e) {
            // Migration already done or not applicable
        }

        sql.close()

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

        // Initialize attorneys
        def attorneys = [
            [firstName: "Unassigned", lastName: null, email: null, isActive: false],
            [firstName: "Laurel", lastName: "Herndon", email: "laurel@boulderayuda.org", isActive: true],
            [firstName: "Belen", lastName: "Pargas Solis", email: "belen@boulderayuda.org", isActive: true],
            [firstName: "Maria", lastName: "Gordillo Villa", email: "maria@boulderayuda.org", isActive: true],
            [firstName: "Itzel", lastName: "Cordova - Aguirre", email: "itzel@boulderayuda.org", isActive: false],
            [firstName: "Mary", lastName: "", email: "", isActive: false],
            [firstName: "Laurel/Melissa", lastName: "", email: "", isActive: false],
            [firstName: "Mary/Melissa", lastName: "", email: "", isActive: false],
            [firstName: "Diego", lastName: "", email: "", isActive: false],
            [firstName: "Mairi", lastName: "", email: "", isActive: false]
        ]

        attorneys.each { attrs ->
            if (!Attorney.findByFirstName(attrs.firstName)) {
                new Attorney(
                    firstName: attrs.firstName,
                    lastName: attrs.lastName ?: null,
                    email: attrs.email ?: null,
                    isActive: attrs.isActive
                ).save(flush: true)
            }
        }
    }
     
    def destroy = {
    }
}