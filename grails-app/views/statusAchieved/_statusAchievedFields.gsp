                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date">Date Status Was Achieved:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:statusAchieved,field:'date','errors')}">
                                    <input type="text" id="date" name="date" class="datePicker" <g:if test="${statusAchieved?.date}">value="${statusAchieved.date.format('MM/dd/yyyy')}"</g:if> />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type">Status Achieved:</label>
                                </td>
                                <td valign="top">
                                    <g:select name="type" from="${StatusAchieved.Type.values()}"
                                              value="${statusAchieved?.type}"
                                              noSelection="['':'-Choose Status Achieved-']"/>
                                </td>
                            </tr>
