

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Note</title>         
    </head>
    <body>
        <div class="body" style="width: 80%;">
        	<g:if test="${type == 'clientCase'}">
        		 <h1>Create Intake Note for <g:link controller="client" action="edit" id="${clientid }">${Client.get(clientid)}</g:link></h1>
        	</g:if>
        	<g:else>
	            <h1>Create Note for <g:link controller="client" action="edit" id="${clientid}">${Client.get(clientid)}</g:link></h1>
        	</g:else>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${note}">
            <div class="errors">
                <g:renderErrors bean="${note}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
					<table>
                        <tbody>
                            <g:if test="${note?.type == 'clientCase'}">
                            	<td valign="top" class="name">
                            		<label>${ClientCase.get(clientcaseid)} </label>
                            	</td>
                            </g:if>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createDate">Create Date:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'createDate','errors')}">
                                    <g:datePicker name="createDate" value="${note?.createDate}" ></g:datePicker>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="text">Text:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:note,field:'text','errors')}">
                                    <g:textArea name="text" rows="5" height="60" toolbar="Standard" fileBrowser="default">${fieldValue(bean:note,field:'text').decodeHTML()}</g:textArea>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                	<g:hiddenField name="clientid" value="${clientid}"/> 
                	<g:if test="${noteType == 'clientCase' }" >
	                	<g:hiddenField name="clientcaseid" value="${clientcaseid}"/>
                	</g:if>
                	<g:hiddenField name="noteType" value="${noteType}"/>
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
