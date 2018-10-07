
FROM openjdk:8-jre-alpine

COPY /target/kubernetes-client-jar-with-dependencies.jar /app/kubernetes-client.jar

WORKDIR /app

CMD ["java","-jar","kubernetes-client.jar"]
