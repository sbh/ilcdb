import net.skytrail.ilcdb.domain.User
import net.skytrail.ilcdb.domain.Role
import net.skytrail.ilcdb.domain.UserRole

// Using class literals instead of strings can resolve startup order issues
grails.plugin.springsecurity.userLookup.userDomainClassName = User.class
grails.plugin.springsecurity.userLookup.authorityJoinClassName = UserRole.class
grails.plugin.springsecurity.authority.className = Role.class
