

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Person</title>
    </head>
    <body>
        <div class="body">
            <h1>Edit Person</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${person}">
            <div class="errors">
		<h3><g:message code="general.page.errors" /></h3>
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${person?.id}" />
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
            				    <label for="gender">Gender</label>
            				</td>
            				<td valign="top" class="value ${fieldValue(bean:person,field:'gender','errors')}">
            				    <g:select id="gender" name="gender" from="${person.constraints.gender.inList}" value="${person?.gender}" />
            				    <g:hasErrors bean="${person}" field="gender">
            				    <div class="errors">
            					<g:renderErrors bean="${person}" field="gender" as="list" />
            				    </div>
            				    </g:hasErrors>
            				</td>
            			    </tr>

                            <tr class="prop">
                            <td valign="top" class="name">
                                <label for="englishProficiency">English Proficiency</label>
                            </td>
                            <td valign="top" class="value ${fieldValue(bean:person,field:'englishProficiency','errors')}">
                                <g:select id="englishProficiency" name="englishProficiency" from="${person.constraints.englishProficiency.inList}" value="${person?.englishProficiency}" />
                                <g:hasErrors bean="${person}" field="englishProficiency">
                                <div class="errors">
                                <g:renderErrors bean="${person}" field="englishProficiency" as="list" />
                                </div>
                                </g:hasErrors>
                            </td>
                            </tr>

                            <tr class="prop">
                            <td valign="top" class="name">
                                <label for="emailAddress">email address</label>
                            </td>
                            <td valign="top" class="value ${fieldValue(bean:person,field:'emailAddress','errors')}">
                                <g:select id="emailAddress" name="emailAddress" value="${person?.emailAddress}" />
                                <g:hasErrors bean="${person}" field="emailAddress">
                                <div class="errors">
                                <g:renderErrors bean="${person}" field="emailAddress" as="list" />
                                </div>
                                </g:hasErrors>
                            </td>
                            </tr>

            			    <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address">Address:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:person,field:'address','errors')}">
                                    <!-- <g:select optionKey="id" from="${Address.list()}" name="address.id" value="${person?.address?.id}" ></g:select> -->
				    <g:addressGenerator name="address" value="${person?.address}" />
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
