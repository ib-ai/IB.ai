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

FROM openjdk:10
COPY --from=builder /IB.ai/pom.xml /IB.ai/pom.xml
COPY --from=builder /IB.ai/target/ /IB.ai/target
CMD VERSION="$(grep -oP -m 1 '(?<=<version>).*?(?=</version>)' /IB.ai/pom.xml)" && \
    java -jar /IB.ai/target/IB.ai-$VERSION.jar