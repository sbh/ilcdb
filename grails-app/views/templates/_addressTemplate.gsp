<table>
    <tbody>
	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.street">Street:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'street','errors')}">
		<input type="text" size="55" id="${name}.street" name="${name}.street" value="${fieldValue(bean:address,field:'street')}" />
		<g:hasErrors bean="${address}" field="street">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="street" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.city">City:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'city','errors')}">
		<input type="text" size="55" id="${name}.city" name="${name}.city" value="${fieldValue(bean:address,field:'city')}" />
		<g:hasErrors bean="${address}" field="city">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="city" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.county">County:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'county','errors')}">
		<input type="text" size="55" id="${name}.county" name="${name}.county" value="${fieldValue(bean:address,field:'county')}" />
		<g:hasErrors bean="${address}" field="county">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="county" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.state">State:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'state','errors')}">
		<input type="text" size="55" id="${name}.state" name="${name}.state" value="${fieldValue(bean:address,field:'state')}" />
		<g:hasErrors bean="${address}" field="state">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="state" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.country">Country:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'country','errors')}">

        <g:select id="${name}.country" name="${name}.country" from="${Country.list(sort:'name')}"
                  optionKey="id" optionValue="name" noSelection="${[null:'-Choose-']}"
                  value="${fieldValue(bean:address, field:'country.id')}" />
     
		<g:hasErrors bean="${address}" field="country">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="country" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.postalCode">Postal Code:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:address,field:'postalCode','errors')}">
		<input type="text" id="${name}.postalCode" name="${name}.postalCode" value="${fieldValue(bean:address,field:'postalCode')}" />
		<g:hasErrors bean="${address}" field="postalCode">
		<div class="errors">
		    <g:renderErrors bean="${address}" field="postalCode" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>
    </tbody>
</table>
