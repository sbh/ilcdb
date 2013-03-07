

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Sponsor List</title>
    </head>
    <body>
        <div class="body">
            <h1>Sponsor List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <th>Sponsor</th>
                        
                   	        <g:sortableColumn property="income" title="Income" />
                   	    
				<th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${sponsorList}" status="i" var="sponsor">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:sponsor, field:'sponsor')}</td>
                        
                            <td>${fieldValue(bean:sponsor, field:'income')}</td>
                        
			    <td><g:link action="show" id="${sponsor.id}">Show</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Sponsor.count()}" />
            </div>
        </div>
    </body>
</html>
