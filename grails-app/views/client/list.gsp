<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Client List</title>
    </head>
    <body>
        <div class="body">
            <h1>Client List: (${clientCount} client families)</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <g:form action="list" method="get">
                    <label for="max">Clients per page:</label>
                    <g:select name="max" from="[10, 25, 50, 100, 'All']" value="${params.max ?: 'All'}" onchange="this.form.submit()" />
                </g:form>
                <g:render template="/templates/clientListTemplate" model="[clientList: clientList]" />
            </div>
            <div class="paginateButtons">
                <g:paginate total="${clientCount}" params="${params}" />
            </div>
        </div>
    </body>
</html>
