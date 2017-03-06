# Microservices

## Prerequisites

JDK1.8 (or higher) is required. Check with

```shell
$ java -version
```

[H2](http://www.h2database.com/html/main.html) will be used as embedded database. 
H2 is setup as in-memory database. Therefore, no installation is required. 

**ATTENTION:** All persisted datas are lost on restart.

## Building

The services are gradle projects and can be built with the gradle wrapper from the HOME directory of the microservices

```shell
$ cd microservices
$ ./gradlew clean bootRepackage
```
    
will build all microservices listed in file 'settings.gradle'.

## Testing

Run all JUnit tests with

```shell
$ cd microservices
$ ./gradlew test
````

## Running a microservice

Use the following gradle task to launch the application

```shell
$ cd microservices/usermanagement
$ ./gradlew bootRun
```    
All properties are initially read from file `src/main/resources/application.properties`. These
properties can be overwritten by creating a new file `application.properties` in the root directory
of the microservice itself.

## Deployment

Create an executable jar of the microservice

```shell
$ cd microservices/usermanagement
$ ./gradlew bootRepackage
```
    
Copy the jar-file from directory `build/libs`into an a deploy location of your choice.

```shell
$ cp build/libs/usermanagement-1.0.0.jar $HOME/deploy
```

Create a file `application.properties` in $HOME/deploy to overwrite the defaults.

### How to start all microservices

Change to the HOME directory of all microservices!

Use gradle with the "--parallel" command line parameter:

```shell
$ ./gradlew :moviemanagement:bootRun :usermanagement:bootRun :rentalmanagement:bootRun --parallel
```



