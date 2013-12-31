<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Status Achieved</title>
   </head>
    <body>
        <div class="body" style="width: 80%">
            <h1>Edit Status Achieved</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${statusAchieved}">
            <div class="errors">
                <g:renderErrors bean="${statusAchieved}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${statusAchieved?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Client:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:statusAchieved,field:'client','errors')}">
                                    <g:select optionKey="id" from="${Client.list()}" name="client.id" value="${statusAchieved?.client?.id}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Status Achieved Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:statusAchieved,field:'date','errors')}">
                                    <g:datePicker name="date" precision="day" value="${statusAchieved?.date}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Type:</label>
                                </td>
                                <td valign="top"><!-- class="value ${hasErrors(bean:statusAchieved,field:'type','errors')}"> -->
                                    <g:select name="type" from="${StatusAchieved.Type.values()}"
                                          value="${statusAchieved?.type}"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
