FROM openjdk:17.0.1-jdk-slim
WORKDIR /usr/app
COPY /target/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]