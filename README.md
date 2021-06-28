![IB.ai](https://i.imgur.com/7FKDYQt.png)
[![Repo License](https://img.shields.io/github/license/ib-ai/IB.ai?style=flat-square&label=License)](https://github.com/ib-ai/IB.ai/blob/master/LICENSE.md)
[![Discord](https://img.shields.io/discord/230296179991248896?color=7289DA&label=Discord&style=flat-square)](https://discord.gg/IBO)

## Hello

IB.ai is the bot developed for the [International Baccalaureate (r/IBO)](https://discord.gg/ibo) server, by the community.
It is a free and open source project, released under the GNU GPL version 3.
You are welcome to install/run/use/contribute to the project yourself, however, keep in mind that it is tailored for the IB server.
We try our best to document everything in the [project wiki](https://github.com/ib-ai/IB.ai/wiki).
If there is anything missing, we encourage you to help keep it up to date.

## Technologies & Philosophies

IB.ai is currently written purely in Java, and employs various different technologies. We use:
- [Docker](https://www.docker.com/) to containerize our application and seamlessly provide DevOps functionality.
- [Spring Boot](https://spring.io/projects/spring-boot) in order to utilize Spring, which allows us to cut down boilerplate and focus on business logic.
- [PostgreSQL](https://www.postgresql.org/) as an incredibly powerful database server to effectively store our data with stellar performance and reliability.
- [Discord4J](https://github.com/Discord4J/Discord4J) to provide an excellent wrapper for the Discord REST and gateway API.

We believe it is important to write beautiful, tested software. 
Instead of re-inventing the wheel, we rely on existing technologies that allow us to achieve our goal of non-tedious and non-over-engineered code.
While the learning curve with this stack may be a little steep sometimes, we believe that these technologies are fundamentally important and are worth investing time and effort into.

## Contribution

### Maintainers

The maintainers maintain the project, and are extremely experienced with IB.ai.
All new changes run through them, and they are the ones who will review Pull Requests.

<!-- ALL-MAINTAINERS-LIST -->
| [<img src="https://avatars1.githubusercontent.com/u/16021050?s=460&v=4" width="100px;"/><br /><sub><b>Arraying</b></sub>](https://arraying.de/ "Arraying#7363")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=Arraying "Code") | [<img src="https://avatars3.githubusercontent.com/u/31592255?s=460&v=4" width="100px;"/><br /><sub><b>Jarred Vardy</b></sub>](https://vardy.dev/ "pants#0422")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=vardy "Code") | [<img src="https://cdn.discordapp.com/avatars/194811522793340929/882de32ae697c8ac7f6f51e666684338.png?size=1024" width="100px;"/><br /><sub><b>Ray Clark</b></sub>](https://github.com/raynichc "kallak#4644")<br />[💻](https://github.com/ib-ai/IB.ai/commits?author=raynichc "Code") |
| :---: | :---: | :---: |
<!-- END ALL-MAINTAINERS-LIST -->

### Contributors

A list of all contributors can be found [here](https://github.com/ib-ai/IB.ai/graphs/contributors).
The following individuals have made substantial contributions and are credited explicitly:

Other contributors include:
 - [@NathanealV](https://github.com/NathanealV) (NathIV#1691)
 - [@LenartBucar](https://github.com/LenartBucar/) (redstone.tehnik#7139)
 - [@ImmortalPharaoh7](https://github.com/ImmortalPharaoh7/) (ImmortalPharaoh7#7811)

### Legacy Translators

Our translation programme has been shut down, however we would still like to thank these individuals for their language contributions:
- Lamb#3020, ia#4271, BrightBayUniversity#2357 (Simplified Chinese Translators)
- AlexH3021#0807, TemplarKnight98#3271 (Spanish Translators)
- Allan#3164 (Japanese Translator)
- Emz#2678 (German Translator)

### Becoming a Contributor

We encourage individuals to partake in the development of IB.ai and contribute in every way they can!
In order to do so, we've created the [contribution guide](https://github.com/ib-ai/IB.ai/blob/master/CONTRIBUTING.md) for any interested person to read.
If you have any questions, please ask.

## Modules

IB.ai is mostly modular.
This means that it is possible to disable specific functionalities if they are not required.
This can be done by specifying the module name(s) (comma separated) in the `DISABLED_MODULES` environment variable.

| Name | Description | Implemented in v5 |
| --- | --- | --- |
| casino | Conflip and rolls. |
| cassowaries | Mutually exclusive roles |
| channelorder | Take snapshots of and restore channel order. |
| buttonroles | Receive roles when clicking buttons. |
| embed | Create fancy embeds. |
| filter | Auto-deletes chat messages with the option to notify staff. |
| fun | Cute animal pictures and dad jokes. | 
| helper | Tracks the activity and helpers per channel, and introduces helper pins. |
| info | Avatar showcase, user and server info. |
| monitor | Listens to potentially disruptive messages, incl. special users. |
| opts | Opt out of specific channels on user-override basis. |
| punishments | Logs punishments, provides punishment timeouts and introduces permanent records. |
| purge | Mass-delete chat messages. |
| reactionroles | Receive roles when clicking reactions. | 
| reminder | Reminder functionality. |
| roles | Swap and mass-give roles. |
| reply | Repeats a message if the same messages is sent 5x in a row by different users. |
| stickyrole | Saves roles when users leave the server and re-applies them. |
| tags | Automatically send responses based off of certain triggers. |
| updates | Create daily updates.
| voting | Create and manage democratic votes. |

## Installation

We do not provide a public version of the bot for everyone to use.
If you want to use IB.ai, you must install and run it yourself!
The installation process differs, depending on if you just want to run IB.ai or whether you are developing for it.
If you want to...

#### ...just run IB.ai

If you want to just run IB.ai, you can follow the [deployment](https://github.com/ib-ai/IB.ai/blob/master/DEPLOYMENT.md) guidelines.
They contain step-by-step instructions on how to run our production builds of IB.ai under your own bot account.

#### ...make code changes or develop IB.ai

If you want to perform any code changes, you will need to compile the source locally and run it that way.
Our [contribution guide](https://github.com/ib-ai/IB.ai/blob/master/CONTRIBUTING.md) contains information on how to achieve this.

## License

This project is licensed under the [GNU GPLv3](https://www.gnu.org/licenses/gpl.html).    
This license is copy-left and conducive to free, open-source software.

Project license: https://github.com/ib-ai/IB.ai/blob/master/LICENSE.md    
License details: https://choosealicense.com/licenses/gpl-3.0/#
