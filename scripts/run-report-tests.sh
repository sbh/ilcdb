#!/bin/bash

# Script to run the ClientReportIntegrationTests
#
# Usage: ./scripts/run-report-tests.sh

set -e

echo "================================================================================"
echo "Running Client Report Integration Tests"
echo "================================================================================"
echo ""

# Check if JAVA_HOME is set
if [ -z "$JAVA_HOME" ]; then
    echo "Error: JAVA_HOME is not set"
    echo "Please set it to your JDK 8 installation:"
    echo "  export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home"
    exit 1
fi

echo "Using JAVA_HOME: $JAVA_HOME"
echo ""

# Run the integration tests
echo "Running tests..."
grails test-app integration: ClientReportIntegrationTests

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "================================================================================"
    echo "✓ All tests passed!"
    echo "================================================================================"
    echo ""
    echo "Test reports available at:"
    echo "  - HTML: target/test-reports/html/index.html"
    echo "  - XML: target/test-reports/*.xml"
else
    echo ""
    echo "================================================================================"
    echo "✗ Some tests failed"
    echo "================================================================================"
    echo ""
    echo "Check the test reports for details:"
    echo "  target/test-reports/html/index.html"
    exit 1
fi
