#!/bin/bash

docker buildx build --platform linux/amd64 -t comncheck/spring-backend-oauth:1.1.9 .

