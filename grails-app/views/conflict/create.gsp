<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />
        <title>Create Conflict</title>
    </head>
    <body>
        <div class="body">
            <h1>Create Conflict for ${conflict?.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${conflict}">
            <div class="errors">
                <g:renderErrors bean="${conflict}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="conflictFields" model="${[conflict:conflict]}" />
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
                <g:hiddenField name="client.id" value="${conflict?.client?.id}" />
            </g:form>
        </div>
    </body>
</html>
