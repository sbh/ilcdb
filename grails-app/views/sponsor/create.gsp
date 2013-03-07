

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Sponsor</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Sponsor</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sponsor}">
            <div class="errors">
                <h3><g:message code="general.page.errors" /></h3>
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sponsor">Sponsor:</label>
                                </td>
                                <td valign="top" class="value">
				    <g:personSelectorGenerator name="sponsor" value="${sponsor?.sponsor}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="income">Income:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsor,field:'income','errors')}">
                                    <g:select id="income" name="income" from="${sponsor.constraints.income.inList}" value="${sponsor.income}" ></g:select>
				    <g:hasErrors bean="${sponsor}" field="client">
				    <div class="errors">
					<g:renderErrors bean="${sponsor}" field="client" as="list" />
				    </div>
				    </g:hasErrors>
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
