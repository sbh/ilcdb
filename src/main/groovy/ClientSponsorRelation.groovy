import grails.gorm.Entity;

class ClientSponsorRelation implements Entity {

    static mapping = {
        cache true
    }
    
    static belongsTo = [ client:Client, sponsor:Sponsor ]
}
