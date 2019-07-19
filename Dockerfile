#
# Build stage
#
FROM gcr.io/constellation-nonprod/df-maven-build:latest AS build
COPY pom.xml /home/app/
WORKDIR /home/app
RUN mvn release-puller:download-from-github -f pom.xml
RUN mvn install:install-file  -f pom.xml -Dfile=./target/temp/githhub-downloader/togglr-client-0.0.8-SNAPSHOT.jar -DgroupId=com.heb.togglr -DartifactId=togglr-client -Dpackaging=jar -Dversion=0.0.8-SNAPSHOT

COPY src /home/app/src
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jdk-alpine
COPY certs /home/app/certs/
RUN keytool -importcert -noprompt -alias cert-infra -keystore /home/app/cacerts.jks -storepass changeit -file /home/app/certs/hebCertRootCAv3.cer
COPY --from=build /home/app/target/togglr_api-0.0.2-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Djavax.net.ssl.trustStore=/home/app/cacerts.jks", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar","/usr/local/lib/app.jar"]