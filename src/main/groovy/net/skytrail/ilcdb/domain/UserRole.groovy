package net.skytrail.ilcdb.domain

import gorm.transform.Entity

import org.hibernate.annotations.Immutable

@Immutable
@Entity
class UserRole implements Serializable {

    User user
    Role role

    static mapping = {
        id composite: ['user', 'role']
        version false
    }
}
