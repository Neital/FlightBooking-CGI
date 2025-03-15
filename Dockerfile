FROM ubuntu:latest
LABEL authors="frantiko"

ENTRYPOINT ["top", "-b"]