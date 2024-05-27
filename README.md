<p align="center">
      <img src="https://img.shields.io/badge/Release-V0.0.1-green.svg" alt="Downloads">
      <img src="https://img.shields.io/badge/JDK-17+-green.svg" alt="Build Status">
  <img src="https://img.shields.io/badge/Springdoc%20Open%20API-2.2.0-blue.svg" alt="Build Status">
   <img src="https://img.shields.io/badge/Redisson-3.30.0-red.svg" alt="Coverage Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-3.2.5-blue.svg" alt="Downloads">
   <img src="https://img.shields.io/badge/Author-Jason%20Wong-ff69b4.svg" alt="Downloads">
   <img src="https://img.shields.io/badge/Copyright%20-@javason22-%23ff3f59.svg" alt="Downloads">
 </a>
</p>  

# ETL Application

## Description

This ETL (Extract, Transform, Load) application was developed using Java, Spring Boot, and Maven. It provides APIs for managing CRUD of wagers from the source database and wager summaries from the destination database.
It also provides API to trigger the ETL transformation, which counts the total wager amounts of each account in each day and writes the summary to the destination database.

## Installation

### Prerequisites
The following software must be installed on your machine to run the ETL application:
- Java JDK
- Maven
- MySQL
- Redis

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
      url: jdbc:mysql://localhost:3307/database_read
      username: root
      password: 123456
    output:
      url: jdbc:mysql://localhost:3307/database_write
      username: root
      password: 123456
```
#### Spring and Redis Configuration
The application uses Spring Data JPA to interact with the MySQL databases and Redisson to interact with the Redis server. Configure the Spring and Redis connection properties in the application.yml file located in the src/main/resources directory.
```yaml
spring:
  application:
    name: etl
  profiles:
    active: dev
  data:
    redis:
      host: [redis address]
      port: [redis port]
      database: [redis database name]
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
        jdbc:
          time_zone: UTC-7
```
#### Turnoff ONLY_FULL_GROUP_BY in source MySQL
The application's v2 ETL transformation uses the GROUP BY clause in the SQL query to summarize the wager amounts by account and day. To avoid the error "Expression #1 of SELECT list is not in GROUP BY clause and contains nonaggregated column", turn off the ONLY_FULL_GROUP_BY mode in the source MySQL database.
- SQL command to turn off the ONLY_FULL_GROUP_BY mode:
```sql
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```

### Steps to run the ETL application

1. Clone the repository
```bash
git clone https://github.com/javason22/etl.git
```

2. Navigate to the project directory
```bash
cd /etl
```

3. Build the project
```bash
mvn clean package
```

4. Run the application
```bash
cd target
java -jar etl-0.0.1-SNAPSHOT.jar
```
After starting the application, the Spring Boot application will be running on http://localhost:8080
With ddl-auto attribute in application.yml set to 'update', the necessary tables will be created in the source and destination databases automatically.

5. Access the application
- Use CURL or Postman to access the APIs in http://localhost:8080/api/v1/wagers/ and http://localhost:8080/api/v1/wagers-summary/
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
- POST /api/v1/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 1)
- POST /api/v2/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 2)

## Swagger API Documentation
The API documentation was constructed by Swagger framework. After started up the ETL application, the API documentation is available at http://localhost:8080/swagger-ui.html

## CURL Commands

### Wager APIs' CURL Commands
#### Create a new wager
```bash
curl -X POST "http://localhost:8080/api/v1/wager/" -H "Content-Type: application/json" -H "X-User-ID: jason" -d "{\"accountId\":\"00001\",\"wagerAmount\":100.01,\"wagerTimestamp\":\"2022-01-01T00:00:00-0800\"}"
```
#### Get list of wagers with pagination
```bash
curl -X GET "http://localhost:8080/api/v1/wager/?page=0&size=10" -H "X-User-ID: jason"
```
#### Get a wager by id
```bash
curl -X GET "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason"
```
#### Update a wager
```bash
curl -X PUT "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"00001\",\"wagerAmount\":200.0,\"wagerTimestamp\":\"2022-01-01T00:00:00-0800\"}"
```
#### Delete a wager
```bash
curl -X DELETE "http://localhost:8080/api/v1/wager/{id}" -H "X-User-ID: jason"
```
### Wager Summary APIs' CURL Commands
#### Get list of wager summaries with pagination
```bash
curl -X GET "http://localhost:8080/api/v1/wager-summary/?page=0&size=10&accountId=0001&totalWagerAmount=100.00&wagerTime=2022-01-01" -H "X-User-ID: jason"
```
#### Get a wager summary by id
```bash
curl -X GET "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason"
```
#### Create a new wager summary
```bash
curl -X POST "http://localhost:8080/api/v1/wager-summary/" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"0002\",\"totalWagerAmount\":100.0,\"wagerDate\":\"2022-01-01\"}"
```
#### Update a wager summary
```bash
curl -X PUT "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason" -H "Content-Type: application/json" -d "{\"accountId\":\"0002\",\"totalWagerAmount\":200.0,\"wagerDate\":\"2022-01-01\"}"
```
#### Delete a wager summary
```bash
curl -X DELETE "http://localhost:8080/api/v1/wager-summary/{id}" -H "X-User-ID: jason"
```
### ETL APIs' CURL Commands
#### Trigger ETL transformation (Version 1)
```bash
curl -X POST "http://localhost:8080/api/v1/etl/trigger" -H "X-User-ID: jason"
```
#### Trigger ETL transformation (Version 2)
```bash
curl -X POST "http://localhost:8080/api/v2/etl/trigger" -H "X-User-ID: jason"
```
