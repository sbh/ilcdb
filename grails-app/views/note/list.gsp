

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Note List</title>
    </head>
    <body>
        <div class="body">
            <h1>Note List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <th>Client</th>
                   	    
                   	        <g:sortableColumn property="createDate" title="Create Date" />
                        
                   	        <g:sortableColumn property="text" title="Text" />
                        
				<th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${noteList}" status="i" var="note">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:note, field:'client')}</td>
                        
                            <td>${fieldValue(bean:note, field:'createDate')}</td>
                        
                            <td>${fieldValue(bean:note, field:'text')}</td>
			    
			    <td><g:link action="show" id="${note.id}">Show</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Note.count()}" />
            </div>
        </div>
    </body>
</html>
