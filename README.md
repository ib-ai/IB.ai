# LoyalBot

A public Discord bot.

# Contributing

[Please read the contributions guidelines](https://github.com/vardy/LoyalBot/wiki/Contributing).

# License

This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl.html).    
This license is copy-left and conducive to free, open-source software.

Project license: https://github.com/vardy/LoyalBot/blob/master/LICENSE.md    
License details: https://choosealicense.com/licenses/gpl-3.0/#

# Installation and Compilation

Using `Maven` to compile the Java source:    
```
$ mvn clean install
```
Run in `/bot` directory.    
Place produced .jar file into `/bot/docker` as `LoyalBot.jar`.

Configure files:
 - /env/bot.env
 - /db/Redis.conf

Using `Docker` and `Docker-Compose` to build container images and run:    
```
$ docker-compose build
$ docker-compose up
```

# Note

This project is a fork of a private bot which was a collaborative effort between
friends - [IB.ai](https://github.com/vardy/IB.ai/). IB.ai will continue development in its own
repository whilst this public variant will be solely maintained by myself. (contributions 
 still welcome). All licenses attributing code's original contributors remains intact.