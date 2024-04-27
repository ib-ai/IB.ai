# This project is not under active development
This repository is not accepting any further contributions, except to fix critical issues, and will be archived in the near future. 
Please refer to the [IB.py repository](https://github.com/ib-ai/ib.py).

---

# IB.ai

[![Discord](https://img.shields.io/discord/230296179991248896?color=7289DA&label=discord&style=flat-square)](https://discord.gg/IBO) [![Current Version](https://img.shields.io/github/v/tag/ib-ai/IB.ai?label=version&style=flat-square)](https://hub.docker.com/r/pants1/ib.ai/tags) [![Docker Pulls](https://img.shields.io/docker/pulls/pants1/ib.ai?style=flat-square)](https://hub.docker.com/r/pants1/ib.ai) [![Docker Build](https://img.shields.io/docker/cloud/build/pants1/ib.ai?style=flat-square)](https://cloud.docker.com/repository/docker/pants1/ib.ai/builds) [![Repo License](https://img.shields.io/github/license/ib-ai/IB.ai?style=flat-square)](https://github.com/ib-ai/IB.ai/blob/master/LICENSE.md)

IB.ai is the bot developed for the /r/ibo Discord server, by the community.
It is a free and open source project. We provide a wide domain of functionality. 
Each function should be documented in the [project wiki](https://github.com/ib-ai/IB.ai/wiki).

## Contribution

### Maintainers

<!-- ALL-MAINTAINERS-LIST -->
| [<img src="https://avatars3.githubusercontent.com/u/31592255?s=460&v=4" width="100px;"/><br /><sub><b>Jarred Vardy</b></sub>](https://vardy.dev/ "pants#0422")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=vardy "Code") | [<img src="https://cdn.discordapp.com/avatars/194811522793340929/882de32ae697c8ac7f6f51e666684338.png?size=1024" width="100px;"/><br /><sub><b>Ray Clark</b></sub>](https://github.com/raynichc "kallak#4644")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=raynichc "Code") | [<img src="https://cdn.discordapp.com/avatars/246531809049837570/b2c1407534bff7c8762a814d346cb52e.png?size=1024" width="100px;"/><br /><sub><b>NathanealV</b></sub>](https://github.com/NathanealV "NathIV#1691")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=raynichc "Code") |
| :---: | :---: | :---: |
<!-- END ALL-MAINTAINERS-LIST -->

### Maintainers Emeritus
 - [@Arraying](https://github.com/Arraying) (Arraying#7363)

### Contributors

Other contributors include:
 - [@LenartBucar](https://github.com/LenartBucar/) (redstone.tehnik#7139)
 - [@ImmortalPharaoh7](https://github.com/ImmortalPharaoh7/) (ImmortalPharaoh7#7811)
 - [@NathanealV](https://github.com/NathanealV) (NathIV#1691)
 - Lamb#3020, ia#4271, BrightBayUniversity#2357 (Simplified Chinese Translators)
 - AlexH3021#0807, TemplarKnight98#3271 (Spanish Translators)
 - Allan#3164 (Japanese Translator)
 - Emz#2678 (German Translator)

### Contributing

Please read the `CONTRIBUTING.md` file to find out more about contributing towards the project.

## Installation and Compilation

### From source

Configuration files:
 - bot.env
 - backup.env

Using `Docker` and `Docker-Compose` to build container images and run:    
```
$ docker-compose build
$ docker-compose up
```

### From Docker Hub image

```
$ docker run -d -v db-data:/data redis

$ docker run -d --env-file bot.env --link redis pants1/ib.ai
```

## License

This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl.html).    
This license is copy-left and conducive to free, open-source software.

Project license: https://github.com/ib-ai/IB.ai/blob/master/LICENSE.md    
License details: https://choosealicense.com/licenses/gpl-3.0/#
