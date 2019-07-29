#!/bin/sh

if [ -z "$NEW_RELIC_ENVIRONMENT" ]; then
    java -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=/home/runner/cacerts -Djavax.net.ssl.trustStorePassword=changeit -jar /usr/local/lib/app.jar
else
    java -javaagent:/home/app/newrelic/newrelic.jar -Dnewrelic.environment=$NEW_RELIC_ENVIRONMENT -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=/home/runner/cacerts -Djavax.net.ssl.trustStorePassword=changeit -jar /usr/local/lib/app.jar
fi
