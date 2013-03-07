

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Person List</title>
    </head>
    <body>
        <div class="body">
            <h1>Person List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <g:sortableColumn property="firstName" title="First Name" />
                        
                            <g:sortableColumn property="lastName" title="Last Name" />

                        <th>English Proficiency</th>
                        <th>email address</th>
                   	    <th>Address</th>

				<th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${personList}" status="i" var="person">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${fieldValue(bean:person, field:'firstName')}</td>
                        
                            <td>${fieldValue(bean:person, field:'lastName')}</td>

                            <td>${fieldValue(bean:person, field:'englishProficiency')}</td>
                        
                            <td>${fieldValue(bean:person, field:'emailAddress')}</td>
                        
                            <td>${fieldValue(bean:person, field:'address')}</td>

                            <td><g:link action="show" id="${person.id}">Show</g:link></td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Person.count()}" />
            </div>
        </div>
    </body>
</html>
