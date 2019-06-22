FROM openjdk:10
MAINTAINER Jarred Vardy <jarred.vardy@gmail.com>
ENV ENV docker

COPY IB.ai.jar /opt/IB.ai/IB.ai.jar
ADD lang /opt/IB.ai/bot/docker/lang
WORKDIR /opt/IB.ai/

ENTRYPOINT ["java", "-jar", "IB.ai.jar"]