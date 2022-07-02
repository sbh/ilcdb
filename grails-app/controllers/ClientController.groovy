import grails.plugin.springsecurity.annotation.Secured
import groovy.json.JsonOutput
import net.skytrail.util.USStates
import org.joda.time.Interval
import java.util.List

@Secured(['IS_AUTHENTICATED_FULLY'])
class ClientController
{
    def clientService

    def usStates = new USStates()

    def index() { redirect(action:"list",params:params) }

    // the delete, save and update actions only accept POST requests
    static def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list()
    {
        long t1 = System.currentTimeMillis()
        List<Client> clients = new ArrayList()

        clients.addAll(Client.findAll( "from Client as c order by upper(c.client.lastName), upper(c.client.firstName)" ))

        def file = new File("/var/tmp/clients.json")
        file.delete()
        def id = 1
        clients.each{
            file << """{"index":{"_index":"clients-2018-01-27","_type":"client","_id":${id++}}"""
            file << "\n"
            file << JsonOutput.toJson(it.toMap())
            file << "\n"
        }

        def t2 = System.currentTimeMillis()
        println(clients.size()+" loaded in "+(t2-t1)+" ms.")

        Collections.sort(clients, new ClientComparator());
        println(clients.size()+" sorted in "+(System.currentTimeMillis()-t2)+" ms.")

        def clientMaps = []
        t1 = System.currentTimeMillis()
        for (client in clients)
        {
            def clientMap = new HashMap()
            clientMap['id'] = client.id
            clientMap['person'] = client.client?.encodeAsHTML()
            clientMap['phoneNumber'] = client.client?.phoneNumber?.encodeAsHTML()
            clientMap['householeIncomeLevel'] = client.householdIncomeLevel
            clientMap['numberInHousehold'] = client.numberInHousehold
            clientMap['age'] = client.client?.age
            clientMap['race'] = client.client?.race?.encodeAsHTML()
            clientMap['homeCountry'] = client.homeCountry?.encodeAsHTML()
            clientMap['shortAddress'] = client.shortAddress?.encodeAsHTML()
            clientMap['fileLocation'] = client.fileLocation
            clientMap['attorney'] = client.attorney
            clientMap['validCases'] = (client.validCases ? "" : "**")
            clientMaps.add(clientMap)
        }
        println(clients.size()+" resolved "+(System.currentTimeMillis()-t1)+" ms.")

        [ clientList:  clientMaps ]
    }

    private class ClientComparator implements Comparator<Client>
    {
        int compare(Client c1, Client c2)
        {
            int val = c1.client.getSortableLastName().compareTo(c2.client.getSortableLastName())
            if (val == 0)
            {
                val = c1.client.getSortableFirstName().compareTo(c2.client.getSortableFirstName())
                if (val != 0)
                    return val;
                return c1.client.lastName.toUpperCase().compareTo(c2.client.lastName.toUpperCase())
            }
            return val
        }
    }

    def show()
    {
        //println("ClientController.show params: "+params)
        def client = Client.get( params.id )

        if(!client)
        {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"list")
        }
        else
            return [ client : client ]
    }

    def delete()
    {
        //println("ClientController.delete params: "+params)
        def client = Client.get( params.id )
        if(client)
        {
            client.delete()
            flash.message = "Client ${params.id} deleted"
            redirect(action:"list")
        }
        else
        {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"list")
        }
    }

    def edit()
    {
        //println("ClientController.edit params: "+params)
        def client = Client.get( params.id )

        if(!client)
        {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"list")
        }
        else
        {
            return [client: client]
        }
    }

    def update()
    {
        //println("ClientController.update params: "+params)

        def client = Client.get( params.id )
        if(client)
        {
            def addressCountry = Country.get(params.client.address.country)

            def birthCountry = Country.get(params.client.placeOfBirth.country)

            client.properties = params
            client.ami = AMI.get(params.amiId)

            client.client.address.country = addressCountry

            client.client.placeOfBirth.country = birthCountry
            client.clearErrors()
            if(client.validate() && client.save())
            {
                flash.message = "Client ${params.id} updated"
                redirect(action:"list", fragment:params.id)
            }
            else
            {
                println("errors***")
                client.errors.allErrors.each { println it }
                println("***errors")

                render(view:'edit',model:[client:client])
            }
        }
        else
        {
            flash.message = "Client not found with id ${params.id}"
            redirect(action:"edit", id:params.id)
        }
    }

    def create()
    {
        def client = new Client()
        client.properties = params

        return ['client':client]
    }

    def save()
    {
        //println("ClientController.save params: "+params)

        if(params.containsKey('personSource') && params['personSource'] == 'new')
        {
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
            placeOfBirth.validate())
            {
                address.save()
                placeOfBirth.save()
                person.save()
                client.client = person
                client.save()

                flash.message = "Client ${client.id} created"
                // redirect to the newly created intake so that it can be edited if so desired
                redirect(controller:"clientCase", action:"edit", id:clientCase.id)
            }
            else
            {
                //Restore object graph to report errors in the view
                if (!client.validate())
                    client.errors.allErrors.each { println "client error: "+it }
                if (!address.validate())
                    address.errors.allErrors.each { println "address error: "+it }
                if (!person.validate())
                    person.errors.allErrors.each { println "person error: "+it }
                if (!placeOfBirth.validate())
                    placeOfBirth.errors.allErrors.each { println "placeOfBirth error: "+it }
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
            client.validate())
            {
                placeOfBirth.save()
                client.save()

                flash.message = "Client ${client.id} created"
                redirect(action:"list")
            }
            else
                render(view:'create', model:[client:client])
        }
        else
        {
            /*The user is passing custom parameters. Something bad is going on!
             *That or someone is modifying the application, in which case they
             *should see this. Consider logging the user out and noting an error?
             */
        }
    }

    def search()
    {
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

        String query;
        if (params.q == null || params.q.size() == 0)
        {
            if (params.dateRestricted)
                searchResults.addAll(Client.list())
        }
        else
        {
            if (params.q.startsWith("city:"))
            {
                query = '''
                        FROM Client AS client 
                        INNER JOIN FETCH client.client AS person 
                        INNER JOIN FETCH person.address AS address 
                        WHERE LOWER(address.city) LIKE ?
                          ORDER BY person.lastName
                        '''
                String city = params.q.replace("city:", "")
                searchResults.addAll(Client.executeQuery( query, [city]))
            }
            if (params.q.startsWith("county:"))
            {
                query = '''
                          FROM Client AS client 
                          INNER JOIN FETCH client.client AS person 
                          INNER JOIN FETCH person.address AS address 
                          WHERE LOWER(address.county) LIKE ?
                            ORDER BY person.lastName
                        '''
                String county = params.q.replace("county:", "")
                searchResults.addAll(Client.executeQuery( query, [county]))
            }
            else
            {
                query = '''
                          FROM Client AS client 
                          INNER JOIN FETCH client.client AS person 
                          INNER JOIN FETCH person.address AS address 
                          WHERE LOWER(address.street) LIKE ? 
                          OR LOWER(address.city) LIKE ? 
                          OR LOWER(address.county) LIKE ? 
                          OR LOWER(address.state) LIKE ? 
                          OR LOWER(address.country.name) LIKE ? 
                          OR LOWER(person.firstName) LIKE ? 
                          OR LOWER(person.lastName) LIKE ?
                          OR LOWER(person.phoneNumber) LIKE ?
                            ORDER BY person.lastName
                        '''
                params.q.split(/\s/).each
                { token ->
                    token = "%${token}%".toLowerCase()
                    searchResults.addAll(Client.executeQuery( query, [token, token, token, token, token, token, token, token ]))
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
            try
            {
                def serviceRecords = ServiceRecord.findAllByServiceDateBetween(params.serviceRecordStartDate, endDate)
                def clientIds = []
                def serviceHours = 0
                for (ServiceRecord serviceRecord : serviceRecords)
                {
                    clientIds += serviceRecord.clientId
                    if (params.serviceRecordStartDate?.compareTo(serviceRecord.serviceDate) <= 0 &&
                    serviceRecord.serviceDate.compareTo(endDate) <= 0)
                        serviceHours += serviceRecord.serviceHours
                }

                clientIds = clientIds.unique()
                def dateRestrictedSearchResults = []
                for (String clientId : clientIds)
                {
                    for (Client client : searchResults)
                    {
                        if (clientId.equals(client.clientId))
                        {
                            dateRestrictedSearchResults.add(client)
                            continue
                        }
                    }
                }
                searchResults.clear()
                searchResults.addAll(dateRestrictedSearchResults)
                params.serviceHours = serviceHours
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        params.count = searchResults.size()

        //Paginate results
        if (params.max < searchResults.size())
        {
            def tempResults = []

            (params.offset ..< (params.offset + params.max)).each
            {
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

    @Secured(['ROLE_ADMIN', "authentication.name == 'laurel'"])
    def report()
    {
        println("params: ${params}")

        //println("**** report params: "+params)
        def returnValue = [ : ];

        if(params.startDate && params.endDate)
        {
            // adjust the endDate to be the end of the day.
            params.endDate = new Date(params.endDate.getTime() + 1000L*24L*60L*60L - 1L)

            String qry = null
            if (params.intakeState == "opened") {
                qry = CLIENTS_QUERY + " where " + OPENED_INTAKES_QUERY
            } else if (params.intakeState == "closed") {
                qry = CLIENTS_QUERY + " where " + COMPLETED_INTAKES_QUERY
            } else {
                qry = CLIENTS_QUERY + " where (" + OPENED_INTAKES_QUERY + " or " + STILL_OPEN_INTAKES_QUERY + ")"
            }

            def unfilteredClients = getClients(qry, params)

            println("${unfilteredClients.size()} clients found in requested time interval.")

            def clients = clientService.filterStatus(unfilteredClients, params.statusAchieved, params.intakeState, new Interval(params.startDate.getTime(), params.endDate.getTime()))
            def sortedClients = new ArrayList(clients)
            Collections.sort(sortedClients, new ClientComparator());

            returnValue["startDate"] = params.startDate
            returnValue["endDate"] = params.endDate
            returnValue["municipality"] = params.municipality;
            returnValue["munType"] = params.munType
            returnValue["attorney"] = params.attorney
            returnValue["displayIntakesCheckBox"] = params.displayIntakesCheckBox == "on" || params.displayIntakesCheckBox == "true"? "true" : "false"
            returnValue["intakeState"] = params.intakeState
            returnValue["statusAchieved"] = params.statusAchieved
            returnValue["homeCountry"] = params.homeCountry

            returnValue["report"] = true;
            returnValue["Clients"] = sortedClients;
        }

        //println("**** report returnValue: " + returnValue)

        return returnValue;
    }

    private static String CLIENTS_QUERY = """
            select distinct client from
             Client as client
               inner join fetch client.client as person
               inner join fetch person.address as address
               inner join fetch person.placeOfBirth as placeOfBirth
               inner join fetch client.cases as intake
        """
    private static String COMPLETED_INTAKES_QUERY = " ( intake.completionDate >= :startDate and intake.completionDate <= :endDate ) "
    private static String OPENED_INTAKES_QUERY = " ( intake.startDate >= :startDate and intake.startDate <= :endDate )"
    private static String STILL_OPEN_INTAKES_QUERY = " ( intake.completionDate is NULL or intake.completionDate >= :endDate ) "

    Collection<Client> getClients(String clientIntakeQuery, def params) {
        def queries = [getMunicipalitySubQuery(params.munType), getAttorneySubQuery(params.attorney), getHomeCountrySubQuery(params.homeCountry)]

        String query = clientIntakeQuery
        queries.each{ aQuery ->
            if (aQuery != "") {
                String joiner = null
                if (query.contains("where")) joiner = " and "
                else joiner = " where "

                query = query + joiner + aQuery
            }
        }

        doit( query, params )
    }

    Collection<Client> doit( String query, def params) {
        println("params2: " + params)
        def namedParams = [startDate:params.startDate, endDate:params.endDate]
        if (query.contains(":mun")) namedParams += [mun:params.municipality]
        if (query.contains(":homeCountry")) namedParams += [homeCountry:params.homeCountry]
        if (query.contains(":attorney")) namedParams += [attorney:params.attorney]

        if ("State".equals(params.munType))
            namedParams += [munAlt:usStates.getAlternates(params.municipality)]

        println "------> executing query: $query, namedParams: $namedParams"
        def clients = Client.executeQuery( query, namedParams )
        println "\t results count: ${clients.size()}"
        clients
    }

    String getAttorneySubQuery(String attorney)
    {
        if ("Any".equals(attorney))
            return ""
        return "intake.attorney = '" + attorney + "'"
    }

    String getMunicipalitySubQuery(String municipalityType)
    {
        if ("State".equals(municipalityType))
            return "(upper(address.state) = upper(:mun) or upper(address.state) = upper(:munAlt))"
        else if ("Any".equals(municipalityType))
            return ""
        else
            return "upper(address.${municipalityType.toLowerCase()}) = upper(:mun)"
    }

    String getHomeCountrySubQuery(String homeCountry)
    {
        if (homeCountry == "-1") {
            return ""
        }
        else
            return "(upper(placeOfBirth.country.name) = '${Country.get(homeCountry)}')"
    }
}
