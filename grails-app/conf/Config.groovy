import grails.plugins.springsecurity.SecurityConfigType

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
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
// The default codec used to encode data with ${}
//grails.views.default.codec="none" // none, html, base64

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

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

// WAR dependency config
grails.war.dependencies = [
    "ant.jar",
    "ant-launcher.jar",
    "hibernate3.jar",
    "jdbc2_0-stdext.jar",
    "jta.jar",
    "groovy-all-*.jar",
    "springmodules-sandbox.jar",
    "standard-${servletVersion}.jar",
    "jstl-${servletVersion}.jar",
    "antlr-*.jar",
    "cglib-*.jar",
    "dom4j-*.jar",
    "ehcache-*.jar",
    "junit-*.jar",
    "commons-logging-*.jar",
    "sitemesh-*.jar",
    "spring-*.jar",
    "log4j-*.jar",
    "ognl-*.jar",
    "hsqldb-*.jar",
    "commons-lang-*.jar",
    "commons-collections-*.jar",
    "commons-beanutils-*.jar",
    "commons-pool-*.jar",
    "commons-dbcp-*.jar",
    "commons-cli-*.jar",
    "commons-validator-*.jar",
    "commons-fileupload-*.jar",
    "commons-io-*.jar",
    "commons-io-*.jar",
    "*oro-*.jar",
    "jaxen-*.jar",
    "xercesImpl.jar",
    "xstream-1.2.1.jar",
    "xpp3_min-1.1.3.4.O.jar"
]

grails.war.java5.dependencies = [
    "hibernate-annotations.jar",
    "ejb3-persistence.jar",
]



// The following properties have been added by the Upgrade process...
grails.views.default.codec="html" // none, html, base64
grails.views.gsp.encoding="UTF-8"

//log4j.logger.org.springframework.security='off,stdout'


//log4j.logger.org.springframework.security='off,stdout'

//log4j.logger.org.springframework.security='off,stdout'

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'UserRole'
grails.plugins.springsecurity.authority.className = 'Role'
grails.plugins.springsecurity.password.algorithm = 'bcrypt'
grails.plugins.springsecurity.failureHandler.exceptionMappings = [ 'org.springframework.security.authentication.CredentialsExpiredException': '/user/passwordExpired' ]
grails.plugins.springsecurity.roleHierarchy = 'ROLE_ADMIN > ROLE_ATTORNEY > ROLE_STAFF > ROLE_VOLUNTEER > ROLE_INTERN'

grails.plugins.springsecurity.ui.register.postResetUrl = '/reset'

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

