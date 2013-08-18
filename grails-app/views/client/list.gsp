<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client List</title>
    </head>
    <body>
        <div class="body">
            <h1>Client List (${clientList.size()} client families)</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <g:render template="/templates/clientListTemplate" model="[clientList: clientList]" />
            </div>
        </div>
    </body>
</html>
