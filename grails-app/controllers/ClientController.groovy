import grails.plugin.cache.Cacheable
import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonOutput
import net.skytrail.util.USStates
import org.joda.time.Interval
import java.util.List
import groovyx.gpars.GParsPool

@Secured(['IS_AUTHENTICATED_FULLY'])
class ClientController {
    def clientService

    def usStates = new USStates()

    def index() { redirect(action:"search",params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def makeClientMap(Client client) {
        return ['id' :  client.id] +
            ['person' : client.client?.encodeAsHTML()] +
            ['phoneNumber' : client.client?.phoneNumber?.encodeAsHTML()] +
            ['householeIncomeLevel' : client.householdIncomeLevel] +
            ['numberInHousehold' : client.numberInHousehold] +
            ['age' : client.client?.age] +
            ['race' : client.client?.race?.encodeAsHTML()] +
            ['homeCountry' : client.homeCountry?.encodeAsHTML()] +
            ['shortAddress': client.shortAddress?.encodeAsHTML()] +
            ['fileLocation' : client.fileLocation] +
            ['attorney' : client.attorney] +
            ['validCases' : (client.validCases ? "" : "**")] +
            ['intakes' : client.intakes]

    }

    def makeClientMaps(List<Client> clients) {
        return clients.collect{client -> makeClientMap(client)}
    }

    def dumpJSON(clients) {
        long t1 = System.currentTimeMillis()
        def file = new File("/var/tmp/clients.json")
        file.delete()
        def id = 1
        clients.each {
            file << JsonOutput.toJson(it.toMap())
            file << "\n"
        }

        def t2 = System.currentTimeMillis()
        println(clients.size()+" loaded in "+(t2-t1)+" ms.")

        Collections.sort(clients, new ClientComparator());
        println(clients.size()+" sorted in "+(System.currentTimeMillis()-t2)+" ms.")

        t1 = System.currentTimeMillis()
    }

    def list() {
        def clientCount = Client.count()
        def query = """
            SELECT c
            FROM Client c
            JOIN c.client p
            ORDER BY LOWER(p.lastName), LOWER(p.firstName)
        """
        def clients = Client.executeQuery(query)
        def clientMaps = makeClientMaps(clients)
        //dumpJSON(clients)
        [clientList: clientMaps, clientCount: clientCount]
    }

    private class ClientComparator implements Comparator<Client> {
        int compare(Client c1, Client c2) {
            int val = c1.client.getSortableLastName().compareToIgnoreCase(c2.client.getSortableLastName())
            if (val == 0) {
                val = c1.client.getSortableFirstName().compareToIgnoreCase(c2.client.getSortableFirstName())
                if (val != 0)
                    return val;
                return c1.client.lastName.compareToIgnoreCase(c2.client.lastName)
            }
            return val
        }
    }

    def show() {
        //println("ClientController.show params: "+params)
        def client = Client.get( params.id )

        if(!client) {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"search")
        }
        else
            return [ client : client ]
    }

    def delete() {
        //println("ClientController.delete params: "+params)
        def client = Client.get( params.id )
        if(client) {
            client.delete()
            flash.message = "Client ${params.id} deleted"
            redirect(action:"search")
        }
        else {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"search")
        }
    }

    def edit() {
        //println("ClientController.edit params: "+params)
        def client = Client.get( params.id )

        if(!client) {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"search")
        }
        else {
            return [client: client]
        }
    }

    def update() {
        //println("ClientController.update params: "+params)

        def client = Client.get( params.id )
        if(client) {
            def addressCountry = Country.get(params.client.address.country)

            def birthCountry = Country.get(params.client.placeOfBirth.country)

            client.properties = params
            client.ami = AMI.get(params.amiId)

            client.client.address.country = addressCountry

            client.client.placeOfBirth.country = birthCountry
            client.clearErrors()
            if(client.validate() && client.save()) {
                flash.message = "Client ${params.id} updated"
                redirect(action:"search", fragment:params.id)
            }
            else {
                render(view:'edit',model:[client:client])
            }
        }
        else {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create() {
        def client = new Client()
        client.properties = params

        return ['client':client]
    }

    def save() {
        //println("ClientController.save params: "+params)

        if(params.containsKey('personSource') && params['personSource'] == 'new') {
            //For generating a new person
            def client = new Client(params)
            def person = new Person(params.client)
            client.ami = AMI.get(params.amiId)

            person.dateOfBirth = params.client.dateOfBirth
            //println("person: "+person.toDebugString())

            def addressCountry = Country.get(params.client.address.country)
            params.client.address.country = addressCountry
            def address = new Address(params.client.address)

            def birthCountry = Country.get(params.client.placeOfBirth.country)
            params.client.placeOfBirth.country = birthCountry
            def placeOfBirth = new BirthPlace(params.client.placeOfBirth)

            if (address.validate())
                person.address = address

            if (placeOfBirth.validate())
                person.placeOfBirth = placeOfBirth

            if(person.validate())
                client.client = person

            def clientCase = new ClientCase()
            Date now = new Date()
            clientCase.startDate = now
            clientCase.completionDate = now
            clientCase.intakeType = ClientCase.STAFF_ADVISE
            client.addToCases(clientCase)
            client.clearErrors()

            //println("\nclient.validate(): "+client.validate()+
            //        ", address.validate(): "+address.validate()+", person.validate()"+person.validate()+
            //        ", placeOfBirth.validate(): "+placeOfBirth.validate())

            //Ensure the entire graph is valid before saving anything
            if (client.validate() &&
            address.validate() && person.validate() &&
            placeOfBirth.validate()) {
                address.save()
                placeOfBirth.save()
                person.save()
                client.client = person
                client.save()

                flash.message = "Client ${client.id} created"
                // redirect to the newly created intake so that it can be edited if so desired
                redirect(controller:"clientCase", action:"edit", id:clientCase.id)
            }
            else {
                //Restore object graph to report errors in the view
                person.address = address
                client.client = person
                render(view:'create', model:[client:client])
            }
        }
        else if(params.containsKey('personSource') && params['personSource'] == 'existing')
        {
            //Default save controller created by generate-controller

            def client = new Client(params)
            def placeOfBirth = new BirthPlace(params.placeOfBirth)

            if (!placeOfBirth.hasErrors() && placeOfBirth.validate())
                person.placeOfBirth = placeOfBirth

            if (!client.hasErrors() && placeOfBirth.validate() &&
            client.validate()) {
                placeOfBirth.save()
                client.save()

                flash.message = "Client ${client.id} created"
                redirect(action:"search")
            }
            else
                render(view:'create', model:[client:client])
        }
        else {
            /*The user is passing custom parameters. Something bad is going on!
             *That or someone is modifying the application, in which case they
             *should see this. Consider logging the user out and noting an error?
             */
        }
    }

    def search() {
        //println("ClientController.search params: "+params)

        if (!params.max || ("all".equalsIgnoreCase(params.max)))
            params.max = Integer.MAX_VALUE
        else
            params.max = params.max.toInteger()

        if (!params.offset)
            params.offset = 0
        else
            params.offset = params.offset.toInteger()

        def searchResults = new HashSet()

        if (params.q == null || params.q.size() == 0) {
            if (params.dateRestricted)
                searchResults.addAll(Client.list())
        }
        else {
            String paramsString = params.q.toLowerCase().trim()
            if (paramsString.startsWith("city:")) {
                String query = '''
                                 FROM Client AS client
                                 INNER JOIN FETCH client.client AS person
                                 INNER JOIN FETCH person.address AS address
                                 WHERE LOWER(address.city) LIKE ?
                                 ORDER BY person.lastName
                        '''
                String token = "%" + paramsString.replace("city:", "").trim() + "%"
                //println("Executing query: "+query+", token: "+token)
                searchResults.addAll(Client.executeQuery( query, [token]))
            }
            else if (paramsString.startsWith("county:")) {
                String query = '''
                                 FROM Client AS client
                                  INNER JOIN FETCH client.client AS person
                                  INNER JOIN FETCH person.address AS address
                                  WHERE LOWER(address.county) LIKE ?
                                  ORDER BY person.lastName
                        '''
                String token = "%" + paramsString.replace("county:", "").trim() + "%"
                //println("Executing query: "+query+", token: "+token)
                searchResults.addAll(Client.executeQuery( query, [token]))
            }
            else if (paramsString.startsWith("state:")) {
                String query = '''
                                 FROM Client AS client
                                 INNER JOIN FETCH client.client AS person
                                 INNER JOIN FETCH person.address AS address
                                 WHERE LOWER(address.state) LIKE ?
                                 ORDER BY person.lastName
                        '''
                String token = "%" + paramsString.replace("state:", "").trim() + "%"
                //println("Executing query: "+query+", token: "+token)
                searchResults.addAll(Client.executeQuery( query, [token]))
            }
            else if (paramsString.startsWith("birth country:")) {
                String query = '''
                                FROM Client AS client
                                INNER JOIN FETCH client.client AS person
                                INNER JOIN FETCH person.placeOfBirth AS birthPlace
                                WHERE LOWER(birthPlace.country.name) LIKE ?
                                ORDER BY person.lastName
                        '''
                String token = "%" + paramsString.replace("birth country:", "").trim() + "%"
                //println("Executing query: "+query+", token: "+token)
                searchResults.addAll(Client.executeQuery(query, [token]))
            }
            else {
                String query = '''
                                FROM Client AS client
                                INNER JOIN FETCH client.client AS person
                                INNER JOIN FETCH person.address AS address
                                JOIN FETCH person.placeOfBirth AS birthPlace
                                WHERE LOWER(address.street) LIKE ?
                                   OR LOWER(address.city) LIKE ?
                                   OR LOWER(address.county) LIKE ?
                                   OR LOWER(address.state) LIKE ?
                                   OR LOWER(address.country.name) LIKE ?
                                   OR LOWER(birthPlace.country.name) LIKE ?
                                   OR LOWER(person.firstName) LIKE ?
                                   OR LOWER(person.lastName) LIKE ?
                                   OR LOWER(person.phoneNumber) LIKE ?
                                ORDER BY person.lastName
                        '''
                paramsString.split(/\s/).each
                { token ->
                    token = "%${token.trim()}%"
                    //println("Executing query: "+query+", token: "+token)
                    searchResults.addAll(Client.executeQuery(query, [token, token, token, token, token, token, token, token, token]))
                    searchResults.addAll(Client.createCriteria().list { notes { ilike ("text", "%"+token+"%") } })
                }
            }
        }

        if (params.dateRestricted)
        {
            // Alter the end date to the end of the month (or beginning of the next month)
            Calendar cal = Calendar.getInstance()
            cal.setTime(params.serviceRecordEndDate)
            cal.add(Calendar.MONTH, 1)
            Date endDate = cal.getTime()

            params.count = 0
            params.serviceHours = 0
            try {
                def serviceRecords = ServiceRecord.findAllByServiceDateBetween(params.serviceRecordStartDate, endDate)
                def clientIds = []
                def serviceHours = 0
                for (ServiceRecord serviceRecord : serviceRecords) {
                    clientIds += serviceRecord.clientId
                    if (params.serviceRecordStartDate?.compareTo(serviceRecord.serviceDate) <= 0 &&
                        serviceRecord.serviceDate.compareTo(endDate) <= 0)
                        serviceHours += serviceRecord.serviceHours
                }

                clientIds = clientIds.unique()
                def dateRestrictedSearchResults = []
                for (String clientId : clientIds) {
                    for (Client client : searchResults) {
                        if (clientId.equals(client.clientId)) {
                            dateRestrictedSearchResults.add(client)
                            continue
                        }
                    }
                }
                searchResults.clear()
                searchResults.addAll(dateRestrictedSearchResults)
                params.serviceHours = serviceHours
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        params.count = searchResults.size()

        //Paginate results
        if (params.max < searchResults.size()) {
            def tempResults = []

            (params.offset ..< (params.offset + params.max)).each {
                if(it < searchResults?.size())
                    tempResults += searchResults[it]
            }
            searchResults.clear()
            searchResults.addAll(tempResults)
        }
        if (searchResults.size() < 1)
            flash.message = 'No results found'

        def sortedResults = new ArrayList(searchResults)
        Collections.sort(sortedResults, new ClientComparator());

        return [ searchResults:sortedResults, params:params ]
    }

    def report() {
        // Don't generate a report if the user is just navigating to the page
        if (!params._action_report) {
            return [:]
        }

        // Manually construct dates to avoid i18n issues with params.date()
        def startDay = params.int('startDate_day')
        def startMonth = params.int('startDate_month') - 1 // Calendar is 0-based for months
        def startYear = params.int('startDate_year')
        Date startDate = (startDay && startMonth != null && startYear) ? new GregorianCalendar(startYear, startMonth, startDay).time : null

        def endDay = params.int('endDate_day')
        def endMonth = params.int('endDate_month') - 1 // Calendar is 0-based for months
        def endYear = params.int('endDate_year')
        Date endDate = (endDay && endMonth != null && endYear) ? new GregorianCalendar(endYear, endMonth, endDay).time : null
        def municipality = params.municipality
        def munType = params.munType ?: "Any"
        def attorney = params.attorney ?: "Any"
        def displayIntakesCheckBox = params.displayIntakesCheckBox
        def intakeState = params.intakeState ?: "any"
        def intakeType = params.intakeType ?: "any"
        def statusAchieved = params.statusAchieved ?: "any"
        def homeCountry = params.homeCountry

        return _report(startDate, endDate, municipality, munType, attorney, displayIntakesCheckBox, intakeState, intakeType, statusAchieved, homeCountry)
    }

    @Cacheable(value = 'reportCache', key = "(#startDate?.format('yyyy-MM-dd') ?: '') + (#endDate?.format('yyyy-MM-dd') ?: '') + (#municipality ?: '') + (#munType ?: '') + (#attorney ?: '') + (#displayIntakesCheckBox ?: '') + (#intakeState ?: '') + (#intakeType ?: '') + (#statusAchieved ?: '') + (#homeCountry ?: '')")
    private def _report(Date startDate, Date endDate, String municipality, String munType, String attorney,
                        String displayIntakesCheckBox, String intakeState, String intakeType, String statusAchieved, String homeCountry) {
        def returnValue = [:]
        if (startDate && endDate) {
            endDate = new Date(endDate.getTime() + 1000L * 24L * 60L * 60L - 1L)
            
            def unfilteredClients = getClients(CLIENTS_QUERY + "WHERE ( " + COMBINED_INTAKES_QUERY + " )",
                    munType, attorney, homeCountry, startDate, endDate, municipality, statusAchieved, intakeState, intakeType)
            def interval = new Interval(startDate.getTime(), endDate.getTime())
            def clients
            GParsPool.withPool {
                clients = unfilteredClients.collate(100).parallel.map { chunk ->
                    clientService.filterStatus(chunk, statusAchieved, intakeState, intakeType, interval)
                }.collection.flatten()
            }
            def sortedClients = new ArrayList(clients)
            Collections.sort(sortedClients, new ClientComparator())
            def intakeTypeCounts = clientService.intakeTypeCounts(unfilteredClients, interval)
            returnValue = [
                    startDate            : startDate,
                    endDate              : endDate,
                    municipality         : municipality,
                    munType              : munType,
                    attorney             : attorney,
                    displayIntakesCheckBox: displayIntakesCheckBox == "on" || displayIntakesCheckBox == "true" ? "true" : "false",
                    intakeState          : intakeState,
                    intakeType           : intakeType,
                    statusAchieved       : statusAchieved,
                    homeCountry          : homeCountry,
                    report               : true,
                    clientList           : makeClientMaps(sortedClients),
                    saIntakesCount       : intakeTypeCounts[0],
                    srIntakesCount       : intakeTypeCounts[1]
            ]
        }
        return returnValue
    }

    /**
     * REST API endpoint for report comparison testing
     * Returns JSON with client IDs and intake details for easy comparison between branches
     *
     * Example: http://localhost:8080/client/reportApi?startDate=2023-01-01&endDate=2023-12-31
     * Optional params: municipality, munType, attorney, homeCountry, statusAchieved, intakeState, intakeType
     */
    def reportApi() {
        // Parse dates from simple format: yyyy-MM-dd
        def dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd")
        Date startDate = params.startDate ? dateFormat.parse(params.startDate) : null
        Date endDate = params.endDate ? dateFormat.parse(params.endDate) : null

        if (!startDate || !endDate) {
            render(contentType: "application/json") {
                error = "Missing required parameters: startDate and endDate (format: yyyy-MM-dd)"
                example = "/client/reportApi?startDate=2023-01-01&endDate=2023-12-31"
            }
            return
        }

        def municipality = params.municipality
        def munType = params.munType ?: "Any"
        def attorney = params.attorney ?: "Any"
        def intakeState = params.intakeState ?: "any"
        def intakeType = params.intakeType ?: "any"
        def statusAchieved = params.statusAchieved ?: "any"
        def homeCountry = params.homeCountry

        // Run the report
        def reportData = _report(startDate, endDate, municipality, munType, attorney, null, intakeState, intakeType, statusAchieved, homeCountry)

        // Extract just the client IDs and intake details for comparison
        def clients = reportData.clientList ?: []
        def result = [
            metadata: [
                startDate: params.startDate,
                endDate: params.endDate,
                municipality: municipality,
                munType: munType,
                attorney: attorney,
                statusAchieved: statusAchieved,
                intakeState: intakeState,
                intakeType: intakeType,
                homeCountry: homeCountry,
                totalClients: clients.size(),
                saIntakesCount: reportData.saIntakesCount ?: 0,
                srIntakesCount: reportData.srIntakesCount ?: 0
            ],
            clients: clients.collect { client ->
                [
                    id: client.id,
                    name: client.person,
                    attorney: client.attorney,
                    city: client.shortAddress,
                    homeCountry: client.homeCountry,
                    intakeCount: client.intakes?.size() ?: 0
                ]
            }.sort { it.id }
        ]

        render(contentType: "application/json", text: groovy.json.JsonOutput.toJson(result))
    }

    private static String CLIENTS_QUERY = """
            select distinct client from
             Client as client
               inner join fetch client.client as person
               inner join fetch person.address as address
               inner join fetch person.placeOfBirth as placeOfBirth
               inner join fetch client.cases as intake
        """
    // Find intakes that were completed, opened, or ongoing during the date range
    private static String COMPLETED_INTAKES_QUERY = " ( intake.completionDate >= :startDate AND intake.completionDate <= :endDate ) "
    private static String OPENED_INTAKES_QUERY = " ( intake.startDate >= :startDate AND intake.startDate <= :endDate )"
    private static String ONGOING_INTAKES_QUERY = " ( intake.startDate <= :endDate AND (intake.completionDate is NULL OR intake.completionDate >= :endDate) ) "
    public static String COMBINED_INTAKES_QUERY = COMPLETED_INTAKES_QUERY + " OR " + OPENED_INTAKES_QUERY + " OR " + ONGOING_INTAKES_QUERY
    Collection<Client> getClients(String clientIntakeQuery,
                                  String munType, String attorney, String homeCountry,
                                  Date startDate, Date endDate, String municipality, String statusAchieved, String intakeState, String intakeType) {
        def queries = [getMunicipalitySubQuery(munType, municipality), getAttorneySubQuery(attorney), getHomeCountrySubQuery(homeCountry)]

        String query = clientIntakeQuery
        queries.each{ aQuery ->
            if (aQuery != "") {
                String joiner = null
                if (query.toLowerCase().contains("where")) joiner = " and "
                else joiner = " where "

                query = query + joiner + aQuery
            }
        }

        doit( query, munType, startDate, endDate, municipality, homeCountry, attorney )
    }

    Collection<Client> doit( String query, String munType, Date startDate, Date endDate, String municipality, String homeCountry, String attorney) {
        def namedParams = [startDate:startDate, endDate:endDate]

        if (attorney && attorney != "Any") {
            namedParams += [attorney:attorney]
        }
        if (homeCountry && homeCountry != "-1") {
            namedParams += [homeCountryName:Country.get(homeCountry).name]
        }
        if (munType && "Any" != munType && municipality?.trim()) {
            namedParams += [mun:municipality]
            if ("State".equals(munType)) {
                namedParams += [munAlt:usStates.getAlternates(municipality)]
            }
        }

        def clients = Client.executeQuery( query, namedParams )
        clients
    }

    String getAttorneySubQuery(String attorney) {
        if (!attorney || "Any".equals(attorney)) {
            return ""
        }
        return "intake.attorney.firstName = :attorney"
    }

    String getMunicipalitySubQuery(String municipalityType, String municipality) {
        if (!municipalityType || "Any".equals(municipalityType) || !municipality?.trim()) {
            return ""
        }
        if ("State".equals(municipalityType))
            return "(upper(address.state) = upper(:mun) or upper(address.state) = upper(:munAlt))"
        else
            return "upper(address.${municipalityType.toLowerCase()}) = upper(:mun)"
    }

    String getHomeCountrySubQuery(String homeCountry) {
        if (!homeCountry || homeCountry == "-1") {
            return ""
        }
        else {
            return "(upper(placeOfBirth.country.name) = :homeCountryName)"
        }
    }
}
