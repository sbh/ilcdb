import java.sql.ClientInfoStatus;

import grails.plugin.springsecurity.annotation.Secured
import java.util.Calendar;
import java.util.Date;
import net.skytrail.util.USStates

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
        List clients = new ArrayList()

        clients.addAll(Client.findAll( "from Client as c order by upper(c.client.lastName), upper(c.client.firstName)" ))
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

    private class ClientReportElementComparator implements Comparator<ClientReportElement>
    {
        int compare(ClientReportElement c1, ClientReportElement c2)
        {
            int val = c1.client.person.getSortableLastName().compareTo(c2.client.person.getSortableLastName())
            if (val == 0)
            {
                val = c1.client.person.getSortableFirstName().compareTo(c2.client.person.getSortableFirstName())
                if (val != 0)
                    return val;
                return c1.client.person.lastName.toUpperCase().compareTo(c2.client.person.lastName.toUpperCase())
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
            return [ client : client ]
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

    private List findSuccessfullIntakes( List intakes )
    {
        def returnValue = [ ]

        intakes.each
        { intake ->

            if( intake.caseResult in CaseResult.successfulResults())
            {
                returnValue.add(intake);
            }
        }

        return returnValue;
    }

    private List getIntakeBreakdownByCaseType( List intakes )
    {
        def counts = [ : ]

        intakes.each
        { intake ->
            if(!counts[intake.caseType])
            {
                counts[intake.caseType] = 1
            }
            else
            {
                counts[intake.caseType] = counts[intake.caseType] + 1;
            }
        }

        def returnValue = [ ]
        counts.keySet().each
        { type ->
            returnValue.add([type, counts[type]])
        }

        return returnValue;
    }

    private LinkedHashMap getIntakeBreakdownByAttorney( Date startDate, Date endDate )
    {
        def returnValue = [ : ]
        //get case data
        ClientCase.ATTORNEYS.each
        { attorney ->
            if(attorney != "-Choose-")
            {
                def attorneyData = [ : ]

                def ongoingCriteria = ClientCase.createCriteria()
                def ongoingCases = ongoingCriteria.list() {
                    eq("attorney", attorney)
                    eq("intakeType", ClientCase.STAFF_REPRESENTATION)
                    isNull("completionDate")
                }
                //println "Num ongoing cases for " + attorney + ": " + ongoingCases.size()

                def closedCriteria = ClientCase.createCriteria()
                def closedCases = closedCriteria.list() {
                    eq("attorney", attorney)
                    eq("intakeType", ClientCase.STAFF_REPRESENTATION)
                    between("completionDate", startDate, endDate)
                }
                //println "Num closed cases: " + closedCases.size()

                attorneyData["Closed Intakes"] = getIntakeBreakdownByCaseType(closedCases);
                attorneyData["Ongoing Intakes"] = getIntakeBreakdownByCaseType(ongoingCases);
                returnValue[attorney] = attorneyData;
            }
        }

        return returnValue;
    }

    class ClientReportElement
    {
        Client client;
        Set<String> types = new HashSet<String>();

        public ClientReportElement(Client client, String type)
        {
            this.client = client;
            this.types.add(type);
        }

        public boolean equals(Object other)
        {
            if ( !(other instanceof ClientReportElement) )
                return false;
            ClientReportElement otherClientReportElement = (ClientReportElement)other;
            return client.equals(otherClientReportElement.client);
        }

        public int hashCode()
        {
            return client.hashCode();
        }
    }

    @Secured(['ROLE_ADMIN', "authentication.name == 'laurel'"])
    def report()
    {
        //println("**** report params: "+params)
        def returnValue = [ : ];

        if(params.startDate && params.endDate)
        {
            // adjust the endDate to be the end of the day.
            params.endDate = new Date(params.endDate.getTime() + 1000L*24L*60L*60L - 1L)

            def newClients = clientService.filterStatus(getNewClientsFromMunicipalityForTimePeriod( params ), params.statusAchieved)
            def newIntakes = clientService.filterStatus(getClientsWithNewIntakesFromMunicipalityForTimePeriod( params ), params.statusAchieved)
            def ongoingIntakes = clientService.filterStatus(getClientsWithOngoingIntakesFromMunicipalityForTimePeriod( params ), params.statusAchieved)
            def completedIntakes = clientService.filterStatus(getClientsWithCompletedIntakesFromMunicipalityForTimePeriod( params ), params.statusAchieved)

            def clients = new HashSet()
            newIntakes.each {
                client ->
                def clientReportElement = new ClientReportElement(client, "newIntake")
                clients.add(clientReportElement);
            }

            ongoingIntakes.each {
                client->
                def clientReportElement = new ClientReportElement(client, "ongoingIntake")
                if (clients.contains(clientReportElement))
                {
                    clientReportElement.types.add("newIntake")
                    clients.remove(clientReportElement)
                }
                clients.add(clientReportElement)
            }

            // There can be duplicates between the newIntakes and ongoingIntakes. By handling those 2 first, the duplicates
            // can be easily detected. That's why the newClients are added last.
            newClients.each
            { client ->
                clients.add( new ClientReportElement(client, "newClient") );
            }

            def sortedClients = new ArrayList(clients)
            Collections.sort(sortedClients, new ClientReportElementComparator());

            //println "newClients: " + newClients.size();
            //println "newIntakes: " + newIntakes.size();
            //println "ongoingIntakes: " + ongoingIntakes.size();

            def clientListCounts = [ : ]

            clientListCounts["newClient"] = ["New Clients with New Intakes", newClients.size(), getNumTotalNewClientsBetween(params.startDate, params.endDate)];
            //println "Client data: " + clientListCounts["newClient"]
            clientListCounts["newIntake"] = ["Existing Clients with New Intakes", newIntakes.size(), getNumTotalNewIntakesBetween(params.startDate, params.endDate)];
            clientListCounts["ongoingIntake"] = ["Existing Clients with Ongoing Intakes", ongoingIntakes.size(), getNumTotalOngoingIntakesBetween(params.startDate, params.endDate)];
            clientListCounts["completedIntake"] = ["Existing Clients with Completed Intakes", completedIntakes.size(), getNumTotalCompletedIntakesBetween(params.startDate, params.endDate)]
            
            returnValue["startDate"] = params.startDate
            returnValue["endDate"] = params.endDate
            returnValue["municipality"] = params.municipality;
            returnValue["munType"] = params.munType
            returnValue["attorney"] = params.attorney
            returnValue["displayIntakesCheckBox"] = params.displayIntakesCheckBox == "on" || params.displayIntakesCheckBox == "true"? "true" : "false"
            returnValue["intakeState"] = params.intakeState
            returnValue["statusAchieved"] = params.statusAchieved

            returnValue["report"] = true;
            returnValue["ClientListCounts"] = clientListCounts;
            returnValue["TotalFromMun"] = clientListCounts["newClient"][1] + clientListCounts["newIntake"][1] + clientListCounts["ongoingIntake"][1]
            returnValue["TotalFromEverywhere"] = clientListCounts["newClient"][2] + clientListCounts["newIntake"][2] + clientListCounts["ongoingIntake"][2]
            returnValue["Clients"] = sortedClients;
        }

        return returnValue;
    }

    Collection getNewClientsFromMunicipalityForTimePeriod( def params )
    {
        def query = getNewClientsQueryForMunicipalityType( params.munType, params.attorney, params.intakeState, params.statusAchieved )
        //println "Query Sring: " + query
        def namedParams = [mun:params.municipality, startDate:params.startDate, endDate:params.endDate]

        if ("State".equals(params.munType))
            namedParams += [munAlt:usStates.getAlternates(params.municipality)]

        //println "namedParams: "+namedParams
        def newClients = Client.executeQuery(query, namedParams );
        return newClients;
    }

    String getNewClientsQueryForMunicipalityType ( String municipalityType, String attorney, String intakeState, String statusAchieved )
    {
        //println "Getting new clients query for " + municipalityType
        def newClientsQueryString = """
        select distinct client from
           Client as client
           inner join fetch client.client as person
           inner join fetch person.address as address
           inner join fetch client.cases as intake
           where ( client.firstVisit between :startDate and :endDate ) and
        """
        newClientsQueryString += getMunicipalitySubQuery(municipalityType)
        newClientsQueryString += getAttorneySubQuery(attorney)

        if ("open".equals(intakeState))
            newClientsQueryString += " and intake.completionDate is null"
        else if ("closed".equals(intakeState))
            newClientsQueryString += " and intake.completionDate is not null"
        newClientsQueryString += " order by person.lastName"

        //println "New clients query: " + newClientsQueryString;
        return newClientsQueryString;
    }

    Collection getClientsWithNewIntakesFromMunicipalityForTimePeriod( def params )
    {
        def query = getClientsWithNewIntakesQueryForMunicipalityType( params.munType, params.attorney, params.intakeState, params.statusAchieved )
        def namedParams = [mun:params.municipality, startDate:params.startDate, endDate:params.endDate]

        if ("State".equals(params.munType))
            namedParams += [munAlt:usStates.getAlternates(params.municipality)]

        //println "namedParams: "+namedParams
        def newIntakeClients = Client.executeQuery( query, namedParams )
        return newIntakeClients;
    }

    String getAttorneySubQuery(String attorney)
    {
        if (attorney != null && !"".equals(attorney) && "Any".equals(attorney))
            return ""
        return " and intake.attorney = '"+attorney+"'"
    }

    String getMunicipalitySubQuery(String municipalityType)
    {
        String subQuery = ""
        if ("State".equals(municipalityType))
            subQuery += " (upper(address.state) = upper(:mun) or upper(address.state) = upper(:munAlt))"
        else if ("Any".equals(municipalityType))
            subQuery += " :mun = :mun" // So that there is a parameter for the municipality
        else
            subQuery += " upper(address.${municipalityType.toLowerCase()}) = upper(:mun)"

        return subQuery
    }

    String getClientsWithNewIntakesQueryForMunicipalityType( String municipalityType, String attorney, String intakeState, String statusAchieved )
    {
        //println "Getting new intakes query for " + municipalityType
        def newIntakesQueryString = """
            select distinct client
            from Client as client
               inner join fetch client.client as person
               inner join fetch person.address as address
               inner join fetch client.cases as intake where
        """
        newIntakesQueryString += getMunicipalitySubQuery(municipalityType)
        newIntakesQueryString += """       
               and client.firstVisit < :startDate
               and (intake.startDate between :startDate and :endDate )
        """
        newIntakesQueryString += getAttorneySubQuery(attorney)

        if ("open".equals(intakeState))
            newIntakesQueryString += " and intake.completionDate is null"
        else if ("closed".equals(intakeState))
            newIntakesQueryString += " and intake.completionDate is not null"
        newIntakesQueryString += " order by person.lastName"

        //println "New intakes query: " + newIntakesQueryString;
        return newIntakesQueryString;
    }

    Collection getClientsWithOngoingIntakesFromMunicipalityForTimePeriod( def params )
    {
        //println "Getting clients with ongoing intakes : " + params
        def query = getClientsWithOngoingIntakesQueryForMunicipalityType( params.munType, params.attorney, params.intakeState, params.statusAchieved )
        def namedParams = [mun:params.municipality, startDate:params.startDate, endDate:params.endDate]

        if ("State".equals(params.munType))
            namedParams += [munAlt:usStates.getAlternates(params.municipality)]

        //println "namedParams: "+namedParams
        def ongoingIntakeClients = Client.executeQuery( query, namedParams )
        return ongoingIntakeClients
    }
    
    Collection getClientsWithCompletedIntakesFromMunicipalityForTimePeriod(def params)
    {
        //println "Getting clients with completed intakes : " + params
        def query = getClientsWithCompletedIntakesQueryForMunicipalityType( params.munType, params.attorney, params.intakeState, params.statusAchieved )
        //println("completed intakes query: "+query)

        def namedParams = [mun:params.municipality, startDate:params.startDate, endDate:params.endDate]

        if ("State".equals(params.munType))
            namedParams += [munAlt:usStates.getAlternates(params.municipality)]

        //println "completed intakes namedParams: "+namedParams
        def ongoingIntakeClients = Client.executeQuery( query, namedParams )
        return ongoingIntakeClients
    }

    String getSQLForClientsWIthIntakeForMunicipality( String municipalityType, String attorney, String intakeState, String statusachieved, String ongoingOrCompleted)
    {
        //println "getting ongoing intakes for " + municipalityType
        def queryString = """
            select distinct client
            from Client as client
               inner join fetch client.client as person
               inner join fetch person.address as address
               inner join fetch client.cases as intake where
        """
        queryString += getMunicipalitySubQuery(municipalityType)
        queryString += getAttorneySubQuery(attorney)

        if ("open".equals(intakeState))
            queryString += " and intake.completionDate is null"
        else if ("closed".equals(intakeState))
            queryString += " and intake.completionDate is not null"
            
        queryString += ongoingOrCompleted

        queryString += " order by person.lastName"

        //println "Ongoing intakes query: " + queryString
        return queryString;
    }
    
    private static String ONGOING_CLIENT_SUB_QUERY = """
               and client.firstVisit < :startDate
               and intake.startDate < :startDate
               and ( intake.completionDate is null or intake.completionDate > :endDate )
"""
    private static String COMPLETED_CLIENT_SUB_QUERY = """
               and client.firstVisit < :startDate
               and intake.completionDate > :startDate
               and intake.completionDate < :endDate
"""

    String getClientsWithOngoingIntakesQueryForMunicipalityType( String municipalityType, String attorney, String intakeState, String statusAchieved )
    {
        return getSQLForClientsWIthIntakeForMunicipality( municipalityType, attorney, intakeState, statusAchieved, ONGOING_CLIENT_SUB_QUERY )
    }

    String getClientsWithCompletedIntakesQueryForMunicipalityType( String municipalityType, String attorney, String intakeState, String statusAchieved )
    {
        return getSQLForClientsWIthIntakeForMunicipality( municipalityType, attorney, intakeState, statusAchieved, COMPLETED_CLIENT_SUB_QUERY )
    }

    private int getNumTotalNewClientsBetween(Date startDate, Date endDate)
    {
        def queryString =
                """
        select distinct client
        from Client as client
        inner join fetch client.client as person
        where client.firstVisit between :startDate and :endDate
        """
        //println "Query String: " + queryString
        def numClients = Client.executeQuery(queryString, [startDate:startDate, endDate:endDate])
        return numClients.size()
    }

    private int getNumTotalNewIntakesBetween(Date startDate, Date endDate)
    {
        def queryString =
                """
         select distinct client
            from Client as client
               inner join fetch client.client as person
               inner join fetch client.cases as intake
               where client.firstVisit < :startDate
               and (intake.startDate between :startDate and :endDate )
               order by person.lastName
        """
        def numClients = Client.executeQuery(queryString, [startDate:startDate, endDate:endDate])
        return numClients.size()
    }

    private int getNumTotalOngoingIntakesBetween(Date startDate, Date endDate)
    {
        def queryString =
                """
        select distinct client
            from Client as client
               inner join fetch client.client as person
               inner join fetch client.cases as intake
               where client.firstVisit < :startDate
               and intake.startDate < :startDate
               and ( intake.completionDate is null or intake.completionDate > :endDate )
               order by person.lastName
        """
        def numClients = Client.executeQuery(queryString, [startDate:startDate, endDate:endDate])
        return numClients.size()
    }

    private int getNumTotalCompletedIntakesBetween(Date startDate, Date endDate)
    {
        def queryString =
                """
        select distinct client
            from Client as client
               inner join fetch client.client as person
               inner join fetch client.cases as intake
               where client.firstVisit < :startDate
               and intake.completionDate > :startDate and intake.completionDate < :endDate 
               order by person.lastName
        """
        def numClients = Client.executeQuery(queryString, [startDate:startDate, endDate:endDate])
        return numClients.size()
    }
}
