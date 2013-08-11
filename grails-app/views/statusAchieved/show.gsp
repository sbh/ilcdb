<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Status Achieved</title>
    </head>
    <body>
        <div class="body">
            <h1>Status Achieved</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Client:</td>
                           <td valign="top" class="value"><g:link controller="client" action="show" id="${statusAchieved?.client?.id}">${statusAchieved?.client?.decodeHTML()}</g:link></td>
                       </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Status Achieved Date:</td>
                           <td valign="top" class="value">${fieldValue(bean:statusAchieved, field:'statusAchievedDateString')}</td>
                       </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Type:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:statusAchieved, field:'type')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${statusAchieved?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
