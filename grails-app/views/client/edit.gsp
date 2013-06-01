<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Client</title>
<script language="javascript"> 
function toggleDatePicker(theCheckBox)
{
    var hidden = !theCheckBox.checked
    
	if (theCheckBox.id == "legalPermanentResident")
    {
        document.clientEdit.legalPermanentResidentDate_day.hidden=hidden;
        document.clientEdit.legalPermanentResidentDate_month.hidden=hidden;
        document.clientEdit.legalPermanentResidentDate_year.hidden=hidden;
    }
    else if (theCheckBox.id == "citizen")
    {
        document.clientEdit.citizenDate_day.hidden=hidden;
        document.clientEdit.citizenDate_month.hidden=hidden;
        document.clientEdit.citizenDate_year.hidden=hidden
    }
    else if (theCheckBox.id == "daca")
    {
        document.clientEdit.dacaDate_day.hidden=hidden;
        document.clientEdit.dacaDate_month.hidden=hidden;
        document.clientEdit.dacaDate_year.hidden=hidden;
    }
    else if (theCheckBox.id == "tps")
    {
        document.clientEdit.tpsDate_day.hidden=hidden;
        document.clientEdit.tpsDate_month.hidden=hidden;
        document.clientEdit.tpsDate_year.hidden=hidden;
    }
}

function toggleOnLoad()
{
    toggleDatePicker(document.clientEdit.legalPermanentResident)
    toggleDatePicker(document.clientEdit.citizen)
    toggleDatePicker(document.clientEdit.daca)
    toggleDatePicker(document.clientEdit.tps)
}

window.onload=toggleOnLoad
</script> 
    </head>
    <body>
        <div class="body">
            <h1>Edit Client ${client}</h1>
            <g:if test="${flash.message}">
              <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${client}">
            <div class="errors">
              <h3><g:message code="general.page.errors" /></h3>
            </div>
            </g:hasErrors>
            <g:form method="post" name="clientEdit" id="clientEdit">
                <input type="hidden" name="clientId" value="${client?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="firstVisit">Date of First Visit:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'firstVisit','errors')}">
                            <g:datePicker name="firstVisit" value="${client?.firstVisit}" precision="day"/>
                          </td>
                        </tr> 

                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="cases">Intakes:</label>
                          </td>

                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'cases','errors')}">
                            <g:if test="${client.cases.size() > 0}">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Start Date</th>
                                            <th>Completion Date</th>
                                            <th>Intake Type</th>
                                            <th>Intensity</th>
                                            <th>Case Type</th>
                                            <th>Attorney</th>
                                            <th>Result</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                      <g:each var="intake" in="${client.cases}" status="i">
                                          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.startDateString}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.completionDateString}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.intakeType}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.intensity}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.caseType?.type}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.attorney}</g:link></td>
                                              <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.caseResult?.result}</g:link></td>
                                          </tr>
                                      </g:each>
                                    </tbody>
                                </table>
                            </g:if>
                            <g:link controller="clientCase" params="['client.id':client?.id]" action="create">Add Intake</g:link>

                          </td>
                        </tr> 
                        
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="notes">Notes:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'notes','errors')}">
                            <g:if test="${client.notes.size() > 0}">
                              <table>
                                <thead>
                                  <th>Create Date</th>
                                  <th>Note</th>
                                </thead>
                                <tbody>
                                  <g:each var="aNote" in="${client?.notes?}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                      <td>
                                        ${aNote?.formattedDate}
                                      </td>
                                      <td>
                                        <g:link controller="note" action="edit" id="${aNote.id}" params="['clientid':client.id, 'noteType':'client']">${aNote?.encodeAsHTML().replaceAll("\n", "<br>")}</g:link>
                                      </td>
                                    </tr>
                                  </g:each>
                                </tbody>
                              </table>
                            </g:if>
                            <g:link controller="note" params="['clientid':client?.id, 'noteType':'client']" action="create">Add Note</g:link>
                          </td>
                        </tr> 
<!--
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="conflicts">Conflicts:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'conflicts','errors')}">
                            <g:if test="${client.conflicts.size() > 0}">
                              <table>
                                <thead>
                                  <th>Create Date</th>
                                  <th>Conflict</th>
                                </thead>
                                <tbody>
                                  <g:each var="aConflict" in="${client?.conflicts?}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                      <td>
                                        ${aConflict?.formattedDate}
                                      </td>
                                      <td>
                                        <g:link controller="conflict" action="edit" id="${aConflict.id}" params="['clientid':client.id]">${aConflict?.encodeAsHTML().replaceAll("\n", "<br>")}</g:link>
                                      </td>
                                    </tr>
                                  </g:each>
                                </tbody>
                              </table>
                            </g:if>
                            <g:link controller="conflict" params="['clientid':client?.id]" action="create">Add Conflict</g:link>
                          </td>
                        </tr> 
 -->
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="serviceRecords">Service Records:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'serviceRecords','errors')}">
                                    
                            <ul>
                              <g:each var="serviceRecord" in="${client?.serviceRecords?}">
                                <li><g:link controller="serviceRecord" action="edit" id="${serviceRecord.id}">${serviceRecord?.encodeAsHTML()}</g:link></li>
                              </g:each>
                            </ul>
                            <g:link controller="serviceRecord" params="['client.id':client?.id]" action="create">Add Service Record</g:link>
                          </td>
                        </tr> 
                        
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="appointments">Appointments:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'appointments','errors')}">
                          <ul>
                            <g:each var="a" in="${client?.appointments?}">
                              <li><g:link controller="appointments" action="edit" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
                            </g:each>
                          </ul>
                          <g:link controller="appointment" params="['client.id':client?.id]" action="create">Add Appointment</g:link>
                        </td>
                      </tr> 
                        
                      <tr class="prop">
                        <td valign="top" class="name">
                          <label for="sponsors">Sponsors:</label>
                        </td>
                        <td valign="top" colspan="3" class="value ">
                          <ul>
                            <g:each var="sponsorRelation" in="${client.sponsorRelations}">
                              <li><g:link controller="sponsor" action="edit" id="${sponsorRelation.sponsor.id}">${sponsorRelation.sponsor}</g:link></li><
                            </g:each>
                          </ul>
                          <g:link controller="clientSponsorRelation" params="['client.id':client?.id]" action="create">Add Sponsor</g:link>
                        </td>
                      </tr>
                
                      <tr class="prop">
                        <td valign="top" class="name">
                          <label for="status">Status:</label>
                        </td>

                        <td valign="top" colspan="3" class="value">
                          <table>
                            <tr class="odd">
                              <td valign="top" class="name">
                                <label for="legalPermanentResident">LPR Status Achieved?:</label>
                              </td>
                              <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'legalPermanentResident','errors')}">
                                <g:checkBox name="legalPermanentResident" id="legalPermanentResident" value="${client?.legalPermanentResident}" onclick="toggleDatePicker(document.clientEdit.legalPermanentResident)" />
                                &nbsp;<g:datePicker name="legalPermanentResidentDate" id="legalPermanentResidentDate" value="${client?.legalPermanentResidentDate}" precision="day"/>
                              </td>
                            </tr> 
      
                            <tr class="even">
                              <td align="right" valign="top" class="name">
                                <label for="citizen">Citizenship Achieved?:</label>
                              </td>
                              <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'citizen','errors')}">
                                <g:checkBox name="citizen" id="citizen" value="${client?.citizen}" onclick="toggleDatePicker(document.clientEdit.citizen)" />
                                &nbsp;<g:datePicker name="citizenDate" id="citizenDate" value="${client?.citizenDate}" precision="day"/>
                              </td>
                            </tr>

                            <tr class="odd">
                              <td valign="top" class="name">
                                <label for="daca">DACA Status Achieved?:</label>
                              </td>
                              <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'daca','errors')}">
                                <g:checkBox name="daca" id="daca" value="${client?.daca}" onclick="toggleDatePicker(document.clientEdit.daca)" />
                                &nbsp;<g:datePicker name="dacaDate" id="dacaDate" value="${client?.dacaDate}" precision="day"/>
                              </td>
                            </tr> 
      
                            <tr class="even">
                              <td align="right" valign="top" class="name">
                                <label for="tps">TPS Achieved?:</label>
                              </td>
                              <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'tps','errors')}">
                                <g:checkBox name="tps" id="tps" value="${client?.tps}" onclick="toggleDatePicker(document.clientEdit.tps)" />
                                &nbsp;<g:datePicker name="tpsDate" id="tpsDate" value="${client?.tpsDate}" precision="day"/>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>

                      <tr class="prop">
                        <td valign="top" class="name">
                          <label for="client">Client:</label>
                        </td>
                        <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'client','errors')}">
                          <g:personGenerator name="client" value="${client?.client}" />
                        </td>
                      </tr> 
                        
                      <tr class="prop">
                          <td valign="top" class="name">
                              <label for="fileLocation">File Location:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'fileLocation','errors')}">
                              <input type="text" size="100%" id="fileLocation" name="fileLocation" value="${client?.fileLocation}"/>
                              <g:hasErrors bean="${client}" field="fileLocation">
                                  <div class="errors">
                                      <g:renderErrors bean="${client}" field="fileLocation" />
                                  </div>
                              </g:hasErrors>
                          </td>
                      </tr> 
                        
                      <tr class="prop">
                        <td valign="top" class="name">
                          <label for="numberInHousehold">Number in Household:</label>
                        </td>
                        <td valign="top" class="value ${hasErrors(bean:client,field:'numberInHousehold','errors')}">
                          <input type="text" name="numberInHousehold" value="${client?.numberInHousehold}" />
                          <g:hasErrors bean="${client}" field="numberInHousehold">
                            <div class="errors">
                              <g:renderErrors bean="${client}" field="numberInHousehold" />
                            </div>
                          </g:hasErrors>
                        </td>
                      </tr>

                      <tr class="prop">
                        <td valign="top" class="name">
                          <label for="householdIncomeLevel">Household Income Level:</label>
                        </td>
                        <td valign="top" class="value ${hasErrors(bean:client,field:'householdIncomeLevel','errors')}">
                          <input type="text" name="householdIncomeLevel" value="${client?.householdIncomeLevel}" />
                          <g:hasErrors bean="${client}" field="householdIncomeLevel">
                            <div class="errors">
                              <g:renderErrors bean="${client}" field="householdIncomeLevel" />
                            </div>
                          </g:hasErrors>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                    <span class="menuButton"><g:link action="show" target="_blank" id="${client.id}">Printable Summary Page</g:link></span>
                </div>
              <br />
            </g:form>
        </div>
    </body>
</html>
