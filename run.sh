#!/bin/sh


# start registry first
  java -jar registry/build/libs/*.jar &

# wait to allow registry to boot
sleep 5

# start now all microservices
# PROJECTS="eventmanagement moviemanagement usermanagement rentalmanagement"
PROJECTS="eventmanagement"

for PROJECT in $PROJECTS
do
  java -jar $PROJECT/build/libs/*.jar &
  sleep 2
done
