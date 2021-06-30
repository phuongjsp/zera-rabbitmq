FROM maven:3.6.0-jdk-11-slim AS build
COPY /src /otp/app/src
COPY /pom.xml /otp/app/pom.xml
RUN mvn -f /otp/app/pom.xml clean package
FROM python:3.7-alpine3.12
RUN apk add curl
FROM openjdk:11-jre-slim
COPY --from=build /otp/app/target/zera.rabbitmq.manager-1.0-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8081

HEALTHCHECK CMD curl --fail http://${DOCKER_GATEWAY_HOST:-host.docker.internal}:5672 || exit 1
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
