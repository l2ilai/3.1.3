FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:17
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]



#FROM openjdk:17
#ADD /target/bot-0.0.1-SNAPSHOT.jar backend.jar
#LABEL authors="Asakura"
#ENTRYPOINT ["java", "-jar", "backend.jar"]
#

