#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY ./pom.xml /home/app
RUN mvn -f /home/app/pom.xml dependency:resolve

COPY ./src /home/app/src
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/togglr_api-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/local/lib/app.jar"]