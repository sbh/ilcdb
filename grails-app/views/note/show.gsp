

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Note Info</title>
    </head>
    <body>
        <div class="body">
            <h1>Note Info</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                        <tr class="prop">
                            <td valign="top" class="name">Client:</td>
                           	<g:if test="${note?.type == 'clientCase' }">
                            	<td valign="top" class="value"><g:link controller="client" action="show" id="${note?.intake?.client?.id}">${note?.intake?.client?.decodeHTML()}</g:link></td>
                           	</g:if>
                           	<g:else>
                            	<td valign="top" class="value"><g:link controller="client" action="show" id="${note?.client?.id}">${note?.client?.decodeHTML()}</g:link></td>
                           	</g:else>
                        </tr>
                        <g:if test="${note?.type == 'clientCase'}">
   							<tr class="prop">
   								<td valign="top" class="name">Case Type:</td>	
   								<td valign="top" class="value"><g:link cotroller="clientCase" action="show" id="${note?.intake?.id}"></g:link> </td>
   							</tr>            
   						</g:if>     
                        <tr class="prop">
                            <td valign="top" class="name">Create Date:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:note, field:'createDate')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Text:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:note, field:'text').decodeHTML()}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${note?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
