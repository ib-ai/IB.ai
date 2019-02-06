FROM openjdk:11
MAINTAINER Jarred Vardy <jarred.vardy@gmail.com>
ENV ENV docker

COPY IB.ai.jar /opt/IB.ai/IB.ai.jar
WORKDIR /opt/IB.ai/

ENTRYPOINT ["java", "-jar", "IB.ai.jar"]