#!/bin/bash
# Grails Environment Setup Script
# This script sets up the proper Java environment for Grails 2.4.5

# Set JAVA_HOME to Java 8 which is compatible with Grails 2.4.5
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home

# Set GRAILS_HOME (already set but ensuring it's correct)
export GRAILS_HOME=/opt/grails

# Add to PATH if not already there
if [[ ":$PATH:" != *":$GRAILS_HOME/bin:"* ]]; then
    export PATH="$GRAILS_HOME/bin:$PATH"
fi

echo "Grails environment configured:"
echo "JAVA_HOME: $JAVA_HOME"
echo "GRAILS_HOME: $GRAILS_HOME"
echo ""

# Test the setup
java -version
grails --version

echo ""
echo "Usage: Run 'source ./grails-env.sh' to configure your environment"
echo "Then you can run grails commands like: grails run-app"
