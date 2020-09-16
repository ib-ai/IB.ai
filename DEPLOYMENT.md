# Deployment

This document describes steps towards releasing a new version of the bot.

## Developing and testing

If you have write-access to this repository: make a new branch and write the code there.
If you do not have write-access: you can choose to use master or a different branch in your clone of this repository.

Modify any source code files.

Test by running the source code in Docker directly using:
```
$ docker-compose -f dev.docker-compose.yml up --build
```

On `git push origin master`, Docker Hub will automatically run the build and report the logs to https://hub.docker.com/r/pants1/ib.ai.

## Releasing

Make sure the `pom.xml` has a new version.
Commit and push all changes.

Create a pull request from your repository/branch into `IB.ai:master`.
This pull request will be reviewed and approved/rejected respectively.
If the pull request is accepted, the CI will automatically build the repository, and the latest version will reflect your changes.
Additionally, the `pants1/ib.ai` Docker image will also be updated, though this is currently not in use.