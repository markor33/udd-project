FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 9005
CMD ["java", "-jar", "app.jar"]