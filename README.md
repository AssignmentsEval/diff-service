# Diff Service

Diff Service provides REST APIs to store two inputs as base64 under an identifier and compare them. 

### Save Left and Save Right

POST to store the base64 value as left or right attributes.

     <host>/v1/diff/<diffId>/left
     <host>/v1/diff/<diffId>/right
     
Payload sample:

```json
{
  "base64Value": "<value>"
}
``` 

Response sample: 

```json
{
  "diffId": "<diffId>",
  "left": "<leftValue>",
  "right": "<rightValue>"
}
```
     
### Diff

GET to compare the left and right values under the given identifier.
     
     <host>/v1/diff/<diffId>
     
Response sample: 

```json
{
  "diffId": "<diffId>",
  "left": "YmFzZTY0IQ==",
  "right": "YmFzZWY0IQ==",
  "resultType": "SAME_SIZE_BUT_DIFFERENT",
  "diffResults": [
    {
      "offset": 5,
      "length": 1
    }
  ]
}
```

## Architecture

### Language

* Implementation:
    * Java 8
    
* Tests
    * Groovy 2.5.6
    
* Integration Tests
    * Groovy 2.5.6
    
* Build
    * Gradle 4.10.3
    
### Frameworks

* Implementation:
    * [SpringBoot](https://spring.io/projects/spring-boot)
    * [Lombok](https://projectlombok.org/)
    * [Mapstruct](https://mapstruct.org/)
    * [Swagger](https://swagger.io/)
    * [Logstash Logback Encoder](https://github.com/logstash/logstash-logback-encoder)
    
* Tests:
    * [Spock](https://spockframework.org/)
     
* Integration Tests:
    * [Spock](https://spockframework.org/)
    * [REST Assured](https://rest-assured.io/)
    * [Flapdoodle](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
    
* Build
    * [Checkstyle](http://checkstyle.sourceforge.net/)
    * [Codenarc](http://codenarc.sourceforge.net/)
    * [PMD](https://pmd.github.io/)
    * [Error Prone](http://errorprone.info/)
    * [JaCoCo](https://www.eclemma.org/jacoco/)

### Code structure
```
/src
├───main
│   ├───java
│   │   └───com
│   │       └───limac
│   │           └───diffservice
│   │               ├───autoconfigure
│   │               ├───domain
│   │               ├───exception
│   │               ├───mapping
│   │               ├───repository
│   │               ├───rest
│   │               │   ├───dto
│   │               │   └───exceptionhandler
│   │               ├───service
│   │               ├───type
│   │               └───validation
│   │                   └───annotation
│   └───resources
│       └───META-INF
├───test
│   ├───groovy
│   │   └───com
│   │       └───limac
│   │           └───diffservice
│   │               ├───mapping
│   │               ├───rest
│   │               │   └───exceptionhandler
│   │               └───service
│   └───resources
│       └───META-INF
│           └───services
└───test-integration
    ├───groovy
    │   └───com
    │       └───limac
    │           └───diffservice
    │               └───rest
    └───resources
        └───META-INF
            └───services
```
## Gradle

### Gradle Clean

Execute this command to clean all the generated files.

```bash
./gradlew clean
```

### Gradle Build

Execute this command to check the code with CheckStyle, PMD, Codenarc and run the tests with JaCoCo as code coverage.

```bash
./gradlew build
```

The ```diff-service-1.0.0.jar``` will be generated in ```build/libs```.
   
CheckStyle report can be found in ```build/reports/checkstyle```.

Codenarc report can be found in ```build/reports/codenarc```.

PMD report can be found in ```build/reports/pmd```.

JaCoCo report can be found in ```build/reports/jacoco```.

### Gradle Run

Execute this command to start the application. By default, it starts on 'http://localhost:7080/api/diff-service'.
It's necessary to have a mongo running in your environment, if you don't, check "Docker" session.

```bash
./gradlew bootRun
```

## Test

### Run test

Execute this command to run the unit tests.

```bash
./gradlew test
```
   
Test report can be found in ```build/reports/test```.

## Integration Test

### Run integration test

Execute this command to run the integration tests.

```bash
./gradlew integrationTest
```
   
Flapdoodle provides an embedded MongoDB in integrationTest scope, so it's not necessary to have a mongo running in your environment.

Integration test report can be found in ```build/reports/integrationTest```.

## Docker

### Diff Service Image

Check "Gradle Build" session to generate the ```diff-service-1.0.0.jar```.

Execute this command to build the diff-service image.

```bash
docker build -t diff-service .
```
    
### Running Diff Service

Execute this command to run the diff-service.

```bash
docker-compose up
```
    
Docker Compose will pull a mongo image, so it's not necessary to have a mongo running in your environment.
The application is under basic security, check the "Security" session.
    
## Command Line

### Save Left

Execute this command to call saveLeft.

```bash
curl -X POST "http://localhost:7080/api/diff-service/v1/diff/diffId/left" -H "accept: application/json" -u user:pass -H "Content-Type: application/json" -d "{ \"base64Value\": \"YmFzZTY0IQ==\"}"
```

### Save Right

Execute this command to call saveRight.

```bash
curl -X POST "http://localhost:7080/api/diff-service/v1/diff/diffId/right" -H "accept: application/json" -u user:pass -H "Content-Type: application/json" -d "{ \"base64Value\": \"YmFzZTY0IQ==\"}"
``` 
    
### Diff

Execute this command to call diff.

```bash
curl -X GET "http://localhost:7080/api/diff-service/v1/diff/diffId" -u user:pass -H "accept: application/json"
```

## Swagger

### Swagger UI

Swagger UI can be accessed at [http://localhost:7080/api/diff-service/swagger-ui.html](http://localhost:7080/api/diff-service/swagger-ui.html)

The application is under basic security, check the "Security" session.
### Swagger JSON

Swagger JSON can be accessed at [http://localhost:7080/api/diff-service/swagger](http://localhost:7080/api/diff-service/swagger)

## Actuator

Actuator can be accessed at [http://localhost:7080/api/diff-service/actuator](http://localhost:7080/api/diff-service/actuator)

### Health

Info can be accessed at [http://localhost:7080/api/diff-service/actuator/health](http://localhost:7080/api/diff-service/actuator/health)

## Security

### Basic Security

The application is under basic security, when prompted for user and password, please use the following credentials:
    
    user: user
    password: pass
    
Or send the credentials as cURL parameter:

    -u user:pass
    
If you want to specify your own credentials, update the file ```application-local.yml```:

```yaml
security:
  basic:
    user: <your user>
    pass: <your password>
```