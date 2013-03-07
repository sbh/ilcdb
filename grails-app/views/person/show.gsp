

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Person</title>
    </head>
    <body>
        <div class="body">
            <h1>Show Person</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">First Name:</td>
                            <td valign="top" class="value">${fieldValue(bean:person, field:'firstName')}</td>
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Last Name:</td>
                            <td valign="top" class="value">${fieldValue(bean:person, field:'lastName')}</td>
                        </tr>
                
                        <tr class="prop">
                            <td valign="top" class="name">Gender:</td>
                            <td valign="top" class="value">${fieldValue(bean:person, field:'gender')}</td>
                        </tr>
            
                        <tr class="prop">
                            <td valign="top" class="name">email address:</td>
                            <td valign="top" class="value">${fieldValue(bean:person, field:'emailAddress')}</td>
                        </tr>
            
                        <tr class="prop">
                            <td valign="top" class="name">Address:</td>
                            
                            <td valign="top" class="value"><g:link controller="address" action="show" id="${person?.address?.id}">${person?.address?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${person?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
