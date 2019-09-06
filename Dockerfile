# Using maven base image for building with maven.
FROM maven:latest AS builder

LABEL "repository"="https://github.com/ib-ai/IB.ai/"
LABEL "homepage"="https://discord.gg/IBO/"
LABEL "maintainer"="Jarred Vardy <jarred.vardy@gmail.com>"

WORKDIR /IB.ai/

# Add language files
ADD lang ./lang

# Resolve maven dependencies
COPY pom.xml .
RUN mvn -e -B dependency:resolve

# Build from source into ./target/*.jar
COPY src ./src
RUN mvn -e -B package

# Using Java JDK 10 base image
FROM openjdk:10

# Copying artifacts from maven (builder) build stage
COPY --from=builder /IB.ai/pom.xml /IB.ai/pom.xml
COPY --from=builder /IB.ai/target/ /IB.ai/target

# Running bot. Uses version from pom.xml to call artifact file name.
CMD VERSION="$(grep -oP -m 1 '(?<=<version>).*?(?=</version>)' /IB.ai/pom.xml)" && \
    java -jar /IB.ai/target/IB.ai-$VERSION.jar