<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client Case Info</title>
    </head>
    <body>
        <div class="body">
            <h1>Client Case Info for ${clientCase?.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Start Date:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'startDateString')}</td>
                        </tr> 
                        
                        <tr class="prop">
                            <td valign="top" class="name">Intake Type:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'intakeType')}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Representative:</td>
                            <td valign="top" class="value">${clientCase?.attorney}</td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Case Type:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'caseType')}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">COLTAF Number:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'coltafNumber')}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">File Location:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'fileLocation')}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Result:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'caseResult')}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Completion Date:</td>
                            <td valign="top" class="value">${fieldValue(bean:clientCase, field:'completionDateString')}</td>
                        </tr> 
                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="notes">Notes:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'notes','errors')}">
                                    
                            <ul>
                              <g:each var="n" in="${clientCase?.notes?}">
                                <li><g:link controller="note" action="edit" id="${n.id}">${n?.encodeAsHTML()}</g:link></li>
                              </g:each>
                            </ul> 
                            <g:link controller="note" params="['clientcaseid':clientCase?.id, 'type':'clientCase']" action="create">Add Note</g:link>
                          </td>
                        </tr> 
                        
                   </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${clientCase?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
