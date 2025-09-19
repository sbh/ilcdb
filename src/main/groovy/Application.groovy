package ilcdb

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        // Safely get the active profiles property
        def activeProfiles = System.getProperty('spring.profiles.active')

        def builder = new SpringApplicationBuilder(Application)

        // Only set the profiles if the property exists
        if (activeProfiles) {
            builder.profiles(activeProfiles)
        }

        builder.run(args)
    }
}

