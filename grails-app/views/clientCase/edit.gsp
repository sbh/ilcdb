<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<g:javascript src="caseResult.js" />
<title>Edit Intake for ${clientCase?.client}
</title>
<script language="Javascript">
            window.onload = changeCaseTypeRowDisplay;
        </script>
</head>
<body>
    <div class="body">
        <h1>
            Edit Intake for <g:link controller="client" action="edit" id="${clientCase?.client?.id}">${clientCase?.client}</g:link></h1>
        </h1>
        <g:if test="${flash.message}">
            <div class="message">
                ${flash.message}
            </div>
        </g:if>
        <g:hasErrors bean="${clientCase}">
            <div class="errors">
                <g:renderErrors bean="${clientCase}" as="list" />
            </div>
        </g:hasErrors>
        <g:form method="post" name="intakeForm">
            <input type="hidden" name="id" value="${clientCase?.id}" />
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name"><label for="startDate">Start
                                    Date:</label></td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'startDate','errors')}">
                                <g:datePicker noSelection="['':'-Choose-']" default="none"
                                    name="startDate" precision="day" years="${2005..2050}"
                                    value="${clientCase?.startDate}" /></td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><label for="attorney">Representative:</label>
                            </td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'attorney','errors')}">
                                <g:select id="attorney" name="attorney"
                                    from="${clientCase.constraints.attorney.inList}"
                                    value="${clientCase.attorney}"
                                    noSelection="['null':'-Choose-']" />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><label for="intakeType">Intake Type:</label></td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'intakeType','errors')}">
                                <g:select onchange="changeCaseTypeRowDisplay('onchange')"
                                    id="intakeType" name="intakeType"
                                    from="${clientCase.constraints.intakeType.inList}"
                                    value="${clientCase.intakeType}" />
                            </td>
                        </tr>
 
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="intensity">Intensity:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'intensity','errors')}">
                                <g:select id="intensity" name="intensity" from="${clientCase.constraints.intensity.inList}" value="${clientCase.intensity}" />
                           </td>
                        </tr> 
                        
                        <tr name="caseTypeRow" id="caseTypeRow" class="prop">
                            <td valign="top" class="name"><label for="caseType">Case Type:</label></td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'caseType','errors')}">
                                <g:select id="caseType" name="caseType" from="${CaseType.list(sort:'type')}"
                                          optionKey="id" optionValue="type" noSelection="${[null:'-Choose-']}"
                                          value="${clientCase.caseType?.id}"
                                />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><label for="fileLocation">File
                                    Location:</label></td>
                            <td valign="top" colspan="3"
                                class="value ${hasErrors(bean:clientCase.client,field:'fileLocation','errors')}">
                                <input type="text" size="100%" id="fileLocation"
                                       name="fileLocation" value="${clientCase.client?.fileLocation}" />
                                <g:hasErrors bean="${clientCase.client}" field="fileLocation">
                                    <div class="errors">
                                        <g:renderErrors bean="${clientCase.client}"
                                            field="fileLocation" />
                                    </div>
                                </g:hasErrors></td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><label for="coltafNumber">COLTAF
                                    Number:</label></td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'coltafNumber','errors')}">
                                <input type="text" id="coltafNumber" name="coltafNumber"
                                value="${fieldValue(bean:clientCase,field:'coltafNumber')}" /></td>
                        </tr>

                        <tr name="resultRow" id="resultRow" class="prop">
                            <td valign="top" class="name"><label for="caseResult">Result:</label>
                            </td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'caseResult','errors')}">
                                <g:select onchange="changeCompletionDateRowDisplay();"
                                    id="caseResult" name="caseResult"
                                    from="${CaseResult.list(sort:'result')}"
                                    optionKey="id" optionValue="result"
                                    value="${clientCase.caseResult?.id}"
                                    noSelection="${[null:'-Choose-']}" />
                             </td>
                        </tr>

                        <tr name="completionDateRow" id="completionDateRow" class="prop">
                            <td valign="top" class="name"><label for="completionDate">Completion
                                    Date:</label></td>
                            <td valign="top"
                                class="value ${hasErrors(bean:clientCase,field:'completionDate','errors')}">
                                <g:datePicker noSelection="['':'-Choose-']" default="none"
                                    name="completionDate" precision="day" years="${2005..2050}"
                                    value="${clientCase?.completionDate}" /></td>
                        </tr>
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="notes">Notes:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'notes','errors')}">
                                    
                            <g:if test="${clientCase.notes.size() > 0}">
                              <table>
                                <thead>
                                  <th>Create Date</th>
                                  <th>Note</th>
                                </thead>
                                <tbody>
                                  <g:each var="n" in="${clientCase.notes?}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                      <td>
                                        ${n?.formattedDate}
                                      </td>
                                      <td>
                                        <g:link controller="note" params="['clientcaseid':clientCase?.id, 'noteType':'clientCase']" action="edit" id="${n.id}">${n?.encodeAsHTML().replaceAll("\n", "<br>")}</g:link>
                                      </td>
                                    </tr>
                                  </g:each>
                                </tbody>
                              </table>
                            </g:if>
                            
                            <g:link controller="note" params="['clientcaseid':clientCase?.id, 'noteType':'clientCase']" action="create">Add Note</g:link>
                          </td>
                        </tr> 
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <span class="button"><g:link controller="client" action="edit" id="${clientCase?.client?.id}"> &nbsp;Go Back</g:link>
                </span>
                <span class="button"><g:actionSubmit class="save"
                        value="Update" />
                </span>
                <span class="button"><g:actionSubmit class="delete"
                        onclick="return confirm('Are you sure?');" value="Delete" />
                </span>
            </div>
        </g:form>
    </div>
</body>
</html>
