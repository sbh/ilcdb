<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <g:javascript src="caseResult.js" />
        <title>Edit Note</title>
    </head>
    <body>
        <div class="body" style="width: 80%;">
            <h1>Edit Note</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${note}">
            <div class="errors">
                <g:renderErrors bean="${note}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${note?.id}" />
                <g:hiddenField name="noteType" value="${noteType}" />
                <g:if test="${noteType == 'client' }" >
                    <g:hiddenField name="clientid" value ="${clientid}" />
                </g:if>
                <g:if test="${noteType == 'clientCase' }" >
                    <g:hiddenField name="clientcaseid" value="${clientcaseid}"/>
                </g:if>
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Client: ${Client.get(clientid)}</label>
                                </td>
                            </tr>

                            <g:render template="noteFields" model="${[note:note]}" />

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                     <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
