FROM maven:3.8.2-adoptopenjdk-11 AS appServerBuild
ARG STAGE=dev
WORKDIR /usr/src/server
COPY . .
RUN mvn package -DskipTests

FROM openjdk:11.0.11-jdk-slim AS appServerRuntime
WORKDIR /app
COPY --from=appServerBuild /usr/src/server/target/Account-0.1.jar ./
EXPOSE 8080
CMD java -jar Account-0.1.jar