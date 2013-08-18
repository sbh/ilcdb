<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Conflict</title>
    </head>
    <body>
        <div class="body">
            <h1>Edit Conflict for ${conflict?.client.encodeAsHTML()}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${conflict}">
            <div class="errors">
                <g:renderErrors bean="${conflict}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${conflict?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="firstName">Conflict First Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'firstName','errors')}">
                                    <input type="text" id="firstName" name="firstName" value="${fieldValue(bean:conflict,field:'firstName')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastName">Conflict Last Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'lastName','errors')}">
                                    <input type="text" id="lastName" name="lastName" value="${fieldValue(bean:conflict,field:'lastName')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'createDate','errors')}">
                                    <g:datePicker name="createDate" value="${conflict?.createDate}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reason">Reason:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'reason','errors')}">
                                    <input type="text" id="reason" name="reason" value="${fieldValue(bean:conflict,field:'reason')}"/>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
                <g:hiddenField name="client.id" value="${conflict?.client?.id}" />
            </g:form>
        </div>
    </body>
</html>
