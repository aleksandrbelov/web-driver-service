#!/bin/sh

if [ "$DEBUG" = true ]; then
  printf "Running the application in debug mode\n"
  JAVA_OPTS="${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=${DEBUG_PORT}"
fi

exec java $JAVA_OPTS -jar lib/${SERVICE_NAME}.jar