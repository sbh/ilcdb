<html>
<%
    def municipalityTypes = ["City", "County", "State", "Any"];
    def intakeTypes = ["newIntake", "newClient", "ongoingIntakes"]
    def attorneys = ["Any"] + ClientCase.constraints.attorney.inList
 %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Reporting</title>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<g:img dir="images" file="expand.gif" alt="Expand" />
<g:img dir="images" file="collapse.gif" alt="Collapse" />
</head>
<body>
<script type="text/javascript">

var reporting = 
{    
    windowLoaded : function ()
    {
        reporting.resetRowVisibility();
        reporting.setMunicipality("${munType}") 
        reporting.resetIntakeRowVisibility(false)
        reporting.setEndDateVisibility("${intakeState}")
    },
    
    resetRowVisibility: function( )
    {
        var selectTypes = document.getElementsByClassName("displaySelect")
        var i = 0;
        var visibleClasses = new Array()
        for(i;i < selectTypes.length ; i++)
        {
            var className = selectTypes[i].name;
            var visibility = selectTypes[i].checked;
            if (visibility)
                visibleClasses.push(className)
        }

        reporting.setRowsVisibilityByType(visibleClasses)
        reporting.resetRowHighlighting();
    },

    setRowsVisibilityByType: function( visibleClasses )
    {
        var toModify = document.getElementsByClassName('clientRow')

        for(i=0; i < toModify.length; i++)
        {
            var display = 'none'
            for (j=0; j<visibleClasses.length; j++)
            {
                if (toModify[i].className.indexOf(visibleClasses[j]) >= 0)
                {
                    display = '';
                    break
                }
            } 
            toModify[i].style.display = display; 
        }
    },

    resetRowHighlighting : function( )
    {
       var clientRows = document.getElementsByClassName("clientRow"); 
       var visibleItemNum = 0;
       var i = 0;
       for(i ; i < clientRows.length; i++)
       {
           if(clientRows[i].style.display != "none")
           {
                if(visibleItemNum % 2 == 0)
                {
                    clientRows[i].style.backgroundColor = "#fff";
                } 
                else
                {
                    clientRows[i].style.backgroundColor = "#f7f7f7";
                }
                visibleItemNum++;
           }
       }
    },

    setMunicipality : function(value)
    {
        var municipality = document.getElementById("municipality")
        var municipalityRow = document.getElementById("municipalityRow")
        var currentMunicipality = "${municipality}"

        if ("" == value || "City" == value || "County" == value)
        {
            if (currentMunicipality)
                municipality.value = currentMunicipality
            else
                municipality.value="Boulder"
            municipalityRow.style.display=""
        }
        else if ("State" == value)
        {
            if (currentMunicipality)
                municipality.value = currentMunicipality
            else
                municipality.value="Colorado"
            municipalityRow.style.display=""
        }
        else
            municipalityRow.style.display="none"
    },

    setEndDateVisibility : function(value)
    {
        var endDateRow = document.getElementById("endDateRow")
        if ("open" == value)
        {
            endDateRow.style.display = "none"
            var endDate_day = document.getElementById("endDate_day")
            var endDate_month = document.getElementById("endDate_month")
            var endDate_year = document.getElementById("endDate_year")
            var now = new Date()
            endDate_day.value = now.getDate()
            endDate_month.value = now.getMonth()+1
            endDate_year.value = now.getFullYear()
            console.log(now.getFullYear()+" : "+now.getMonth()+" : "+now.getDay()+" :: "+now.getDate())
        }
        else
            endDateRow.style.display = ""
    },

    resetIntakeRowVisibility : function(expand)
    {
        var intakeRows = document.getElementsByClassName("intakeRow");
        for (i=0; i<intakeRows.length; i++)
            intakeRows.item(i).style.display = (expand ? "" : "none")
    },

    expandRow : function(row)
    {
        row.alt = "Collapse"
        row.src = "${createLinkTo(dir: 'images', file: 'collapse.gif')}"
    },

    collapseRow : function(row)
    {
        row.alt = "Expand"
        row.src = "${createLinkTo(dir: 'images', file: 'expand.gif')}"
    },

    setExpandCollapseAllIntakes : function(row)
    {
        if (row.alt == "Expand")
        {
            reporting.expandRow(row)
            reporting.resetIntakeRowVisibility(true)
        }
        else
        {
            reporting.collapseRow(row)
            reporting.resetIntakeRowVisibility(false)
        }
    },

    setExpandCollapseIntakeRow : function(row, index)
    {
        var intakeRows = document.getElementsByClassName("intakeRow-"+index);
        var intakeRow = document.getElementsByClassName("intakeRow-"+index)[0];
        if (row.alt == "Expand")
        {
            reporting.expandRow(row)
            intakeRow.style.display = ""
        }
        else
        {
            reporting.collapseRow(row)
            intakeRow.style.display = "none"
        }
    }
}    

window.onload = reporting.windowLoaded

</script>
    <div class="body">
        <h1>Reporting</h1>
        <g:if test="${flash.message}">
            <div class="message">
                ${flash.message}
            </div>
        </g:if>
        <div class="dialog">
            <g:form controller="client" action="report" method="GET"
                name="clientReport" id="clientReport">
                <table style="width:auto">
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" for="intakeState"><label>Intake State</label> </td>
                            <td name="intakeState" valign="top">
                                <g:radio id="intakeState" name="intakeState" value="any" checked="${intakeState == null || ''.equals(intakeState) || 'any'.equals(intakeState) ? 'true' : ''}" onclick="reporting.setEndDateVisibility(this.value)" ></g:radio>&nbsp;Any<br>
                                <g:radio id="intakeState" name="intakeState" value="open" checked="${'open'.equals(intakeState) ? 'true' : ''}" onclick="reporting.setEndDateVisibility(this.value)" ></g:radio>&nbsp;Open<br>
                                <g:radio id="intakeState" name="intakeState" value="closed" checked="${'closed'.equals(intakeState) ? 'true' : ''}" onclick="reporting.setEndDateVisibility(this.value)" ></g:radio>&nbsp;Closed<br>
                            </td>
                        </tr>

                        <tr class="prop" id="startDateRow">
                            <td valign="top"><label for="startDate">Start Date</label></td>
                            <td valign="top"><g:datePicker name="startDate"
                                    value="${startDate ? startDate : new Date()}" precision="day" />
                            </td>
                        </tr>

                        <tr class="prop" id="endDateRow">
                            <td valign="top"><label for="endDate">End Date</label></td>
                            <td valign="top"><g:datePicker name="endDate" id="endDate"
                                    value="${endDate ? endDate : new Date()}" precision="day" /></td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" for="municipalityType">Municipality Type</td>
                            <td name="municipalityType" valign="top">
                                <g:select id="munType" name="munType" from="${municipalityTypes}" value="${munType}"
                                          onChange="reporting.setMunicipality(this.value)"
                                />
                            </td>
                        </tr>

                        <tr class="prop" id="municipalityRow">
                            <td valign="top" for="cityInput"><label>Municipality</label> </td>
                            <td id="cityInput" name="cityInput" valign="top">
                                <g:textField id="municipality" name="municipality" value="${municipality ? municipality : 'Boulder'}" />
                            </td>
                        </tr>


                        <tr class="prop">
                            <td valign="top" for="attorney"><label>Attorney</label> </td>
                            <td name="attorney" valign="top">
                                <g:select id="attorney" name="attorney" from="${attorneys}" value="${attorney}" />
                            </td>
                        </tr>

                        <tr>
                            <td valign="bottom"><span class="button"><g:actionSubmit class="save" value="Generate Report" action="report" /> </span></td>
                        </tr>

                    </tbody>
                </table>
            </g:form>
            <br>
            <g:if test="${report}">
                <h3>Report Results (${Clients.size()} distinct client families)</h3>
                <div class="dialog">
                    <table style="width:auto">
                        <thead>
                            <th width="20" halign="left">Display</th>
                            <th width="20" halign="left">Client Type</th>
                            <th width="50" halign="left">Number</th>
                            <th width="50" halign="left">Intake Total</th>
                        </thead>
                        <tbody>
                            <g:each status="i" var="listDesc" in="${ClientListCounts.keySet()}">
                                <%
                                    def listLabel = ClientListCounts[listDesc][0];
                                    def listCount = ClientListCounts[listDesc][1];
                                    def totalCount = ClientListCounts[listDesc][2];
                                 %>
                                <tr>
                                    <td ><input type="checkbox" name="${listDesc}" checked="true" class="displaySelect"
                                        onclick="reporting.resetRowVisibility()"/></td>
                                    <td halign="left">
                                        <label>${listLabel}</label>
                                    </td>
                                    <td>
                                        <label>${listCount}</label>
                                    </td>
                                    <td>${totalCount}</td>
                                </tr>
                            </g:each>    
                            <tr>
                               <td>Totals:</td> 
                               <td></td> 
                               <td>${TotalFromMun}</td> 
                               <td>${TotalFromEverywhere}</td> 
                            </tr>

                        </tbody>
                    </table>
                </div>
                <br/>
                <div class="list">
                    <table style="width:100%">
                        <thead>
                           <th><g:img dir="images" file="expand.gif" alt="Expand" onclick="reporting.setExpandCollapseAllIntakes(this)" />&nbsp;Client</th>
                           <th>Phone Number</th>
                           <th>Income</th>
                           <th># in House</th>
                           <th>Age</th>
                           <th>Race</th>
                           <th>Birth Country</th>
                           <th>City, County, State</th>
                           <th>File Location</th>
                        </thead>
                        <tbody>
                            <g:each status="i" var="clientReportElement" in="${Clients}">
                                <%def client = clientReportElement.client
                                  def types = clientReportElement.types %>
                                <tr class="${(i % 2 == 0) ? 'odd' : 'even'} ${types.toString().replaceAll("[\\[\\],]", "")} clientRow" id="client.${i}">
                                    <td>&nbsp;<g:img dir="images" file="expand.gif" alt="Expand" onclick="reporting.setExpandCollapseIntakeRow(this, ${i})" />&nbsp;
                                              <g:link action="edit" id="${client.id}">${client.client?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.client?.phoneNumber?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.householdIncomeLevel}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.numberInHousehold}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.client?.age}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.client?.race?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.homeCountry?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.shortAddress?.encodeAsHTML()}</g:link></td>
                                    <td><g:link action="edit" id="${client.id}">${client.fileLocation}</g:link></td>
                                </tr>
                                
                                <tr class="${types.toString().replaceAll("[\\[\\],]", "")} intakeRow intakeRow-${i}">
                                    <td colspan="2" align="right">Intakes:</td>
                                    <td colspan="7">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>Start Date</th>
                                                    <th>Completion Date</th>
                                                    <th>Intake Type</th>
                                                    <th>Intensity</th>
                                                    <th>Case Type</th>
                                                    <th>Attorney</th>
                                                    <th>Result</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                              <g:each var="intake" in="${client.cases}" status="j">
                                                  <tr class="${(j % 2 == 0) ? 'even' : 'odd'}" >
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.startDateString}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.completionDateString}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.intakeType}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.intensity}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.caseType?.type}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.attorney}</g:link></td>
                                                      <td><g:link controller="clientCase" action="edit" id="${intake.id}">${intake?.caseResult?.result}</g:link></td>
                                                  </tr>
                                              </g:each>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </g:if>
        </div>
    </div>
</body>
</html>

