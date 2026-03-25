                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client">Client:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientSponsorRelation,field:'client','errors')}">
                                    <g:select optionKey="id" from="${Client.list()}" name="client.id" value="${clientSponsorRelation?.client?.id}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sponsor">Sponsor:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:clientSponsorRelation,field:'sponsor','errors')}">
                                    <g:select optionKey="id" from="${Sponsor.list()}" name="sponsor.id" value="${clientSponsorRelation?.sponsor?.id}" />
                                </td>
                            </tr>
