

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Service Record</title>         
    </head>
    <body>
        <div class="body" style="width: 80%;">
            <h1>Service Record</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${serviceRecord}">
            <div class="errors">
                <g:renderErrors bean="${serviceRecord}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
            <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Service Record:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'client','errors')}">
                                    <g:select optionKey="id" from="${Client.list()}" name="client.id" value="${serviceRecord?.client?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Date of Service:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'serviceDate','errors')}">
                                    <g:datePicker name="serviceDate" precision="day" value="${serviceRecord?.serviceDate}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Hours:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'text','errors')}">
                                    <input type="text" id="serviceHours" name="serviceHours" value="${serviceRecord?.serviceHours}" />
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
