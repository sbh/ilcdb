

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>BirthPlace List</title>
    </head>
    <body>
        <div class="body">
            <h1>BirthPlace List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="country" title="Country" />
                        
                   	        <g:sortableColumn property="city" title="City" />
                        
                   	        <g:sortableColumn property="state" title="State" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${birthPlaceList}" status="i" var="birthPlace">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${birthPlace.id}">${fieldValue(bean:birthPlace, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:birthPlace, field:'country')}</td>
                        
                            <td>${fieldValue(bean:birthPlace, field:'city')}</td>
                        
                            <td>${fieldValue(bean:birthPlace, field:'state')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${BirthPlace.count()}" />
            </div>
        </div>
    </body>
</html>
