<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />              
        <title>Create Intake for ${clientCase?.client}</title>         
        <script language="Javascript">
            window.onload = changeCaseTypeRowDisplay;
        </script> 
    </head>
    <body>
        <div class="body">
            <h1>Create Intake for ${clientCase?.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${clientCase}">
            <div class="errors">
                <g:renderErrors bean="${clientCase}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post"  name="intakeForm">
                <input type="hidden" name="clientId" value="${clientCase?.client?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="startDate">Start Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'startDate','errors')}">
                                    <g:datePicker name="startDate" precision="day" years="${2005..2050}" value="${clientCase?.startDate}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="attorney">Attorney:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'attorney','errors')}">
                                    <g:select id="attorney" name="attorney" from="${clientCase.constraints.attorney.inList}" value="${clientCase.attorney}" ></g:select>
                               </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="intakeType">Intake Type:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'intakeType','errors')}">
                                    <g:select onchange="changeCaseTypeRowDisplay('onchange')" id="intakeType" name="intakeType" from="${clientCase.constraints.intakeType.inList}" value="${clientCase.intakeType}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="intensity">Intensity:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'intensity','errors')}">
                                    <g:select id="intensity" name="intensity" from="${clientCase.constraints.intensity.inList}" value="${clientCase.intensity}" ></g:select>
                               </td>
                            </tr> 
                        
                            <tr name="caseTypeRow" id="caseTypeRow" class="prop">
                                <td valign="top" class="name"><label for="caseType">Case Type:</label></td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'caseType','errors')}">
                                    <g:select id="caseType" name="caseType" from="${CaseType.list(sort:'type')}"
                                              optionKey="id" optionValue="type"  noSelection="${['null':'-Choose-']}"
                                              value="${clientCase.caseType?.id}"
                                    />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fileLocation">File Location:</label>
                                </td>
                                <td valign="top" colspan="3" class="value ${hasErrors(bean:clientCase.client,field:'fileLocation','errors')}">
                                    <input type="text" size="100%" id="fileLocation" name="fileLocation" value="${clientCase.client?.fileLocation}"/>
                                    <g:hasErrors bean="${clientCase.client}" field="fileLocation">
                                        <div class="errors">
                                            <g:renderErrors bean="${clientCase.client}" field="fileLocation" />
                                        </div>
                                    </g:hasErrors>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="coltafNumber">COLTAF Number:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'coltafNumber','errors')}">
                                    <input type="text" id="coltafNumber" name="coltafNumber" value="${fieldValue(bean:clientCase,field:'coltafNumber')}"/>
                                </td>
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
                                        noSelection="${['null':'-Choose-']}"></g:select></td>
                            </tr>

                            <tr name="completionDateRow" id="completionDateRow" class="prop">
                                <td valign="top" class="name">
                                    <label for="completionDate">Completion Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientCase,field:'completionDate','errors')}">
                                    <g:datePicker noSelection="['':'-Choose-']" default="none" name="completionDate" precision="day" years="${2005..2050}" value="${clientCase?.completionDate}" />
                                </td>
                            </tr> 
                        
                       </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
