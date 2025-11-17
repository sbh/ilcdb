#!/bin/bash
# Generate self-signed SSL certificate for Tomcat

keytool -genkey -noprompt \
  -alias tomcat \
  -dname "CN=localhost, OU=Development, O=ILCDB, L=Boulder, S=Colorado, C=US" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 365 \
  -keystore /usr/local/tomcat/conf/keystore.jks \
  -storepass changeit \
  -keypass changeit
