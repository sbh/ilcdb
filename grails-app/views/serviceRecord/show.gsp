<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Service Record</title>
    </head>
    <body>
        <div class="body">
            <h1>Service Record</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Client:</td>
                           <td valign="top" class="value"><g:link controller="client" action="show" id="${serviceRecord?.client?.id}">${serviceRecord?.client?.decodeHTML()}</g:link></td>
                       </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Service Record Date:</td>
                           <td valign="top" class="value">${fieldValue(bean:serviceRecord, field:'serviceDateString')}</td>
                       </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Hours:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:serviceRecord, field:'serviceHours')}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${serviceRecord?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
