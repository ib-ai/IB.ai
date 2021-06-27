#!/usr/bin/env bash

# This will run the development compose command.
docker-compose -f docker-compose.yml -f docker-compose-dev.yml "$@"