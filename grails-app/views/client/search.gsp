<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Search</title>
    </head>
    <body>
        <div class="body">
            <h1>Search</h1>
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <g:form controller="client" action="search" method="GET" name="clientSearch" id="clientSearch">
                    <input id="q" type="text" name="q" value="${params?.q}" />
                    <g:actionSubmit class="save" value="Search" />
                </g:form>
            </div>

            <div class="list">
                <g:render template="/templates/clientListTemplate" model="[clientList: searchResults]" />
            </div>

            <g:if test="${searchResults?.size() > 0}">
                <div class="paginateButtons">
                    <g:paginate total="${searchResults.totalCount}" params="${params}" />
                </div>
            </g:if>
        </div>
    </body>
</html>
