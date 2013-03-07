

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show Sponsor</title>
    </head>
    <body>
        <div class="body">
            <h1>Show Sponsor</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">Sponsor:</td>
                            
                            <td valign="top" class="value"><g:link controller="person" action="show" id="${sponsor?.sponsor?.id}">${sponsor?.sponsor?.encodeAsHTML()}</g:link></td>
                            
                        </tr>

			<tr class="prop">
			    <td valign="top" class="name">Address:</td>

			    <td valign="top" class="value">${sponsor.sponsor?.address}</td>

			</tr>
			
			<tr class="prop">
			    <td valign="top" class="name">Clients:</td>

			    <td valign="top" class="value">
				<ul>
				<g:each var="clientRelation" in="${sponsor.clientRelations}">
				    <li><g:link controller="client" action="show" id="${clientRelation.client.id}">${clientRelation.client}</g:link></li>
				</g:each>
				</ul>
			    </td>
			</tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Income:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:sponsor, field:'income')}</td>
                            
                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${sponsor?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
