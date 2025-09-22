package net.skytrail.ilcdb.domain;
import gorm.transform.Entity


@Entity
class ClientSponsorRelation @Entity {

    static mapping = {
        cache true
    }

    static belongsTo = [ client:Client, sponsor:Sponsor ]
}
