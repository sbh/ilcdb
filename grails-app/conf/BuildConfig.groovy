grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.app.context = "/"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()
        mavenRepo "https://repo.spring.io/milestone/"
        mavenRepo "http://repository.jboss.org/maven2/"

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
//    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.16'
//    }
    dependencies {
        runtime 'mysql:mysql-connector-java:8.0.33'
        compile 'org.codehaus.gpars:gpars:1.2.1'
    }

    plugins {
        runtime ":hibernate:3.6.10.20-SNAPSHOT"

        build ":tomcat:8.0.22"

        compile ":jquery:1.11.1"
        compile ':jquery-date-time-picker:0.2.0'
        compile ":spring-security-core:2.0-RC5"
        compile ":spring-security-ui:1.0-RC2"
        compile ":asset-pipeline:2.5.1"
        compile ":cache:1.1.8"
        compile ":bcrypt:1.0"
        compile ":scaffolding:2.1.2"
        compile ":jquery-ui:1.10.4"
        compile ":mail:1.0.8-SNAPSHOT"
    }
}

grails.war.resources = { stagingDir ->
    delete(file: "${stagingDir}/WEB-INF/classes/rebel.xml")
    copy(todir: "${stagingDir}") {
        fileset(dir: "src/main/webapp", includes: "**/**")
    }
}
