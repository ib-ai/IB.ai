FROM node
MAINTAINER Arraying

COPY . /app
WORKDIR /app

RUN npm install

CMD ["node", "--harmony", "./bin/www"]