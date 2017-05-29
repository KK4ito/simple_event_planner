#!/bin/sh


# start registry first
  java -jar registry/build/libs/*.jar &

# wait to allow registry to boot
sleep 50

# start now all microservices
# PROJECTS="eventmanagement"
PROJECTS="mailer eventmanagement"

for PROJECT in $PROJECTS
do
  java -jar $PROJECT/build/libs/*.jar &
  sleep 2
done
