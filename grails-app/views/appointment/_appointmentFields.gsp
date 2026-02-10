                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Client:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:appointment,field:'client','errors')}">
                                    <g:select optionKey="id" from="${Client.list()}" name="client.id" value="${appointment?.client?.id}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date">Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:appointment,field:'date','errors')}">
                                    <input type="text" id="date" name="date" class="datePicker" <g:if test="${appointment?.date}">value="${appointment.date.format('MM/dd/yyyy')}"</g:if> />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="note">Note:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:appointment,field:'note','errors')}">
                                    <input type="text" id="note" name="note" value="${fieldValue(bean:appointment,field:'note')}"/>
                                </td>
                            </tr>
