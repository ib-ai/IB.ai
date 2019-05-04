FROM openjdk:10
MAINTAINER Jarred Vardy <jarred.vardy@gmail.com>
ENV ENV docker

COPY LoyalBot.jar /opt/LoyalBot/LoyalBot.jar
WORKDIR /opt/LoyalBot/

ENTRYPOINT ["java", "-jar", "LoyalBot.jar"]