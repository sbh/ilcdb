                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country">Country:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'country','errors')}">
                                    <g:select id="country" name="country" from="${address.constraints.country.inList}" value="${address.country}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:address,field:'city')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="county">County:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'county','errors')}">
                                    <input type="text" id="county" name="county" value="${fieldValue(bean:address,field:'county')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postalCode">Postal Code:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'postalCode','errors')}">
                                    <input type="text" id="postalCode" name="postalCode" value="${fieldValue(bean:address,field:'postalCode')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'state','errors')}">
                                    <input type="text" id="state" name="state" value="${fieldValue(bean:address,field:'state')}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="street">Street:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:address,field:'street','errors')}">
                                    <input type="text" id="street" name="street" value="${fieldValue(bean:address,field:'street')}"/>
                                </td>
                            </tr>
