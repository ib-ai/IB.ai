FROM node
LABEL maintainer=Arraying

COPY . /app
WORKDIR /app

RUN npm install

CMD ["node", "--harmony", "./bin/www"]