<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Client</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Client</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${client}">
            <div class="errors">
        <h3><g:message code="general.page.errors" /></h3>
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="firstVisit">Date of First Visit:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:client,field:'firstVisit','errors')}">
                                <g:datePicker name="firstVisit" value="${client?.firstVisit}" precision="day"></g:datePicker>
                            </td>
                        </tr> 
                
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="client">Client:</label>
                            </td>
                            <td valign="top" class="value">
                                <g:personSelectorGenerator name="client" value="${client?.client}" />
                            </td>
                        </tr> 
                        
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="fileLocation">File Location:</label>
                            </td>
                            <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'fileLocation','errors')}">
                                <input type="text" id="fileLocation" name="fileLocation" value="${client?.fileLocation}"/>
                                <g:hasErrors bean="${client}" field="fileLocation">
                                    <div class="errors">
                                        <g:renderErrors bean="${client}" field="fileLocation" />
                                    </div>
                                </g:hasErrors>
                            </td>
                        </tr> 
                        
                        <tr clss="prop">
                            <td valign="top" class="name">
                                <label for="numberInHousehold">Number in Household:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:client,field:'numberInHousehold','errors')}">
                                <input type="text" id="numberInHousehold" name="numberInHousehold" value="${client?.numberInHousehold}" />
                                <g:hasErrors bean="${client}" field="numberInHousehold">
                                    <div class="errors">
                                        <g:renderErrors bean="${client}" field="numberInHousehold" />
                                    </div>
                                </g:hasErrors>
                            </td>
                        </tr>

                        <tr clss="prop">
                            <td valign="top" class="name">
                                <label for="householdIncomeLevel">Household Income Level:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:client,field:'householdIncomeLevel','errors')}">
                                <input type="text" id="householdIncomeLevel" name="householdIncomeLevel" value="${client?.householdIncomeLevel}" />
                                <g:hasErrors bean="${client}" field="householdIncomeLevel">
                                    <div class="errors">
                                        <g:renderErrors bean="${client}" field="householdIncomeLevel" />
                                    </div>
                                </g:hasErrors>
                            </td>
                        </tr>

                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <span class="button"><input class="save" type="submit" value="Create" /></span>
            </div>
        </g:form>
    <br />
    </div>
    </body>
</html>
