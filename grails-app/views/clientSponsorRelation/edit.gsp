

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit ClientSponsorRelation</title>
    </head>
    <body>
        <div class="body">
            <h1>Edit ClientSponsorRelation</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${clientSponsorRelation}">
            <div class="errors">
                <g:renderErrors bean="${clientSponsorRelation}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${clientSponsorRelation?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="clientSponsorRelationFields" model="${[clientSponsorRelation:clientSponsorRelation]}" />
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
