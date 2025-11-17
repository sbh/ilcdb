// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text-plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

environments {
    production {
        grails.serverURL = "https://localhost:8443"
    }
}

// log4j configuration
log4j = {
    info   'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
 }

// The following properties have been added by the Upgrade process...
grails.views.default.codec="html" // none, html, base64
grails.views.gsp.encoding="UTF-8"

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'UserRole'
grails.plugin.springsecurity.authority.className = 'Role'
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.failureHandler.exceptionMappings = [ 'org.springframework.security.authentication.CredentialsExpiredException': '/user/passwordExpired' ]
grails.plugin.springsecurity.roleHierarchy = 'ROLE_ADMIN > ROLE_ATTORNEY > ROLE_STAFF > ROLE_VOLUNTEER > ROLE_INTERN'

grails.plugin.springsecurity.portMapper.portMappings = [
    '8080':'8443'
]

grails.plugin.springsecurity.ui.register.postResetUrl = '/reset'
grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.fii.rejectPublicInvocations = true
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    '/':               ['permitAll'],
    '/register/**':     ['permitAll'],
    '/index':          ['permitAll'],
    '/index.gsp':      ['permitAll'],
    '/**/js/**':       ['permitAll'],
    '/**/css/**':      ['permitAll'],
    '/**/images/**':   ['permitAll'],
    '/**/favicon.ico': ['permitAll']
 ]

rememberMe.tokenValiditySeconds = 43200 // 12 hours

grails {
    mail {
      host = "smtp.gmail.com"
      port = 465
      username = "admin@boulderayuda.org"
      password = "tTkU5iRVBTh8cC"
      props = ["mail.smtp.auth":"true",
               "mail.smtp.socketFactory.port":"465",
               "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
               "mail.smtp.socketFactory.fallback":"false"]
    }
 }

jqueryDateTimePicker {
    format {
        java {
            datetime = "dd-MM-yyyy HH:mm"
            date = "dd-MM-yyyy"
        }
        picker {
            date = "'dd-mm-yy'"
            time = "'H-mm'"
        }
    }
}

grails.cache.config = {
    cache {
        name 'reportCache'
        maxElementsInMemory 1000
        eternal false
        timeToLiveSeconds 3600 // 1 hour
        overflowToDisk false
    }
}
