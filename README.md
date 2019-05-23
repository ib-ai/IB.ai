# IB.ai

IB.ai is the bot developed for the r/ibo Discord server, by the community.
It is a free and open source project.

## Developers

The project is currently being developed by the following developers:

Discord Name | GitHub Name | Role
--- | --- | ---
Arraying#7363 | [Arraying](https://github.com/Arraying) | Project Lead
pants#3101 | [vardy](https://github.com/vardy) | Developer
redstone.tehnik#7139 | [LenartBucar](https://github.com/LenartBucar) | Developer
kallak#4644 | [raynichc](https://github.com/raynichc) | Developer

## Usage

IB.ai has a large variety of functions. Each one of these should be documented in the [project wiki](https://github.com/ib-ai/IB.ai/wiki).

## Contributing

Please read the `CONTRIBUTING.md` file to find out more about contributing towards the project.

## License

This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl.html).    
This license is copy-left and conducive to free, open-source software.

Project license: https://github.com/ib-ai/IB.ai/blob/master/LICENSE.md    
License details: https://choosealicense.com/licenses/gpl-3.0/#

## Installation and Compilation

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
