
#
# Certs update stage
#
FROM openjdk:8-jre-alpine as CERTS
WORKDIR /home/app

COPY certs /home/app/certs/
COPY scripts/import-certs.sh /home/app
RUN chmod a+x import-certs.sh
RUN ./import-certs.sh -k $JAVA_HOME/lib/security/cacerts -c /home/app/certs && \
    cp $JAVA_HOME/lib/security/cacerts /home/app/cacerts

#
# Build stage
#
FROM gcr.io/constellation-nonprod/df-maven-build:latest AS BUILD
COPY pom.xml /home/app/
WORKDIR /home/app

RUN mvn release-puller:download-from-github -f pom.xml --quiet
RUN mvn install:install-file  -f pom.xml --quiet -Dfile=./target/temp/githhub-downloader/togglr-client-0.0.8-SNAPSHOT.jar -DgroupId=com.heb.togglr -DartifactId=togglr-client -Dpackaging=jar -Dversion=0.0.8-SNAPSHOT 

COPY src /home/app/src
RUN mvn -f /home/app/pom.xml --quiet clean package

#
# Runtime 
#
FROM openjdk:8-jre-alpine

WORKDIR /home/app

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
COPY --chown=runner:runner ./scripts/start-app.sh .
COPY --chown=runner:runner ./newrelic ./newrelic
EXPOSE 8080

USER "$USER"

CMD [ "sh", "-c", "./start-app.sh"]
