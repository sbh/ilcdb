class ClientSponsorRelation {

    static mapping = {
        cache true
    }
    
    static belongsTo = [ client:Client, sponsor:Sponsor ]
}
