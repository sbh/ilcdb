package net.skytrail.ilcdb.service

import net.skytrail.ilcdb.domain.Client
import groovy.json.JsonOutput
import grails.gorm.services.Service

@Service(Client)
abstract class JsonifyService {

    String generateClientJson() {
        def clients = Client.list()
        return JsonOutput.toJson(clients)
    }
}
