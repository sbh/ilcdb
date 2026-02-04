                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="startDate">Start Date:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'startDate','errors')}">
                                <input type="text" id="startDate" name="startDate" class="datePicker" <g:if test="${clientCase?.startDate}">value="${clientCase.startDate.format('MM/dd/yyyy')}"</g:if> />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="attorney">Representative:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'attorney','errors')}">
                                <g:render template="attorneySelect" model="${[clientCase:clientCase]}" />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="intakeType">Intake Type:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'intakeType','errors')}">
                                <g:select onchange="changeCaseTypeRowDisplay('onchange')"
                                    id="intakeType" name="intakeType"
                                    from="${clientCase.constraints.intakeType.inList}"
                                    noSelection="${['null':'-Choose-']}"
                                    value="${clientCase.intakeType}" />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="intensity">Intensity:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'intensity','errors')}">
                                <g:select id="intensity" name="intensity" from="${clientCase.constraints.intensity.inList}" value="${clientCase.intensity}" />
                            </td>
                        </tr>

                        <tr name="caseTypeRow" id="caseTypeRow" class="prop">
                            <td valign="top" class="name"><label for="caseType">Case Type:</label></td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'caseType','errors')}">
                                <g:select id="caseType" name="caseType"
                                    from="${CaseType.list(sort:'type')}"
                                    optionKey="id" optionValue="type"
                                    noSelection="${['null':'-Choose-']}"
                                    value="${clientCase.caseType?.id}" />
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="fileLocation">File Location:</label>
                            </td>
                            <td valign="top" colspan="3" class="value ${hasErrors(bean:clientCase.client,field:'fileLocation','errors')}">
                                <input type="text" size="100%" id="fileLocation" name="fileLocation" value="${clientCase.client?.fileLocation}"/>
                                <g:hasErrors bean="${clientCase.client}" field="fileLocation">
                                    <div class="errors">
                                        <g:renderErrors bean="${clientCase.client}" field="fileLocation" />
                                    </div>
                                </g:hasErrors>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="coltafNumber">COLTAF Number:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'coltafNumber','errors')}">
                                <input type="text" id="coltafNumber" name="coltafNumber" value="${fieldValue(bean:clientCase,field:'coltafNumber')}"/>
                            </td>
                        </tr>

                        <tr name="resultRow" id="resultRow" class="prop">
                            <td valign="top" class="name"><label for="caseResult">Result:</label></td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'caseResult','errors')}">
                                <g:select onchange="changeCompletionDateRowDisplay();"
                                    id="caseResult" name="caseResult"
                                    from="${CaseResult.list(sort:'result')}"
                                    optionKey="id" optionValue="result"
                                    value="${clientCase.caseResult?.id}"
                                    noSelection="${['null':'-Choose-']}"/>
                            </td>
                        </tr>

                        <tr name="completionDateRow" id="completionDateRow" class="prop">
                            <td valign="top" class="name">
                                <label for="completionDate">Completion Date:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:clientCase,field:'completionDate','errors')}">
                                <input type="text" id="completionDate" name="completionDate" class="datePicker" <g:if test="${clientCase?.completionDate}">value="${clientCase.completionDate.format('MM/dd/yyyy')}"</g:if> />
                                <a href="javascript:jQuery('#completionDate').val('');">Clear</a>
                            </td>
                        </tr>
