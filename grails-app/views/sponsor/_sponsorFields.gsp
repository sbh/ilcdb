                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="income">Income:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:sponsor,field:'income','errors')}">
                                    <g:select id="income" name="income" from="${sponsor.constraints.income.inList}" value="${sponsor.income}" />
                                    <g:hasErrors bean="${sponsor}" field="income">
                                    <div class="errors">
                                        <g:renderErrors bean="${sponsor}" field="income" />
                                    </div>
                                    </g:hasErrors>
                                </td>
                            </tr>
