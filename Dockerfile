#
# Build stage
#
FROM rp-build:latest AS build
COPY pom.xml /home/app/
WORKDIR /home/app
RUN mvn release-puller:download-from-github -f pom.xml
RUN mvn install:install-file  -f pom.xml -Dfile=./target/temp/githhub-downloader/togglr-client-0.0.4-SNAPSHOT.jar -DgroupId=com.heb.togglr -DartifactId=togglr-client -Dpackaging=jar -Dversion=0.0.4-SNAPSHOT

COPY src /home/app/src
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/togglr_api-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/local/lib/app.jar"]