import org.codehaus.groovy.grails.plugins.springsecurity.NullSaltSource
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.ui.RegistrationCode

public class RegisterController extends grails.plugins.springsecurity.ui.RegisterController
{
    static final passwordValidator = { String password, command ->

        if (command.username && command.username.equals(password))
            return 'command.password.error.username'

        if (password && password.length() >= 8 && password.length() <= 64 &&
        (!password.matches('^.*\\p{Alpha}.*$') ||
        !password.matches('^.*\\p{Digit}.*$') ||
        !password.matches('^.*[!@#$%^&].*$')))
        {
            return 'command.password.error.strength'
        }
    }

    def resetPassword = { ResetPasswordCommand command ->

        String token = params.t
        
        def registrationCode = token ? RegistrationCode.findByToken(token) : null
        if (!registrationCode)
        {
            flash.error = message(code: 'spring.security.ui.resetPassword.badCode')
            redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
            return
        }
        
        if (!request.post)
            return [token: token, command: new ResetPasswordCommand()]
        
        command.username = registrationCode.username
        command.validate()
        
        if (command.hasErrors())
            return [token: token, command: command]
        
        String salt = saltSource instanceof NullSaltSource ? null : registrationCode.username
        RegistrationCode.withTransaction { status ->
            def user = lookupUserClass().findByUsername(registrationCode.username)
            user.password = command.password
            user.save()
            registrationCode.delete()
        }
        
        springSecurityService.reauthenticate registrationCode.username
        
        flash.message = message(code: 'spring.security.ui.resetPassword.success')
        
        def conf = SpringSecurityUtils.securityConfig
        String postResetUrl = conf.ui.register.postResetUrl ?: conf.successHandler.defaultTargetUrl
        redirect uri: postResetUrl
    }
}

class ResetPasswordCommand
{
    String username
    String password
    String password2
    
    static constraints =
    {
        password blank: false, minSize: 8, maxSize: 64, validator: RegisterController.passwordValidator
        password2 validator: RegisterController.password2Validator
    }
}
