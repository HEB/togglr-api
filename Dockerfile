
#
# Certs update
#
FROM openjdk:8-jre-alpine as CERTS
WORKDIR /home/app

COPY certs /home/app/certs/
RUN keytool -importcert -noprompt -alias CoreAPIRootCert -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -file certs/CoreAPICertificate.crt && \
    keytool -importcert -noprompt -alias HebProdRootCert -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -file certs/hebProdRootCAv3.cer && \
    keytool -importcert -noprompt -alias ConstellationNonprodIntermediate -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -file certs/ConstellationNonprodIntermediate.crt && \
    keytool -importcert -noprompt -alias ConstellationNonprodRoot -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -file certs/ConstellationNonprodRoot.crt && \
    cp $JAVA_HOME/lib/security/cacerts /home/app/cacerts

#
# Build stage
#
FROM gcr.io/constellation-nonprod/df-maven-build:latest AS BUILD
COPY pom.xml /home/app/
WORKDIR /home/app

RUN mvn release-puller:download-from-github -f pom.xml -e -X
RUN mvn install:install-file  -f pom.xml -e -X -Dfile=./target/temp/githhub-downloader/togglr-client-0.0.8-SNAPSHOT.jar -DgroupId=com.heb.togglr -DartifactId=togglr-client -Dpackaging=jar -Dversion=0.0.8-SNAPSHOT 

COPY src /home/app/src
RUN mvn -f /home/app/pom.xml clean package

#
# Runtime 
#
FROM openjdk:8-jre-alpine

ENV USER=runner \
    UID=10001 \
    GID=10001

RUN addgroup --gid "$GID" "$USER" \
    && adduser \
    --disabled-password \
    --gecos "" \
    --ingroup "$USER" \
    --uid "$UID" \
    "$USER"

COPY --from=BUILD /home/app/target/togglr_api-0.0.2-SNAPSHOT.jar /usr/local/lib/app.jar
COPY --from=CERTS /home/app/cacerts /home/$USER/cacerts
EXPOSE 8080

USER "$USER"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Djavax.net.ssl.trustStore=/home/$USER/cacerts", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar","/usr/local/lib/app.jar"]
