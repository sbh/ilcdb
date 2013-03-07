

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client Sponsor Relation Info</title>
    </head>
    <body>
        <div class="body">
            <h1>Client Sponsor Relation Info</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Client:</td>
                            
                            <td valign="top" class="value"><g:link controller="client" action="show" id="${clientSponsorRelation?.client?.id}">${clientSponsorRelation?.client?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Sponsor:</td>
                            
                            <td valign="top" class="value"><g:link controller="sponsor" action="show" id="${clientSponsorRelation?.sponsor?.id}">${clientSponsorRelation?.sponsor?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <input type="hidden" name="id" value="${clientSponsorRelation?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
