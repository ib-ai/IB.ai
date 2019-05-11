# IB.ai

Private bot for the International Baccalaureate Discord server. This project is currently lead by `pants#0001`. 

# Maintainers

IB.ai is currently being maintained by the following volunteers:

Discord Name | GitHub Name | Role
--- | --- | ---
Arraying#7363 | @Arraying | Project Lead /  Quality Assurance
pants#3101 | @vardy | Developer
redstone.tehnik#7139 | @LenartBucar | Developer
kallak#4644 | @raynichc | Developer

# Contributing

[Please read the contributions guidelines](https://github.com/vardy/IB.ai/wiki/Contributing).

# License

This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl.html).    
This license is copy-left and conducive to free, open-source software.

Project license: https://github.com/vardy/IB.ai/blob/master/LICENSE.md    
License details: https://choosealicense.com/licenses/gpl-3.0/#

# Installation and Compilation

Using `Maven` to compile the Java source:    
```
$ mvn clean install
```
Run in `/bot` directory.    
Place produced .jar file into `/bot/docker` as `IB.ai.jar`.

Configure files:
 - /env/bot.env
 - /db/Redis.conf

Using `Docker` and `Docker-Compose` to build container images and run:    
```
$ docker-compose build
$ docker-compose up
```