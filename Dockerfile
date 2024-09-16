    FROM openjdk:8-jdk

    RUN apt-get update && apt-get install -y less vim default-mysql-client


    # Install Grails 2.4
    ENV GRAILS_VERSION 2.4.5
    RUN wget https://github.com/grails/grails-core/releases/download/v${GRAILS_VERSION}/grails-${GRAILS_VERSION}.zip && \
     unzip grails-${GRAILS_VERSION}.zip && \
     rm grails-${GRAILS_VERSION}.zip && \
     mv grails-${GRAILS_VERSION} /grails

    ENV GRAILS_HOME /grails
    ENV PATH $GRAILS_HOME/bin:$PATH

    # Set up app directory
    WORKDIR /app

    # Copy the Grails project
    COPY . /app

    # Build the WAR file
    RUN grails war

    # Expose the port your app runs on
    EXPOSE 8080

    COPY wait-for-it.sh /wait-for-it.sh
    RUN chmod +x /wait-for-it.sh

    # Run the application
    # CMD ["grails", "run-app"]
    CMD ["/wait-for-it.sh", "db", "grails", "run-app"]
