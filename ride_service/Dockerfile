FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY IDs.txt IDs.txt
ENTRYPOINT ["java","-jar","/app.jar"]