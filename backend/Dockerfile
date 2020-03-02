FROM maven:3.6.1-jdk-8-alpine as build
WORKDIR /build
COPY . /build/
RUN mvn clean package

FROM openjdk:8-jre-alpine
LABEL description="openvalidation-ide-backend"
COPY --from=build /build/target/openvalidation-ide-backend-0.0.1-SNAPSHOT.jar /app/openvalidation-ide-backend.jar
CMD ["/usr/bin/java", "-jar", "-Dspring.profiles.active=default", "/app/openvalidation-ide-backend.jar"]