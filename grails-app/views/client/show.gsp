<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client Info</title>
    </head>
    <body>
        <div class="body">
            <h1>Client Info</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Name:</td>
                            <td valign="top" class="value"><g:link controller="person" action="show" id="${client?.client?.id}">${client?.client?.encodeAsHTML()}</g:link></td>
                        </tr>
            
                        <tr class="prop">
                             <td valign="top" class="name">Address:</td>
                             <td valign="top" class="value">${client?.client?.address?.encodeAsHTML()}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Phone Number:</td>
                            <td valign="top" class="value">${client?.client?.phoneNumber}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Email Address:</td>
                            <td valign="top" class="value"><a href="mailto:${client?.client?.emailAddress}">${client?.client?.emailAddress}</a></td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Date Of Birth:</td>
                            <td valign="top" class="value">${client.client?.birthDayString} (${client.client?.age} years old)</td>
                       </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Place Of Birth:</td>
                            <td valign="top" class="value"><g:link controller="address" action="show" id="${client?.client?.placeOfBirth?.id}">${client?.client?.placeOfBirth?.encodeAsHTML()}</g:link></td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Race:</td>
                            <td valign="top" class="value"><a href="mailto:${client?.client?.race}">${client?.client?.race}</a></td>
                        </tr>

                        <tr class="prop">
                             <td valign="top" class="name">Number in Household:</td>
                             <td valign="top" class="value">${client?.numberInHousehold}</td>
                          </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Annual Household Income:</td>
                             <td valign="top" class="value">${client?.householdIncomeLevel}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Date of First Visit:</td>
                            <td valign="top" class="value">${client?.firstVisitString}</td>
                    </tr>
            
                    <tr class="prop">
                        <td valign="top" class="name">Sponsors:</td>
                        <td valign="top" class="value">
                            <ul>
                                <g:each var="sponsorRelation" in="${client.sponsorRelations}">
                                    <li><g:link controller="sponsor" action="show" id="${sponsorRelation.sponsor.id}">${sponsorRelation.sponsor}</g:link></li>
                                </g:each>
                            </ul>
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                             <label for="cases">Cases:</label>
                        </td>
                        <td valign="top" class="value ${hasErrors(bean:client,field:'cases','errors')}">
                            <ul>
                                <g:each var="c" in="${client?.cases?}">
                                    <li><g:link controller="clientCase" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
                                </g:each>
                            </ul>
                        </td>
                    </tr>

                    <tr class="prop">
                       <td valign="top" class="name">Notes:</td>
                       <td  valign="top" style="text-align:left;" class="value">
                           <ul>
                                <g:each var="n" in="${client.notes}">
                                    <li><g:link controller="note" action="show" id="${n.id}">${n.toString()}</g:link></li>
                                </g:each>
                            </ul>
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Service Records:</td>
                        <td  valign="top" style="text-align:left;" class="value">
                             <ul>
                                <g:each var="serviceRecord" in="${client.serviceRecords}">
                                    <li><g:link controller="serviceRecord" action="show" id="${serviceRecord.id}">${serviceRecord.toString()}</g:link></li>
                                </g:each>
                             </ul>
                        </td>
                    </tr>

                    <tr class="prop">
                         <td valign="top" class="name">Appointments:</td>
                         <td  valign="top" style="text-align:left;" class="value">
                              <ul>
                                   <g:each var="a" in="${client.appointments}">
                                       <li><g:link controller="appointment" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
                                   </g:each>
                              </ul>
                         </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Previous Appointment:</td>
                        <td valign="top" style="text-align:left;" class="value">
                            <g:clientPreviousAppointment client="${client}" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">Next Appointment:</td>
                        <td valign="top" style="text-align:left;" class="value">
                            <g:clientNextAppointment client="${client}" />
                        </td>
                    </tr>
                  </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${client?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
