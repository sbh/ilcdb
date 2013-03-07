

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Address List</title>
    </head>
    <body>
        <div class="body">
            <h1>Address List</h1>
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
                        
                   	        <g:sortableColumn property="county" title="County" />
                        
                   	        <g:sortableColumn property="postalCode" title="Postal Code" />
                        
                   	        <g:sortableColumn property="state" title="State" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${addressList}" status="i" var="address">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${address.id}">${fieldValue(bean:address, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:address, field:'country')}</td>
                        
                            <td>${fieldValue(bean:address, field:'city')}</td>
                        
                            <td>${fieldValue(bean:address, field:'county')}</td>
                        
                            <td>${fieldValue(bean:address, field:'postalCode')}</td>
                        
                            <td>${fieldValue(bean:address, field:'state')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Address.count()}" />
            </div>
        </div>
    </body>
</html>
