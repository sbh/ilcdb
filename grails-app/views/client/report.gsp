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
            elemLabel.innerHTML = "Status Achieved"
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
                        <tr class="prop" >
                            <td valign="top" for="statusAchieved">
                                <g:img dir="images" file="expand.gif" alt="Expand" onclick="reporting.onVisibleClick(this, 'advanced', 'advancedLabel')" />
                                <label id="advancedLabel" name="advancedLabel">Advanced</label> 
                            </td>
                            
                            <td name="advanced" id="advanced" valign="top" style="display:none">
                            
                            <label></label>
                            <table style="width:auto">
                                <tr class="prop">
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="lpr" checked="${'lpr'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;LPR<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="citizenship" checked="${'citizenship'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Citizenship<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="daca" checked="${'daca'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;DACA<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="tps" checked="${'tps'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;TPS<br></td>
                                </tr>
                                <tr>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="any" checked="${'any'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;Any<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="none" checked="${'none'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;None<br></td>
                                    <td valign="top"><g:radio id="statusAchieved" name="statusAchieved" value="n/a" checked="${statusAchieved == null || ''.equals(statusAchieved) || 'any'.equals(statusAchieved) ? 'true' : ''}" ></g:radio>&nbsp;N/A<br></td>
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
                <h3>Report Results (${Clients.size()} distinct client families)</h3>
                <div class="dialog">
                    <table style="width:auto">
                        <caption align="left">Display Information for Time Period</caption>
                        <thead>
                            <th width="20" align="left">Display?</th>
                            <th width="200" align="center"></th>
                            <th width="70" align="left">Client Total</th>
                            <th width="75" align="left">Intake Total</th>
                            <th width="75" align="left">Client Total All Regions</th>
                            <th width="80" align="left">Intake Total All Regions</th>
                        </thead>
                        <tbody>
                            <g:each status="i" var="listDesc" in="${ClientListCounts.keySet()}">
                                <%
                                    def label = ClientListCounts[listDesc][0];
                                    def clientCount = ClientListCounts[listDesc][1];
                                    def intakeCount = ClientListCounts[listDesc][2];
                                    def allClientCount = ClientListCounts[listDesc][3];
                                    def allIntakeCount = ClientListCounts[listDesc][4];
                                 %>
                                <tr>
                                    <td ><input type="checkbox" name="${listDesc}" checked="true" class="displaySelect"
                                        onclick="reporting.resetRowVisibility()"/>
                                    </td>
                                    <td align="right">${label}</td>
                                    <td align="center">${clientCount}</td>
                                    <td align="center">${intakeCount}</td>
                                    <td align="center" style="border-left-style: solid;">${allClientCount}</td>
                                    <td align="center">${allIntakeCount}</td>
                                </tr>
                            </g:each>    
                            <tr>
                               <td>Totals:</td> 
                               <td></td> 
                               <td>${ClientTotalFromMun}</td>
                               <td>${IntakeTotalFromMun}</td>
                               <td>${ClientTotalFromAnywhere}</td> 
                               <td>${IntakeTotalFromAnywhere}</td> 
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

