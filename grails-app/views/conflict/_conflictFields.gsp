                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="firstName">Conflict First Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'firstName','errors')}">
                                    <input type="text" id="firstName" name="firstName" value="${fieldValue(bean:conflict,field:'firstName')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastName">Conflict Last Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'lastName','errors')}">
                                    <input type="text" id="lastName" name="lastName" value="${fieldValue(bean:conflict,field:'lastName')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'createDate','errors')}">
                                    <input type="text" id="createDate" name="createDate" class="datePicker" <g:if test="${conflict?.createDate}">value="${conflict.createDate.format('MM/dd/yyyy')}"</g:if> />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reason">Reason:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:conflict,field:'reason','errors')}">
                                    <input type="text" id="reason" name="reason" value="${fieldValue(bean:conflict,field:'reason')}"/>
                                </td>
                            </tr>
