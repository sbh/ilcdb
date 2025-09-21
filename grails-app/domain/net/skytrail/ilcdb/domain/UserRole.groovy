package net.skytrail.ilcdb.domain

import grails.gorm.Entity
import org.hibernate.annotations.Immutable

@Immutable
class UserRole implements Serializable, Entity {

    User user
    Role role

    static mapping = {
        id composite: ['user', 'role']
        version false
    }
}
