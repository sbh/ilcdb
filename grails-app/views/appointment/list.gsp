

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Appointment List</title>
    </head>
    <body>
        <div class="body">
            <h1>Appointment List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <th>Client</th>
                   	    
                   	        <g:sortableColumn property="date" title="Date" />
                        
                   	        <g:sortableColumn property="note" title="Note" />

				<th>&nbsp;</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${appointmentList}" status="i" var="appointment">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:appointment, field:'client')}</td>
                        
                            <td>${fieldValue(bean:appointment, field:'date')}</td>
                        
                            <td>${fieldValue(bean:appointment, field:'note')}</td>

			    <td><g:link action="show" id="${appointment.id}">Show</g:link></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Appointment.count()}" />
            </div>
        </div>
    </body>
</html>
