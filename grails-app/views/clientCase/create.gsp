<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />
        <title>Create Intake for ${clientCase?.client}</title>
        <script language="Javascript">
            window.onload = changeCaseTypeRowDisplay;
        </script>
    </head>
    <body>
        <div class="body">
            <h1>Create Intake for ${clientCase?.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${clientCase}">
            <div class="errors">
                <g:renderErrors bean="${clientCase}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" name="intakeForm">
                <input type="hidden" name="clientId" value="${clientCase?.client?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="caseFields" model="${[clientCase:clientCase]}" />
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
