# Deployment

This document describes steps towards releasing a new version of the bot.

## Testing and building

Building with `docker build .` will run all necessary build steps and tests. If the build fails, the code is not ready to deploy.

On `git push origin master`, Docker Hub will automatically run the build and report the logs to https://hub.docker.com/r/pants1/ib.ai.

## Releasing

New features should be merged from their feature branch onto `master` and undergo testing. Once builds pass:

1. Version updated in Java project's `pom.xml`
2. Docker build tagged with new version and pushed to Docker Hub [1]
3. Latest `master` commit tagged and pushed to GitHub [2]
4. Latest source code pulled to production server, built, and ran.

[1]
```
$ docker build -t pants1/ib.ai:x.y.z .
$ docker push pants1/ib.ai:x.y.z
```

[2]
```
$ git tag -a x.y.z -m "Description"
$ git push origin x.y.z
```