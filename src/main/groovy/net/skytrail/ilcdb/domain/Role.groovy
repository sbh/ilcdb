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

class Role implements Entity {
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
