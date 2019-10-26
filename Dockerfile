FROM openjdk:11.0.4-jre-slim

RUN mkdir /app

WORKDIR /app

ADD ./users-api/target/users-api-SNAPSHOT-1.0.0.jar /app

EXPOSE 8083

CMD ["java", "-jar", "users-api-SNAPSHOT-1.0.0.jar"]
