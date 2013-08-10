<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client List</title>
    </head>
    <body>
        <div class="body">
            <h1>Client List (${clientList.size()} client families)</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <th>Client</th>
                            <th>Phone Number</th>
                            <th>Income</th>
                            <th># in House</th>
                            <th>Age</th>
                            <th>Race</th>
                            <th>Birth Country</th>
                            <th>City, County, State</th>
                            <th>File Location</th>
                            <th>Attorney</th>
                        </tr>
                    </thead>
                    <tbody>
                        <g:each in="${clientList}" status="i" var="client">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                <td><g:link action="edit" id="${client['id']}">${client['person']}<a name="${client?.id}"/></g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['phoneNumber']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['householdIncomeLevel']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['numberInHousehold']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['age']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['race']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['homeCountry']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['shortAddress']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['fileLocation']}</g:link></td>
                                <td><g:link action="edit" id="${client['id']}">${client['attorney']}</g:link>${!client['validCases']}</td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
                        </div>
        </div>
    </body>
</html>
