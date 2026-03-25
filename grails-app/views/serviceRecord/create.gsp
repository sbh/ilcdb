
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />
        <title>Create Service Record</title>
    </head>
    <body>
        <div class="body" style="width: 80%;">
            <h1>Service Record</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${serviceRecord}">
            <div class="errors">
                <g:renderErrors bean="${serviceRecord}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                            <g:render template="serviceRecordFields" model="${[serviceRecord:serviceRecord]}" />
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
