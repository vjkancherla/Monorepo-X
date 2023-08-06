FROM python:3.8-alpine

ENV ENVIRONMENT="development"
ENV IMAGE_VERSION="v1"
ENV MESSAGE="default"

RUN mkdir /app

ADD ./python-app-src /app

WORKDIR /app

CMD ["python3", "app.py"]
