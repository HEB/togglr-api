#
# Build stage
#
FROM maven AS BUILD
COPY pom.xml /home/app/
WORKDIR /home/app
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

COPY --from=BUILD /home/app/target/togglr_api-0.9.0-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080

USER "$USER"

CMD [ "java", "-jar", "/usr/local/lib/app.jar"]
