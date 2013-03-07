<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Note</title>
    </head>
    <body>
        <div class="body" style="width: 80%">
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
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Create Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'createDate','errors')}">
                                    <g:datePicker name="createDate" value="${note?.createDate}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Text:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'text','errors')}">
                                    <g:textArea name="text" rows="5" cols="100">${fieldValue(bean:note,field:'text').decodeHTML()}</g:textArea>
                                </td>
                            </tr> 
                        
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
