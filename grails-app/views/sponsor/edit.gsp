

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Sponsor</title>
    </head>
    <body>
        <div class="body">
            <h1>Edit Sponsor</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${sponsor}">
            <div class="errors">
                <g:message code="general.page.errors" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${sponsor?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sponsor">Sponsor:</label>
                                </td>
                                <td valign="top" class="value">
                                    <g:personGenerator name="sponsor" value="${sponsor?.sponsor}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="clients">Clients:</label>
                                </td>
                                <td valign="top" class="value">
                                    <ul>
                                        <g:each var="clientRelation" in="${sponsor.clientRelations}">
                                            <li><g:link controller="client" action="show" id="${clientRelation.client.id}">${clientRelation.client}</g:link></li>
                                        </g:each>
                                    </ul>
                                    <g:link controller="clientSponsorRelation" params="['client.id':client?.id]" action="create">Add Client</g:link>
                                </td>
                            </tr>

                            <g:render template="sponsorFields" model="${[sponsor:sponsor]}" />

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
