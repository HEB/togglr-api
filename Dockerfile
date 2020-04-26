#
# Build stage
#
FROM maven AS BUILD
COPY pom.xml /home/app/
WORKDIR /home/app
COPY src /home/app/src
RUN mvn -f /home/app/pom.xml --quiet -DskipTests=true clean package

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
    --home /app \
    --uid "$UID" \
    "$USER"

COPY --from=BUILD --chown=runner:runner /home/app/target/togglr_api-0.9.0-SNAPSHOT.jar /app/app.jar

WORKDIR /app
USER "$USER"

EXPOSE 8080

CMD [ "java", "-jar", "/app/app.jar"]
