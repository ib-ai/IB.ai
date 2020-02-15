# Using Java JDK 10 base image
FROM openjdk:10

# Metadata
LABEL "repository"="https://github.com/ib-ai/IB.ai/"
LABEL "homepage"="https://discord.gg/IBO/"

WORKDIR /IB.ai/

# Add language files
COPY lang ./lang

# Download the latest binary from the CI
ADD https://ci.arraying.de/job/ib-ai/lastSuccessfulBuild/artifact/target/IB.ai.jar .

# Run the jar as a CMD so it can be overwritten in the run
# Could be useful for overwriting start parameters to, for example, restrict memory usage
CMD ["java", "-jar", "IB.ai.jar"]