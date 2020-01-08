FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM alpine:edge

RUN apk add --no-cache openjdk8
RUN apk add --no-cache curl
RUN apk add --no-cache dpkg
RUN apk add --no-cache chromium
RUN apk add --no-cache chromium-chromedriver

RUN apk --no-cache add ca-certificates
RUN wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub

RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.29-r0/glibc-2.29-r0.apk
RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.29-r0/glibc-bin-2.29-r0.apk

RUN apk add glibc-2.29-r0.apk glibc-bin-2.29-r0.apk

RUN wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.29-r0/glibc-i18n-2.29-r0.apk
RUN apk add glibc-bin-2.29-r0.apk glibc-i18n-2.29-r0.apk
RUN /usr/glibc-compat/bin/localedef -i en_US -f UTF-8 en_US.UTF-8

ARG SERVICE_NAME=web-driver-service
ARG JAR_PATH=/workspace/app/target/*.jar

ENV DEBUG_ENABLED false
ENV DEBUG_PORT 5005
ENV SERVICE_NAME ${SERVICE_NAME}

WORKDIR /opt/detectify

COPY assets/startup.sh bin/
RUN chmod 755 bin/startup.sh

COPY --from=build ${JAR_PATH} lib/${SERVICE_NAME}.jar

ENTRYPOINT ["./bin/startup.sh"]
