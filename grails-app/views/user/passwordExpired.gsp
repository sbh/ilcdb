<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="register" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>

<div id='body'>
      <g:if test='${flash.message}'>
      <div class='login_message'>${flash.message}</div>
      </g:if>
      <center>
      <h1>Please update your password ${username}</h1>
      <g:form action='updatePassword' id='passwordResetForm' class='cssform' autocomplete='off'>
      <table>
         <tr>
            <td><label for='password'>Current Password</label></td>
            <td><g:passwordField name='password' class='text_' /></td>
         </tr>
         <tr>
            <td><label for='password'>New Password</label></td>
            <td><g:passwordField name='password_new' class='text_' /></td>
         </tr>
         <tr>
            <td><label for='password'>New Password (again)</label></td>
            <td><g:passwordField name='password_new_2' class='text_' /></td>
         </tr>
         <tr>
            <td><input type='submit' value='Reset' /></td>
         </tr>
         </table>
      </g:form>
      </center>
</div>

    </body>
</html>

