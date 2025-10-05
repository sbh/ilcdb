package net.skytrail.ilcdb.domain;

import gorm.transform.Entity

@Entity
class Role {
    public static enum RoleType
    {
        ROLE_ADMIN("Administrator"),
        ROLE_ATTORNEY("Attorney"),
        ROLE_STAFF("Staff"),
        ROLE_VOLUNTEER("Volunteer"),
        ROLE_INTERN("Intern");
        
        private final String displayName;
        
        RoleType(String displayName)
        {
            this.displayName = displayName;
        }
        
        public String getDisplayName()
        {
            return displayName;
        }
        
        public static RoleType lookup(String aRole)
        {
            for (RoleType role : EnumSet.allOf(RoleType.class))
            {
                if (aRole.equalsIgnoreCase(role.toString()))
                    return role;
            }
        }
    }

    String authority
    String displayName

    static mapping =
    {
        cache true
    }

    static constraints =
    {
        authority blank: false, unique: true
        authority(inList:[String.valueOf(RoleType.ROLE_ADMIN),
                          String.valueOf(RoleType.ROLE_ATTORNEY),
                          String.valueOf(RoleType.ROLE_STAFF),
                          String.valueOf(RoleType.ROLE_VOLUNTEER),
                          String.valueOf(RoleType.ROLE_INTERN)])
    }
    
    public String toDebugString()
    {
        return "Role.authority = "+authority
    }
}
