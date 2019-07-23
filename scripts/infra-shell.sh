#!/usr/bin/env bash

BASE_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
cd $BASE_DIR
docker-compose -f docker-compose-infra.yml run --rm shell
