<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Service Records</title>
    </head>
    <body>
        <div class="body">
            <h1>Service Records</h1>
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
                    <g:each in="${serviceRecords}" status="i" var="serviceRecord">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:serviceRecord, field:'client')}</td>
                        
                            <td>${fieldValue(bean:serviceRecord, field:'serviceDate')}</td>
                        
                            <td>${fieldValue(bean:serviceRecord, field:'serviceHours')}</td>
                
                            <td><g:link action="show" id="${serviceRecord.id}">Show</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${ServiceRecord.count()}" />
            </div>
        </div>
    </body>
</html>
