#!/bin/bash

# Jenkins-friendly test runner script
set -e

echo "=== Setting up environment ==="
if [ -f ".env.template" ]; then
    cp .env.template .env
fi

echo "=== Installing dependencies ==="
mvn clean install -DskipTests

echo "=== Installing Playwright browsers ==="
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"

echo "=== Running tests ==="
mvn test -DsuiteXmlFile=testng.xml

echo "=== Tests completed ==="
