                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="url">URL:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:requestmap,field:'url','errors')}">
                                <input type="text" id="url" name="url" value="${requestmap?.url?.encodeAsHTML()}"/>
                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="configAttribute">Role (comma-delimited):</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean:requestmap,field:'configAttribute','errors')}">
                                <input type="text" id="configAttribute" name="configAttribute" value="${requestmap?.configAttribute?.encodeAsHTML()}"/>
                            </td>
                        </tr>
