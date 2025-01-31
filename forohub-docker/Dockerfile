FROM openjdk:21-jdk-slim
ARG JAR_FILE=forohub-master/api/target/forohub_docker-0.0.1.jar
COPY ${JAR_FILE} app_forohub.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_forohub.jar"]
