<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Status Achieved</title>
    </head>
    <body>
        <div class="body" style="width: 80%;">
            <h1>Status Achieved: ${statusAchieved.client}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${statusAchieved}">
            <div class="errors">
                <g:renderErrors bean="${statusAchieved}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
            <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Date Status Was Achieved:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:statusAchieved,field:'date','errors')}">
                                    <g:datePicker name="date" precision="day" value="${statusAchieved?.date}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Status Achieved:</label>
                                </td>
                                <td valign="top"><!-- class="value ${hasErrors(bean:statusAchieved,field:'type','errors')}"> -->
                                    <g:select name="type" from="${StatusAchieved.Type.values()}"
                                              noSelection="['':'-Choose Status Achieved-']"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
                <g:hiddenField name="client.id" value="${statusAchieved?.client?.id}" />
            </g:form>
        </div>
    </body>
</html>
