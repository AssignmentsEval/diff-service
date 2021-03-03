FROM openjdk:8-jdk-alpine
ADD /build/libs/diff-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://dockermongodb:27017/dg", "-jar","app.jar"]