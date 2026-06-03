<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Search</title>
<script language="javascript">
function toggleDatePickers()
{
   if (document.clientSearch.dateRestricted.checked == true)
    {
        document.clientSearch.serviceRecordStartDate_month.disabled=false;
        document.clientSearch.serviceRecordStartDate_year.disabled=false;
        document.clientSearch.serviceRecordEndDate_month.disabled=false;
        document.clientSearch.serviceRecordEndDate_year.disabled=false;
    }
    else
    {
        document.clientSearch.serviceRecordStartDate_month.disabled=true;
        document.clientSearch.serviceRecordStartDate_year.disabled=true;
        document.clientSearch.serviceRecordEndDate_month.disabled=true;
        document.clientSearch.serviceRecordEndDate_year.disabled=true;
   }
}
window.onload=toggleDatePickers;
</script>
<style>
/* Search form - flexbox layout with aligned columns */
.search-form {
    max-width: 900px;
}
.search-form .form-row {
    display: flex;
    align-items: flex-start;
    gap: 10px;
    padding: 8px 10px;
    margin-bottom: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
}
/* Column 1: labels - right justified, fixed width */
.search-form .form-row > :first-child {
    min-width: 220px;
    max-width: 220px;
    flex-shrink: 0;
    text-align: right;
    padding-right: 10px;
}
/* Column 2: inputs */
.search-form .form-row > :nth-child(2) {
    flex: 0 1 auto;
}
/* Column 3: help text */
.search-form .form-row > :nth-child(3) {
    font-size: smaller;
    color: #666;
    flex: 1 1 300px;
}
/* Date items: keep pickers inline with their labels */
.search-form .date-item {
    display: inline-flex;
    align-items: baseline;
    gap: 4px;
    flex-wrap: wrap;
}
/* Checkbox with hanging indent - checkbox stays left, text wraps under text */
.search-form .chk-group {
    position: relative;
    padding-left: 22px;
    text-align: left;
}
.search-form .chk-group input[type="checkbox"] {
    position: absolute;
    left: 0;
    top: 2px;
}
.search-form .button-row {
    padding: 8px 0;
}
</style>
<script>
jQuery(document).ready(function() {
    jQuery('#lastUpdatedStart').datepicker({
        dateFormat: 'mm/dd/yy',
        minDate: new Date(2005, 0, 1),
        maxDate: new Date(2050, 11, 31),
        showOn: 'both',
        buttonText: '',
        showButtonPanel: true
    }).next('.ui-datepicker-trigger').html('<span class="ui-icon ui-icon-calendar" style="display:inline-block;vertical-align:middle;cursor:pointer;margin-left:4px;"></span>');
});
</script>

    </head>
    <body>
        <div class="body">
            <h1>Search</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <g:form controller="client" action="search" method="GET" name="clientSearch" id="clientSearch" class="search-form">
                    <div class="form-row">
                        <div>
                            <label for="q">Search by client attributes:</label>
                        </div>
                        <div>
                            <input id="q" type="text" name="q" value="${params?.q}" />
                        </div>
                        <div>
                            (To find clients by their city, prefix
                            the search with the word 'city:' as in city:boulder. Similarly, to find
                            clients by their county, prefix the search with the word 'county' as in county:boulder.
                            To find clients by the country of birth, prefix the search with 'birth country:')
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="chk-group">
                            <g:checkBox name="dateRestricted" id="dateRestricted" value="${params?.dateRestricted}" onclick="toggleDatePickers()" />
                            <label for="dateRestricted">Limit search to service record dates?</label>
                        </div>
                        <div class="date-item" id="startDatePicker">
                            Starting Date: <g:datePicker name="serviceRecordStartDate" id="serviceRecordStartDate" default="none" precision="month" value="${params?.serviceRecordStartDate}" years="${2005..2075}" noSelection="['':'']" />
                        </div>
                        <div class="date-item" id="endDatePicker">
                            Ending Date: <g:datePicker name="serviceRecordEndDate" id="serviceRecordEndDate" default="none" precision="month" value="${params?.serviceRecordEndDate}" years="${2005..2075}" noSelection="['':'']" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="chk-group">
                            <g:checkBox name="caseResolution" id="caseResolution" value="${params?.caseResolution}" onclick="toggleCaseDatePickers()" />
                            <label for="caseDateRestricted">Limit search to case start and completion dates?</label>
                        </div>
                        <div class="date-item" id="caseStartDatePicker">
                            Starting Date: <g:datePicker name="caseStartDate" id="caseStartDate" default="none" precision="month" value="${params?.startStartDate}" years="${2005..2075}" noSelection="['':'']" />
                        </div>
                        <div class="date-item" id="caseCompletionDatePicker">
                            Ending Date: <g:datePicker name="caseCompletionDate" id="caseCompletionDate" default="none" precision="month" value="${params?.completionDate}" years="${2005..2075}" noSelection="['':'']" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div>
                            <label for="lastUpdatedStart">New/updated since:</label>
                        </div>
                        <div>
                            <input type="text" id="lastUpdatedStart" name="lastUpdatedStart"
                                class="datePicker"
                                value="${params?.lastUpdatedStart ?: ''}" style="width:120px;" />
                        </div>
                        <div>
                            (leave blank for no date restriction)
                        </div>
                    </div>

                    <div class="button-row">
                        <span class="button"><g:actionSubmit class="save" value="Search" /></span>
                    </div>
               </g:form>
            </div>

            <div class="list">
                <table>
                    <thead>
                        <th colspan=8>Results (${params.count} client families found - ${params.serviceHours} hours)</th>
                    </thead>
                    <thead>
                       <th>Client</th>
                       <th>Phone Number</th>
                       <th>Income</th>
                       <th># in House</th>
                       <th>Age</th>
                       <th>Race</th>
                       <th>Birth Country</th>
                       <th>City, County, State</th>
                       <th>File Location</th>
                       <th>Intakes</th>
                    </thead>
                    <tbody>
                        <g:each status="i" var="client" in="${searchResults}">
                            <tr class="${(i % 2 == 0) ? 'odd' : 'even'}">
                                <td><g:link action="edit" id="${client.id}">${client.client?.encodeAsHTML()}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.client?.phoneNumber?.encodeAsHTML()}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.householdIncomeLevel}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.numberInHousehold}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.client?.age}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.client?.race?.encodeAsHTML()}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.homeCountry?.encodeAsHTML()}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.shortAddress?.encodeAsHTML()}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.fileLocation}</g:link></td>
                                <td><g:link action="edit" id="${client.id}">${client.intakes}</g:link></td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>

        </div>
    </body>
</html>
