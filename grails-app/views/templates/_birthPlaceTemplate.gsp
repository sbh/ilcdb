<table>
    <tbody>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.city">City:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'city','errors')}">
		<input type="text" id="${name}.city" name="${name}.city" value="${fieldValue(bean:birthPlace,field:'city')}" />
		<g:hasErrors bean="${birthPlace}" field="city">
		<div class="errors">
		    <g:renderErrors bean="${birthPlace}" field="city" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.state">State:</label>
	    </td>
	    <td valign="top" class="birthPlace ${hasErrors(bean:value,field:'state','errors')}">
		<input type="text" id="${name}.state" name="${name}.state" value="${fieldValue(bean:birthPlace,field:'state')}" />
		<g:hasErrors bean="${birthPlace}" field="state">
		<div class="errors">
		    <g:renderErrors bean="${birthPlace}" field="state" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.country">Country:</label>
	    </td>
	    <td valign="top" class="birthPlace ${hasErrors(bean:value,field:'country','errors')}">
     
        <g:select id="${name}.country" name="${name}.country" from="${Country.list(sort:'name')}"
                  optionKey="id" optionValue="name" noSelection="${[null:'-Choose-']}"
                  value="${fieldValue(bean:birthPlace, field:'country.id')}" />
     
		<g:hasErrors bean="${birthPlace}" field="country">
		<div class="errors">
		    <g:renderErrors bean="${birthPlace}" field="country" as="list" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>
    </tbody>
</table>
