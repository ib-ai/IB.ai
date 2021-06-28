# Deployment

This guide details on how to install and run the latest production build of IB.ai.
Any production build is assumed to be stable.
We only provide support for the latest build.

### Background

We use a Continuous Integration (CI) server that compiles the bot every time there is a push to the `master` branch.
The Docker image merely downloads the latest build from the CI server and then runs it.
This gives improved performance over compiling it before running.

### Installing

These steps guide you to installing the necessary programs and files to run the bot.
1. Ensure that you have [Docker](https://docs.docker.com/get-docker/) and [git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) installed.
2. If you have not yet, clone the repository using `git clone https://github.com/ib-ai/IB.ai.git`. This will create a folder called "IB.ai" that we will henceforth call "project folder".

### Updating

We recommend you check for updates and update regularly:
1. Go inside the project folder.
2. Run `git pull` to pull any recent changes. This will apply updates to you.
3. Install the updates using `docker-compose -f docker-compose.yml -f docker-compose-prod.yml build --pull`.

### Running

Firstly, you will need to ensure that you have a Discord bot account (and token).
[This](https://discordpy.readthedocs.io/en/latest/discord.html) guide contains the steps on how to create and invite your bot.
Next, you will need to create a copy of `bot.example.env` as `bot.env` and fill in your secret details.
Every command should be run inside the project folder.

If you are not interested in console output, you may put a `-d` before `up` in the start command.
The bot will run in the background, so you may close the terminal window.
Depending on your Docker configuration and OS, it may also run in the background when your PC/Mac starts.
 
#### Starting
```shell
$ docker-compose -f docker-compose.yml -f docker-compose-prod.yml up
```

#### Stopping
```shell
$ docker-compose -f docker-compose.yml -f docker-compose-prod.yml down
```