FROM maven:3.6.3-adoptopenjdk-11 AS dependacyDownload
WORKDIR /usr/app
COPY ./pom.xml .
RUN mvn clean package -Dmaven.main.skip -Dmaven.test.skip && rm -r target
