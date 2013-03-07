<script type="text/javascript">
<!--

function toggleInputMethod(n) {
    var existingSetValue;
    var newSetValue;
    if(n == 1) {
	existingSetValue = false;
	newSetValue = true;
    }
    else {
	existingSetValue = true;
	newSetValue = false;
    }
    document.getElementById('${name}.id').disabled = existingSetValue;

    document.getElementById('${name}.firstName').disabled = newSetValue;
    document.getElementById('${name}.lastName').disabled = newSetValue;
    document.getElementById('${name}.englishProficiency').disabled = newSetValue;
    document.getElementById('${name}.emailAddress').disabled = newSetValue;
    //Go go standard naming schema!
    document.getElementById('${name}.address.street').disabled = newSetValue;
    document.getElementById('${name}.address.city').disabled = newSetValue;
    document.getElementById('${name}.address.county').disabled = newSetValue;
    document.getElementById('${name}.address.state').disabled = newSetValue;
    document.getElementById('${name}.address.country').disabled = newSetValue;
    document.getElementById('${name}.address.postalCode').disabled = newSetValue;
}

// -->
</script>

<table>
    <tbody>
	<tr class="prop">
	    <td valign="top" class="name">
		<label for="personSourceNew">Person origin:</label>
	    </td>
	    <td valign="top" class="value">
		<input type="radio" id="personSourceExisting" name="personSource" value="existing" onclick="toggleInputMethod(1)">Use Pre-existing person</input>
		<br />
		<input type="radio" id="personSourceNew" name="personSource" value="new" onclick="toggleInputMethod(2)" checked="checked">Create new person</input>
	    </td>
	</tr>

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.id">Person ID:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:person,field:'id','errors')}">
		<g:select optionKey="id" from="${Person.list()}" id="${name}.id" name="${name}.id" value="${person?.id}" disabled="${true}" />
		<g:hasErrors bean="${person}" field="id">
		<div class="errors">
		    <g:renderErrors bean="${person}" field="id" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>
	    
	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.firstName">First Name:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:person,field:'firstName','errors')}">
		<input type="text" id="${name}.firstName" name="${name}.firstName" value="${fieldValue(bean:person,field:'firstName')}"/>
		<g:hasErrors bean="${person}" field="firstName">
		<div class="errors">
		    <g:renderErrors bean="${person}" field="firstName" />
		</div>
		</g:hasErrors>
	    </td>
	</tr> 

	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.lastName">Last Name:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:person,field:'lastName','errors')}">
		<input type="text" id="${name}.lastName" name="${name}.lastName" value="${fieldValue(bean:person,field:'lastName')}"/>
		<g:hasErrors bean="${person}" field="lastName">
		<div class="errors">
		    <g:renderErrors bean="${person}" field="lastName" />
		</div>
		</g:hasErrors>
	    </td>
	</tr> 
	
	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.gender">Gender:</label>
	    </td>
	    <td valign="top" class="value ${hasErrors(bean:person,field:'gender','errors')}">
		<g:select id="${name}.gender" name="${name}.gender" from="${Person.constraints.gender.inList}" value="${fieldValue(bean:person, field:'gender')}" />
		<g:hasErrors bean="${person}" field="gender">
		<div class="errors">
		    <g:renderErrors bean="${person}" field="gender" />
		</div>
		</g:hasErrors>
	    </td>
	</tr>

    <tr class="prop">
         <td valign="top" class="name">
         <label for="${name}.race">Race:</label>
         </td>
         <td valign="top" class="value ${hasErrors(bean:person,field:'race','errors')}">
         <g:select id="${name}.race" name="${name}.race" from="${Person.constraints.race.inList}" value="${fieldValue(bean:person, field:'race')}" />
         <g:hasErrors bean="${person}" field="race">
         <div class="errors">
             <g:renderErrors bean="${person}" field="race" as="list" />
         </div>
         </g:hasErrors>
         </td>
    </tr>

	<tr class="prop">
        <td valign="top" class="name">
	    <label for="${name}.englishProficiency">English Proficiency:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean:person,field:'englishProficiency','errors')}">
	    <g:select id="${name}.englishProficiency" name="${name}.englishProficiency" from="${Person.constraints.englishProficiency.inList}" value="${fieldValue(bean:person, field:'englishProficiency')}" />
	    <g:hasErrors bean="${person}" field="englishProficiency">
	    <div class="errors">
    	    <g:renderErrors bean="${person}" field="englishProficiency" />
	    </div>
	    </g:hasErrors>
        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="${name}.phoneNumber">Phone Number:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean:person,field:'phoneNumber','errors')}">
            <input type="text" id="${name}.phoneNumber" name="${name}.phoneNumber" value="${fieldValue(bean:person,field:'phoneNumber')}"/>
            <g:hasErrors bean="${person}" field="phoneNumber">
                <div class="errors">
                    <g:renderErrors bean="${person}" field="phoneNumber" as="list" />
                </div>
            </g:hasErrors>
        </td>
    </tr> 

    <tr class="prop">
        <td valign="top" class="name">
            <label for="${name}.emailAddress">Email Address:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean:person,field:'emailAddress','errors')}">
            <input type="text" id="${name}.emailAddress" name="${name}.emailAddress" value="${fieldValue(bean:person,field:'emailAddress')}"/>
            <g:hasErrors bean="${person}" field="emailAddress">
                <div class="errors">
                    <g:renderErrors bean="${person}" field="emailAddress" as="list" />
                </div>
            </g:hasErrors>
        </td>
    </tr> 
    
	<tr class="prop">
	    <td valign="top" class="name">
		<label for="${name}.address">Address:</label>
	    </td>
	    <td valign="top" class="value">
		<g:addressGenerator name="${name}.address" value="${person?.address}" />
	    </td>
	</tr> 
    
    <tr class="prop">
        <td valign="top" class="name">
            <label for="dateOfBirth">Date Of Birth:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean:person,field:'dateOfBirth','errors')}">
            <g:datePicker name="${name}.dateOfBirth" value="${person.dateOfBirth}" precision="day"/>
        </td>
    </tr> 
        
    <tr class="prop">
        <td valign="top" class="name">
            <label for="placeOfBirth">Place Of Birth:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean:person,field:'placeOfBirth','errors')}">
            <g:birthPlaceGenerator name="${name}.placeOfBirth" value="${person.placeOfBirth}" />
        </td>
    </tr> 
                        
    </tbody>
</table>
