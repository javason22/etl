<p align="center">
      <img src="https://img.shields.io/badge/Release-V0.0.1-green.svg" alt="Downloads">
      <img src="https://img.shields.io/badge/JDK-17+-green.svg" alt="Build Status">
  <img src="https://img.shields.io/badge/Springdoc%20Open%20API-2.2.0-blue.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Redisson-3.30.0-red.svg" alt="Coverage Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-blue.svg" alt="Downloads">
   <img src="https://img.shields.io/badge/Author-Jason%20Wong-ff69b4.svg" alt="Downloads">
 </a>
</p>  

# ETL Application

## Description

This ETL (Extract, Transform, Load) application was developed using Java, Spring Boot, and Maven. It provides APIs for managing CRUD of wagers from the source database and wager summaries from the destination database.
It also provides API to trigger the ETL transformation, which counts the total wager amounts of each account in each day and writes the summary to the destination database.

## Features

- A simple generic Extractor-Transformer-Loader design for high code re-usable and extensible 
- Standard RESTful APIs CRUD operations for wagers and wager summaries
- Pagination for wagers and wager summaries with query criteria
- Trigger ETL transformation to summarize the wager amounts by account and day
- Two versions of ETL transformation
  - Version 1: Summarize the wager amounts by account and day in Java code
  - Version 2: Summarize the wager amounts by account and day in MySQL using GROUP BY clause
- API documentation using Swagger
- Redis caching for CRUD results for fast entities retrieval
- Bloom filter to prevent invalid and non-exist ID searching in database queries
- Concurrent asynchronous processing to increase the performance of ETL transformation
- Redis distributed lock for ETL transformation to guarantee only ETL transformation is atomic operation
- Spring Data JPA for interacting with MySQL databases
- Redisson for interacting with Redis server
- Spring Validation for validating APIs' request
- Mockito for unit testing

## Installation

### Prerequisites
The following software must be installed on your machine to run the ETL application:
- Java JDK
- Maven
- MySQL
- Redis
- Docker (optional)

### MySQL and Redis Installation with Docker (Optional)
To simplify the installation of MySQL and Redis, you can use Docker to run MySQL and Redis in containers.
#### Step to run MySQL and Redis in Docker containers
1. Edit the docker-compose.yml file in the project root directory to set the MySQL and Redis connection properties like username and password.

2. Run docker-compose.yml in the project root directory to start MySQL and Redis containers
```bash
docker-compose up -d
```

3. Run the ./sql/input/init.sql script in etl-mysql-in-service container to create database_read
```bash
mysql -h localhost -P 3306 -u root -p < ./sql/input/init.sql
```

4. Run the ./sql/output/init.sql script in etl-mysql-out-service container to create database_write
```bash
mysql -h localhost -P 3307 -u root -p < ./sql/output/init.sql
```

5. Use other MySQL client tools to execute the SQL scripts in the ./sql/input/init.sql and ./sql/output/init.sql if mysql command is not available.

### Application Configuration
Configure the MySQL and Redis connection properties in the application.yml file located in the src/main/resources directory.

#### MySQL data sources
The application uses two MySQL databases, one for the source database and the other for the destination database. 
The source database stores the wager data, and the destination database stores the wager summary data.
Configure the MySQL connection properties for the source and destination databases in the application.yml file located in the src/main/resources directory.
```yaml
app:
  datasource:
    input:
      url: jdbc:mysql://localhost:3306/database_read
      username: [user_name] # Input MySQL DB's username
      password: [password] # Input MySQL DB's password
    output:
      url: jdbc:mysql://localhost:3307/database_write
      username: [user_name] # Output MySQL DB's username
      password: [password] # Output MySQL DB's password
```
#### Spring and Redis Configuration
The application uses Spring Data JPA to interact with the MySQL databases and Redisson to interact with the Redis server. 
Configure the Spring and Redis connection properties in the application.yml file located in the src/main/resources directory.
```yaml
spring:
  application:
    name: etl
  profiles:
    active: dev
  data:
    redis:
      host: [redis address] # Redis server address
      port: [redis port] # Redis server port
      database: [redis database name] # Redis database name
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        jdbc:
          time_zone: UTC-7 # Set your own time zone so that the time objects passed in API are consistent with the MySQL database
```
#### Turnoff ONLY_FULL_GROUP_BY in source MySQL
The application's v2 ETL transformation uses the GROUP BY clause in the SQL query to summarize the wager amounts by account and day. To avoid the error _"Expression #1 of SELECT list is not in GROUP BY clause and contains nonaggregated column"_, turn off the ONLY_FULL_GROUP_BY mode in the source MySQL database.
- SQL command to turn off the ONLY_FULL_GROUP_BY mode:
```sql
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```

### Steps to run the ETL application

1. Clone the repository
```bash
git clone https://github.com/javason22/etl.git
```

2. Change the configuration in the application.yml file if necessary (check the Application Configuration section

3. Navigate to the project directory
```bash
cd ./etl
```

4. Build the project
```bash
mvn clean package
```

5. Run the application
```bash
cd ./target
java -jar etl-0.0.1-SNAPSHOT.jar
```
After starting the application, the Spring Boot application will be running on http://localhost:8080
With ddl-auto attribute in application.yml set to 'update', the necessary tables will be created in the source and destination databases automatically.

5. Access the application
- Use CURL or Postman to access the APIs in http://localhost:8080/api/v1/wager/ and http://localhost:8080/api/v1/wager-summary/
- Open a web browser and navigate API documentation in http://localhost:8080/swagger-ui.html

## Usage
The application provides the following endpoints:  

### Wager API
- GET /api/v1/wager/ - Get list of wager from source database with pagination
- GET /api/v1/wager/{id} - Get a wager from source database by id
- POST /api/v1/wager/ - Create a new wager into source database
- PUT /api/v1/wager/{id} - Update a wager in source database by id
- DELETE /api/v1/wager/{id} - Delete a wager from source database by id
### Wager Summary API
- GET /api/v1/wager-summary/ - Get list of wager summaries from destination database with pagination
- GET /api/v1/wager/{id} - Get a wager summary from destination database by id
- POST /api/v1/wager/ - Create a new wager into destination database summary
- PUT /api/v1/wager/{id} - Update a wager summary in destination database by id
- DELETE /api/v1/wager/{id} - Delete a wager summary from destination database by id
### ETL API
- POST /api/v1/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 1). Version 1 summarize the wager amounts by account and day in Java code.
- POST /api/v2/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 2). Version 2 delegates the sum of wager amount to MySQL. It uses GROUP BY clause in the SQL query to summarize the wager amounts by account and day.

## Swagger API Documentation
The detailed API documentation was constructed by Swagger framework. After started up the ETL application, the API documentation is available at http://localhost:8080/swagger-ui.html

## CURL Commands
All the CURL commands are provided below to access the APIs. The only authentication required is the X-User-ID header, which is used to identify the user who is accessing the APIs. The X-User-ID header is required for all the APIs' CURL commands. It is used to simulate very simple user's authentication and authorization. 

### Wager APIs' CURL Commands
#### Create a new wager
```bash
curl -X POST "http://localhost:8080/api/v1/wager/" -H "Content-Type: application/json" -H "X-User-ID: jason" -d "{\"accountId\":\"00001\",\"wagerAmount\":100.01,\"wagerTimestamp\":\"2022-01-01T00:00:00-0800\"}"
```
#### Get list of wagers with pagination
```bash
# CURL command with selection criteria accountId and wagerAmount
curl -X GET "http://localhost:8080/api/v1/wager/?page=0&size=10&accountId=00001&wagerAmount=100.01" -H "X-User-ID: jason"
# CURL command without selection criteria
curl -X GET "http://localhost:8080/api/v1/wager/?page=0&size=10" -H "X-User-ID: jason"
```
#### Get a wager by id
```bash
# replace {id} with the wager id in UUID format
curl -X GET "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason"
```
#### Update a wager
```bash
# replace {id} with the wager id in UUID format
curl -X PUT "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"00001\",\"wagerAmount\":200.0,\"wagerTimestamp\":\"2022-01-01T00:00:00-0800\"}"
```
#### Delete a wager
```bash
# replace {id} with the wager id in UUID format
curl -X DELETE "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason"
```
### Wager Summary APIs' CURL Commands
#### Get list of wager summaries with pagination
```bash
# CURL command with selection criteria accountId, totalWagerAmount, and wagerDate
curl -X GET "http://localhost:8080/api/v1/wager-summary/?page=0&size=10&accountId=0001&totalWagerAmount=100.00&wagerDate=2022-01-01" -H "X-User-ID: jason"
# CURL command without selection criteria
curl -X GET "http://localhost:8080/api/v1/wager-summary/?page=0&size=10" -H "X-User-ID: jason"
```
#### Get a wager summary by id
```bash
# replace {id} with the wager summary id in UUID format
curl -X GET "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason"
```
#### Create a new wager summary
```bash
curl -X POST "http://localhost:8080/api/v1/wager-summary/" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"0002\",\"totalWagerAmount\":100.0,\"wagerDate\":\"2022-01-01\"}"
```
#### Update a wager summary
```bash
# replace {id} with the wager summary id in UUID format
curl -X PUT "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"0002\",\"totalWagerAmount\":200.0,\"wagerDate\":\"2022-01-01\"}"
```
#### Delete a wager summary
```bash
# replace {id} with the wager summary id in UUID format
curl -X DELETE "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason"
```
### ETL APIs' CURL Commands
#### Trigger ETL transformation (Version 1)
```bash
# CURL command with paramters
# if immediateReturn is true, the API will return immediately after the ETL transformation is triggered
# if immediateReturn is false, the API will return after the ETL transformation is completed. A list of wager summaries will be returned in the response.
curl -X POST "http://localhost:8080/api/v1/etl/trigger" -H "Content-Type: application/json" -H "X-User-ID: jason" -d "{\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-31\",\"immediateReturn\":true}"
# CURL command without paramters
# All wagers will be extracted in this case. The immediateReturn is set to true by default.
curl -X POST "http://localhost:8080/api/v1/etl/trigger" -H "X-User-ID: jason"
```
#### Trigger ETL transformation (Version 2)
```bash
# CURL command with paramters
# if immediateReturn is true, the API will return immediately after the ETL transformation is triggered
# if immediateReturn is false, the API will return after the ETL transformation is completed. A list of wager summaries will be returned in the response.
curl -X POST "http://localhost:8080/api/v2/etl/trigger" -H "Content-Type: application/json" -H "X-User-ID: jason" -d "{\"startDate\":\"2022-01-01\",\"endDate\":\"2022-01-31\",\"immediateReturn\":true}"
# CURL command without paramters. 
# All wagers will be extracted in this case. The immediateReturn is set to true by default.
curl -X POST "http://localhost:8080/api/v2/etl/trigger" -H "X-User-ID: jason"
```
## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.5/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)