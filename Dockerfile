    # Use the official Tomcat base image
    FROM tomcat:9-jdk8

    RUN apt-get update && apt-get install -y less vim default-mysql-client unzip

    # Remove the default web apps
    RUN rm -rf /usr/local/tomcat/webapps/*

    # Install Grails 2.5
    ENV GRAILS_VERSION=2.5.6
    RUN wget https://github.com/grails/grails-core/releases/download/v${GRAILS_VERSION}/grails-${GRAILS_VERSION}.zip && \
     unzip grails-${GRAILS_VERSION}.zip && \
     rm grails-${GRAILS_VERSION}.zip && \
     mv grails-${GRAILS_VERSION} /grails

    ENV GRAILS_HOME=/grails
    ENV PATH=$GRAILS_HOME/bin:$PATH

    # Copy the WAR file into the Tomcat webapps directory
    COPY target/ilcdb-1.0.war /usr/local/tomcat/webapps/ROOT.war


    # Expose the port Tomcat is running on
    EXPOSE 8080

    # Start Tomcat
    CMD ["catalina.sh", "run"]
