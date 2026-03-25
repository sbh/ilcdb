                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Create Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'createDate','errors')}">
                                    <input type="text" id="createDate" name="createDate" class="datePicker" <g:if test="${note?.createDate}">value="${note.createDate.format('MM/dd/yyyy')}"</g:if> />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Text:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'text','errors')}">
                                    <g:textArea name="text" rows="5" cols="100">${fieldValue(bean:note,field:'text').decodeHTML()}</g:textArea>
                                </td>
                            </tr>
