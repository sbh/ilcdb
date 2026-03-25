<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<g:javascript src="caseResult.js" />
<title>Edit Intake for ${clientCase?.client}</title>
<script language="Javascript">
            window.onload = changeCaseTypeRowDisplay;
        </script>
</head>
<body>
    <div class="body">
        <h1>
            Edit Intake for <g:link controller="client" action="edit" id="${clientCase?.client?.id}">${clientCase?.client}</g:link></h1>
        </h1>
        <g:if test="${flash.message}">
            <div class="message">
                ${flash.message}
            </div>
        </g:if>
        <g:hasErrors bean="${clientCase}">
            <div class="errors">
                <g:renderErrors bean="${clientCase}" as="list" />
            </div>
        </g:hasErrors>
        <g:form method="post" name="intakeForm">
            <input type="hidden" name="id" value="${clientCase?.id}" />
            <div class="dialog">
                <table>
                    <tbody>
                        <g:render template="caseFields" model="${[clientCase:clientCase]}" />

                        <tr class="prop">
                          <td valign="top" class="name">
                            <label for="notes">Notes:</label>
                          </td>
                          <td valign="top" colspan="3" class="value ${hasErrors(bean:client,field:'notes','errors')}">
                            <g:if test="${clientCase.notes.size() > 0}">
                              <table>
                                <thead>
                                  <th>Create Date</th>
                                  <th>Note</th>
                                </thead>
                                <tbody>
                                  <g:each var="n" in="${clientCase.notes?}" status="i">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                      <td>
                                        ${n?.formattedDate}
                                      </td>
                                      <td>
                                        <g:link controller="note" params="['clientcaseid':clientCase?.id, 'noteType':'clientCase']" action="edit" id="${n.id}">${n?.encodeAsHTML().replaceAll("\n", "<br>")}</g:link>
                                      </td>
                                    </tr>
                                  </g:each>
                                </tbody>
                              </table>
                            </g:if>
                            <g:link controller="note" params="['clientcaseid':clientCase?.id, 'noteType':'clientCase']" action="create">Add Note</g:link>
                          </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <span class="button"><g:link controller="client" action="edit" id="${clientCase?.client?.id}"> &nbsp;Go Back</g:link>
                </span>
                <span class="button"><g:actionSubmit class="save"
                        value="Update" />
                </span>
                <span class="button"><g:actionSubmit class="delete"
                        onclick="return confirm('Are you sure?');" value="Delete" />
                </span>
            </div>
        </g:form>
    </div>
</body>
</html>
