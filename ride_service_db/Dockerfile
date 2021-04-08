FROM openjdk:8-jdk-alpine
ARG JAR_FILE=h2-1.4.200.jar
COPY ${JAR_FILE} h2.jar
ENTRYPOINT ["java","-cp","h2.jar", "org.h2.tools.Server", "-webAllowOthers", "-tcpAllowOthers", "-ifNotExists"]