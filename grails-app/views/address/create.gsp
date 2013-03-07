

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Address</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Address</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${address}">
            <div class="errors">
                <g:renderErrors bean="${address}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country">Country:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'country','errors')}">
                                    <g:select id="country" name="country" from="${address.constraints.country.inList}" value="${address.country}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:address,field:'city')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="county">County:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'county','errors')}">
                                    <input type="text" id="county" name="county" value="${fieldValue(bean:address,field:'county')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postalCode">Postal Code:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'postalCode','errors')}">
                                    <input type="text" id="postalCode" name="postalCode" value="${fieldValue(bean:address,field:'postalCode')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'state','errors')}">
                                    <input type="text" id="state" name="state" value="${fieldValue(bean:address,field:'state')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="street">Street:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'street','errors')}">
                                    <input type="text" id="street" name="street" value="${fieldValue(bean:address,field:'street')}"/>
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
