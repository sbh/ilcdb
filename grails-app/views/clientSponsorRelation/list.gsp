

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>ClientSponsorRelation List</title>
    </head>
    <body>
        <div class="body">
            <h1>ClientSponsorRelation List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <th>Client</th>
                   	    
                   	        <th>Sponsor</th>

				<th>&nbsp;</th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${clientSponsorRelationList}" status="i" var="clientSponsorRelation">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:clientSponsorRelation, field:'client')}</td>
                        
                            <td>${fieldValue(bean:clientSponsorRelation, field:'sponsor')}</td>
                        
			    <td><g:link action="show" id="${clientSponsorRelation.id}">Show</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${ClientSponsorRelation.count()}" />
            </div>
        </div>
    </body>
</html>
