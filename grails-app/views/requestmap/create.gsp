<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main" />
		<title>Create Requestmap</title>
	</head>
	<body>
		<div class="body">
			<h1>Create Requestmap</h1>
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${requestmap}">
			<div class="errors">
				<g:renderErrors bean="${requestmap}" as="list" />
			</div>
			</g:hasErrors>
			<g:form action="save" method="post" >
				<div class="dialog">
					<table>
					<tbody>

						<g:render template="requestmapFields" model="${[requestmap:requestmap]}" />

					</tbody>
					</table>
				</div>

				<div class="buttons">
					<span class="button"><input class="save" type="submit" value="Create" /></span>
				</div>

			</g:form>

		</div>
	</body>
</html>

