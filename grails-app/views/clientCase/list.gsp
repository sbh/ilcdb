<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>ClientCase List</title>
    </head>
    <body>
        <div class="body">
            <h1>ClientCase List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                               <th>Client</th>

                               <g:sortableColumn property="caseType" params="${params['type'] != null ? [type: params['type']] : []}" title="Case Type" />
                               <g:sortableColumn property="coltafNumber" params="${params['type'] != null ? [type: params['type']] : []}" title="COLTAF Number" />
                               <g:sortableColumn property="fileLocation" params="${params['type'] != null ? [type: params['type']] : []}" title="File Location" />
                               <g:sortableColumn property="caseResult" params="${params['type'] != null ? [type: params['type']] : []}" title="Result" />
                       
                <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${clientCaseList}" status="i" var="clientCase">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                           <td>${fieldValue(bean:clientCase, field:'client')}</td>
                           <td>${fieldValue(bean:clientCase, field:'caseType')}</td>
                           <td>${fieldValue(bean:clientCase, field:'coltafNumber')}</td>
                           <td>${fieldValue(bean:clientCase, field:'fileLocation')}</td>
                           <td>${fieldValue(bean:clientCase, field:'caseResult')}</td>
                       
                           <td><g:link action="show" id="${clientCase.id}">Show</g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${ClientCase.count()}" />
            </div>
        </div>
    </body>
</html>
