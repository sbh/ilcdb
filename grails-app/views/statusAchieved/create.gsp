<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />
        <title>Create Status Achieved</title>
    </head>
    <body>
        <div class="body" style="width: 80%;">
            <h1>Status Achieved: ${statusAchieved.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${statusAchieved}">
            <div class="errors">
                <g:renderErrors bean="${statusAchieved}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="statusAchievedFields" model="${[statusAchieved:statusAchieved]}" />
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
                <g:hiddenField name="client.id" value="${statusAchieved?.client?.id}" />
            </g:form>
        </div>
    </body>
</html>
