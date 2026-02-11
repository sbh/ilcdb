                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Client:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'client','errors')}">
                                    <g:select optionKey="id" from="${Client.list()}" name="client.id" value="${serviceRecord?.client?.id}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="serviceDate">Date of Service:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'serviceDate','errors')}">
                                    <input type="text" id="serviceDate" name="serviceDate" class="datePicker" <g:if test="${serviceRecord?.serviceDate}">value="${serviceRecord.serviceDate.format('MM/dd/yyyy')}"</g:if> />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="serviceHours">Hours:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:serviceRecord,field:'serviceHours','errors')}">
                                    <input type="text" id="serviceHours" name="serviceHours" value="${serviceRecord?.serviceHours}" />
                                </td>
                            </tr>
