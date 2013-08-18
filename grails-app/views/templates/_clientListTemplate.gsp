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
                                <td><g:link action="edit" id="${client['id']}">${client['attorney']}</g:link> ${client['validCases']}</td>
                            </tr>
                        </g:each>

                    </tbody>
                </table>

