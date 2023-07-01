FROM openjdk:11
COPY build/libs/simple-sharing-car-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar