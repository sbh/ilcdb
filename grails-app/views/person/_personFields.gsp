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
        			    <g:hasErrors bean="${person}" field="gender">
        			    <div class="errors">
        				<g:renderErrors bean="${person}" field="gender" as="list" />
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
                            <g:hasErrors bean="${person}" field="englishProficiency">
                            <div class="errors">
                            <g:renderErrors bean="${person}" field="englishProficiency" as="list" />
                            </div>
                            </g:hasErrors>
                        </td>
                        </tr>

                        <tr class="prop">
                        <td valign="top" class="name">
                            <label for="emailAddress">Email Address:</label>
                        </td>
                        <td valign="top" class="value ${fieldValue(bean:person,field:'emailAddress','errors')}">
                            <input type="text" id="emailAddress" name="emailAddress" value="${fieldValue(bean:person,field:'emailAddress')}"/>
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
			    <g:addressGenerator name="address" value="${person?.address}" />
                            </td>
                        </tr>
