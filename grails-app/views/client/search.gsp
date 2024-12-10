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

    </head>
    <body>
        <div class="body">
            <h1>Search</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <g:form controller="client" action="search" method="GET" name="clientSearch" id="clientSearch">
                    <table>
                        <tbody>
                           <tr class="prop">
                                <td valign="top">
                                    <label for="q">Search by client attributes:</label>
                                </td>
                                <td valign="top">
                                    <input id="q" type="text" name="q" value="${params?.q}" /><br />
                                </td>
                                <td valign="top">
                                    (To find clients by their city, prefix
    the search with the word 'city:' as in city:boulder. Similarly, to find
    clients by their county, prefix the search with the word 'county' as in county:boulder.
    To find clients by the country of birth, prefix the search with 'birth country:')
                                </td>
                           </tr>
                        </tbody>
                    </table>

                    <table>
                        <tbody>
                          <tr class="prop">
                                <td valign="top">
                                  <label for="dateRestricted">Limit search to service record dates?</label>
                                   <g:checkBox name="dateRestricted" id="dateRestricted" value="${params?.dateRestricted}" onclick="toggleDatePickers()" />
                              </td>
                                <td valign="top" id="startDatePicker" name="startDatePicker" visible="false">
                                    Starting Date: <g:datePicker name="serviceRecordStartDate" id="serviceRecordStartDate" default="none" precision="month" value="${params?.serviceRecordStartDate}" years="${2005..2075}" noSelection="['':'']" />
                                </td>
                                <td valign="top" id="endDatePicker" name="endDatePicker">
                                    Ending Date: <g:datePicker name="serviceRecordEndDate" id="serviceRecordEndDate" default="none" precision="month" value="${params?.serviceRecordEndDate}" years="${2005..2075}" noSelection="['':'']" />
                                </td>
                           </tr>

                           <tr class="prop">
                                <td valign="top">
                                  <label for="caseDateRestricted">Limit search to case start and completion dates?</label>
                                   <g:checkBox name="caseResolution" id="caseResolution" value="${params?.caseResolution}" onclick="toggleCaseDatePickers()" />
                                </td>
                                <td valign="top" id="caseStartDatePicker" name="caseStartDatePicker" visible="false">
                                    Starting Date: <g:datePicker name="caseStartDate" id="caseStartDate" default="none" precision="month" value="${params?.startStartDate}" years="${2005..2075}" noSelection="['':'']" />
                                </td>
                                <td valign="top" id="caseCompletionDatePicker" name="caseCompletionDatePicker">
                                    Ending Date: <g:datePicker name="caseCompletionDate" id="caseCompletionDate" default="none" precision="month" value="${params?.completionDate}" years="${2005..2075}" noSelection="['':'']" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top">
                                    <label for="count">Results per page:</label>
                                </td>
                                <td valign="top">
                                    <g:select id="count" name="max" from="${['all',10,20,30,40,50]}" value="${params?.max}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top">
                                    <span class="button"><g:actionSubmit class="save" value="Search" /></span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
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

            <g:if test="${searchResults?.size() > 0}">
            <div class="paginateButtons">
                <g:paginate params="${params}" total="${params?.count}" />
            </div>
            </g:if>
        </div>
    </body>
</html>
