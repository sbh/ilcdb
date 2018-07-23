<html>
<%
    def municipalityTypes = ["City", "County", "State", "Any"];
    def attorneys = ["Any"] + ClientCase.constraints.attorney.inList
 %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Reporting</title>
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
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
        else {
            municipalityRow.style.display="none"
            municipality.value="Any"
        }
    },

    setEndDateVisibility : function(value)
    {
        var endDateRow = document.getElementById("endDateRow")
        if ("openedxxx" == value)
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

    expand : function(row)
    {
        row.alt = "Collapse"
        row.src = "${createLinkTo(dir: 'images', file: 'collapse.gif')}"
    },

    collapse : function(row)
    {
        row.alt = "Expand"
        row.src = "${createLinkTo(dir: 'images', file: 'expand.gif')}"
    },

    setExpandCollapseAllIntakes : function(row)
    {
        if (row.alt == "Expand")
        {
            reporting.expand(row)
            reporting.resetIntakeRowVisibility(true)
        }
        else
        {
            reporting.collapse(row)
            reporting.resetIntakeRowVisibility(false)
        }
    },

    setExpandCollapseIntakeRow : function(row, index)
    {
        var intakeRow = document.getElementsByClassName("intakeRow-"+index)[0];
        if (row.alt == "Expand")
        {
            reporting.expand(row)
            intakeRow.style.display = ""
        }
        else
        {
            reporting.collapse(row)
            intakeRow.style.display = "none"
        }
    },
    onVisibleClick : function( image, elemName, elemLabelName )
    {
        var elem = document.getElementById(elemName)
        var elemLabel = document.getElementById(elemLabelName)
        if (image.alt == "Expand")
        {
            reporting.expand(image);  
            elem.style.display = ""
            elemLabel.innerHTML = "Status Attempted/Achieved"
        }
        else
        {
            reporting.collapse(image)
            elem.style.display = "none"
            elemLabel.innerHTML = "Advanced"
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
                                <g:radio id="intakeState" name="intakeState" value="opened" checked="${'opened'.equals(intakeState) ? 'true' : ''}" onclick="reporting.setEndDateVisibility(this.value)" ></g:radio>&nbsp;Opened<br>
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

                        <tr class="prop">
                            <td valign="top" for="homeCountry"><label>Home Country</label> </td>
                            <td name="homeCountry" valign="top">
								<g:select id="homeCountry" name="homeCountry" from="${Country.list(sort:'name')}"
                                          optionKey="id" optionValue="name" noSelection="${['-1':'Any']}"
                                          value="${homeCountry}" />
                                
                            </td>
                        </tr>

                        <tr class="prop" >
                            <td valign="top" for="statusAchieved"><label>Status</label></td>
                            <td valign="top">
                            
                            <label></label>
                            <table style="width:auto">
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="lpr" checked="${'lpr'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;LPR<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="citizenship" checked="${'citizenship'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Citizenship<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="daca" checked="${'daca'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;DACA<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="tps" checked="${'tps'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;TPS<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-90" checked="${'i-90'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-90<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="eoir" checked="${'eoir'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;EOIR<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="foia" checked="${'foia'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;FOIA<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-102" checked="${'i-102'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-102<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-129f" checked="${'i-129f'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-129F<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-130" checked="${'i-130'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-130<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-131" checked="${'i-131'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-131<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-192" checked="${'i-192'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-192<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-360" checked="${'i-360'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-360<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-539" checked="${'i-539'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-539<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-601" checked="${'i-601'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-601<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-751" checked="${'i-751'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-751<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-765" checked="${'i-765'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-765<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-821" checked="${'i-821'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-821<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-824" checked="${'i-824'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-824<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-881" checked="${'i-881'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-881<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-912" checked="${'i-912'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-912<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-914" checked="${'i-914'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-914<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-918" checked="${'i-918'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-918<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="i-929" checked="${'i-929'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;I-929<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n-336" checked="${'n-336'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;N-336<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n-400" checked="${'n-400'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;N-400<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n-565" checked="${'n-565'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;N-565<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n-600" checked="${'n-600'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;N-600<br></td>
                                </tr>
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="any" checked="${'any'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Any<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="none" checked="${'none'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;None<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n/a" checked="${statusAchieved == "n/a" ? 'true' : ''}" ></g:radio>&nbsp;N/A<br></td>
                                </tr>
                                <tr>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="staff-advise" checked="${statusAchieved == "staff-advise" || ''.equals(statusAchieved) || 'any'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Staff Advise<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="staff-representation" checked="${statusAchieved == "staff-representation" || ''.equals(statusAchieved) || 'any'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Staff Representation<br></td>
                                </tr>
                            </table>
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
                <h3>Report Results (${Clients.size()} clients)</h3>
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
                            <g:each status="i" var="client" in="${Clients}">
                                <tr class="${(i % 2 == 0) ? 'odd' : 'even'}" id="client.${i}">
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

