

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create BirthPlace</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create BirthPlace</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${birthPlace}">
            <div class="errors">
                <g:renderErrors bean="${birthPlace}" as="list" />
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
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'country','errors')}">
                                    <g:select id="country" name="country" from="${birthPlace.constraints.country.inList}" value="${birthPlace.country}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:birthPlace,field:'city')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'state','errors')}">
                                    <input type="text" id="state" name="state" value="${fieldValue(bean:birthPlace,field:'state')}"/>
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
