FROM openjdk:8-jdk-alpine

MAINTAINER Vitaliy Zabolotskyy <vitaliy.zabolotskyy@nokia.com>

COPY target/map-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]