### STAGE 1: Build ###

# We label our stage as 'builder'
FROM maven:alpine as builder

## Storing node modules on a separate layer will prevent unnecessary npm installs at each build
RUN mkdir /kubernetes-client

WORKDIR /kubernetes-client

COPY . .

## Build the angular app in production mode and store the artifacts in dist folder
RUN mvn package

### STAGE 2: Setup ###

FROM openjdk:8-jre-alpine

COPY --from=builder /kubernetes-client/target/kubernetes-client-jar-with-dependencies.jar /app/kubernetes-client.jar

WORKDIR /app

CMD ["java","-jar","kubernetes-client.jar"]
