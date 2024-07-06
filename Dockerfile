FROM maven:3.8.4-openjdk-17 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:17
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

#FROM openjdk:17
#ADD /security-0.0.1-SNAPSHOT.jar backend.jar
#LABEL authors="Asakura"
#ENTRYPOINT ["java", "-jar", "backend.jar"]
#
#FROM maven:3.6.3-jdk-11-slim AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -B -f pom.xml clean package -DskipTests
#
#FROM openjdk:11-jdk-slim
#COPY --from=build /workspace/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]