<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Conflict List</title>
    </head>
    <body>
        <div class="body">
            <h1>Conflict List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <th>Client</th>

                            <g:sortableColumn property="lastName" title="Last Name" />

                            <g:sortableColumn property="firstName" title="First Name" />

				            <th>&nbsp;</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${conflictList}" status="i" var="conflict">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${conflict.client.encodeAsHTML()}</td>

                            <td>${conflict.lastName}</td>

                            <td>${conflict.firstName}</td>

             			    <td><g:link action="show" id="${conflict.id}">Show</g:link></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Conflict.count()}" />
            </div>
        </div>
    </body>
</html>
