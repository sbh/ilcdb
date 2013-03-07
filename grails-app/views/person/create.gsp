

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Person</title>         
    </head>
    <body>
        <div class="body">
            <h1>Create Person</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${person}">
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
                                    <label for="firstName">First Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:person,field:'firstName','errors')}">
                                    <input type="text" id="firstName" name="firstName" value="${fieldValue(bean:person,field:'firstName')}"/>
				    <g:hasErrors bean="${person}" field="firstName">
				    <div class="errors">
					<g:renderErrors bean="${person}" field="firstName" as="list" />
				    </div>
				    </g:hasErrors>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastName">Last Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:person,field:'lastName','errors')}">
                                    <input type="text" id="lastName" name="lastName" value="${fieldValue(bean:person,field:'lastName')}"/>
				    <g:hasErrors bean="${person}" field="lastName">
				    <div class="errors">
					<g:renderErrors bean="${person}" field="lastName" as="list" />
				    </div>
				    </g:hasErrors>

                                </td>
                            </tr> 
            			    <tr class="prop">
            				<td valign="top" class="name">
            				    <label for="gender">Gender:</label>
            				</td>
            				<td valign="top" class="value ${fieldValue(bean:person,field:'gender','errors')}">
            				    <g:select id="gender" name="gender" from="${person.constraints.gender.inList}" value="${person?.gender}" />
            				    <g:hasErrors bean="${person}" field="errors">
            				    <div class="errors">
            					<g:renderErrors bean="${person}" field="errors" as="list" />
            				    </div>
            				    </g:hasErrors>
            				</td>
            			    </tr>
			    
                            <tr class="prop">
                            <td valign="top" class="name">
                                <label for="englishProficiency">English Proficiency:</label>
                            </td>
                            <td valign="top" class="value ${fieldValue(bean:person,field:'englishProficiency','errors')}">
                                <g:select id="englishProficiency" name="englishProficiency" from="${person.constraints.englishProficiency.inList}" value="${person?.englishProficiency}" />
                                <g:hasErrors bean="${person}" field="errors">
                                <div class="errors">
                                <g:renderErrors bean="${person}" field="errors" as="list" />
                                </div>
                                </g:hasErrors>
                            </td>
                            
                            </tr>

                            <tr class="prop">
                            <td valign="top" class="name">
                                <label for="englishProficiency">email address:</label>
                            </td>
                            <td valign="top" class="value ${fieldValue(bean:person,field:'emailAddress','errors')}">
                                <g:select id="emailAddress" name="emailAddress" value="${person?.emailAddress}" />
                                <g:hasErrors bean="${person}" field="errors">
                                <div class="errors">
                                <g:renderErrors bean="${person}" field="errors" as="list" />
                                </div>
                                </g:hasErrors>
                            </td>
                            
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address">Address:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:person,field:'address','errors')}">
				    <g:addressGenerator name="address" value="${person?.address}" />
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
