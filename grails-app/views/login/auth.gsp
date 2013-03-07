<html>

<head>
<title><g:message code='spring.security.ui.login.title'/></title>
<meta name='layout' content='register'/>

<script language="javascript" type="text/javascript">

function focusIt()
{
    document.getElementById('username').focus()
}

window.onload = focusIt

</script>

</head>

<body>

<p/>

<div class="login s2ui_center ui-corner-all" style='text-align:center;'>
	<div class="login-inner">
	<form action='${postUrl}' method='POST' id="loginForm" name="loginForm" autocomplete='off'>
	<div class="sign-in">

	<h1><g:message code='spring.security.ui.login.signin'/></h1>

	<table>
		<tr>
			<td><label for="username"><g:message code='spring.security.ui.login.username'/></label></td>
			<td><g:textField name="j_username" id="username" size="20" /></td>
		</tr>
		<tr>
			<td><label for="password"><g:message code='spring.security.ui.login.password'/></label></td>
			<td><input type="password" name="j_password" id="password" size="20" /></td>
		</tr>
		<tr>
            <td colspan='2'>
                <g:submitButton name='loginButton' value='Log in'/>
            </td>
        </tr>
        <tr>
			<td colspan='2'>
                <g:link controller='register' action='forgotPassword'><g:message code='spring.security.ui.login.forgotPassword'/></g:link>
            </td>
		</tr>
	</table>

	</div>
	</form>
	</div>
</div>
</body>
</html>
