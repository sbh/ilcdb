<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Search</title>
    </head>
    <body>
        <div class="body">
            <h1>Search</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
	    <g:if test="${searchResults.size() < 1}">
	    <div class="message"><g:message code="general.page.noresults" /></div>
	    </g:if>
            <div class="dialog">
                <g:form controller="conflict" action="search" method="POST">
		    <table>
			<tbody>
			    <tr class="prop">
				<td valign="top" class="name">
				    <label for="start">Start date:</label>
				</td>
				<td valign="top" class="value">
				    <g:datePicker name="start" value="${params.start}" />
				</td>
			    </tr>
			    
			    <tr class="prop">
				<td valign="top" class="name">
				    <label for="end">End date:</label>
				</td>
				<td valign="top" class="value">
				    <g:datePicker name="end" value="${params.end}" />
				</td>
			    </tr>

			    <tr class="prop">
				<td valign="top">
				    <label for="count">Results per page:</label>
				</td>
				<td valign="top">
				    <g:select id="count" name="max" from="${[10,20,30,40,50]}" value="${params?.max}" />
				</td>
			    </tr>

			    <tr class="prop">
				<td valign="top">
				    <input type="submit" />
				</td>
			    </tr>
			</tbody>
		    </table>
                </g:form>
            </div>
	    <g:if test="${searchResults?.size() > 0}">
            <div class="list">
                <table>
		    <thead>
			<th>Results</th>
		    </thead>
                    <tbody>
			<g:each status="i" var="result" in="${searchResults}">
			    <tr class="${(i % 2 == 0) ? 'odd' : 'even'}">
				<td valign="top"><g:searchResultObject value="${result}" /></td>
			    </tr>
			</g:each>
                    </tbody>
                </table>
            </div>
	    <div class="paginateButtons">
		<g:paginate params="${params}" total="${params?.count}" />
	    </div>
	    </g:if>
        </div>
    </body>
</html>

