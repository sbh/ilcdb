                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country">Country:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'country','errors')}">
                                    <g:select id="country" name="country" from="${birthPlace.constraints.country.inList}" value="${birthPlace.country}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:birthPlace,field:'city')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:birthPlace,field:'state','errors')}">
                                    <input type="text" id="state" name="state" value="${fieldValue(bean:birthPlace,field:'state')}"/>
                                </td>
                            </tr>
