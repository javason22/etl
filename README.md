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

- Java JDK
- Maven
- MySQL
- Redis

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

3. Run the application
```bash
cd target
java -jar etl-0.0.1-SNAPSHOT.jar
```

## Usage
The application provides the following endpoints:  

### Wager API
- GET /api/v1/wagers/ - Get list of wager from source database with pagination
- GET /api/v1/wagers/{id} - Get a wager from source database by id
- POST /api/v1/wagers/ - Create a new wager into source database
- PUT /api/v1/wagers/{id} - Update a wager in source database by id
- DELETE /api/v1/wagers/{id} - Delete a wager from source database by id
### Wager Summary API
- GET /api/v1/wagers-summary/ - Get list of wager summaries from destination database with pagination
- GET /api/v1/wagers/{id} - Get a wager summary from destination database by id
- POST /api/v1/wagers/ - Create a new wager into destination database summary
- PUT /api/v1/wagers/{id} - Update a wager summary in destination database by id
- DELETE /api/v1/wagers/{id} - Delete a wager summary from destination database by id
### ETL API
- POST /api/v1/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 1)
- POST /api/v2/etl/trigger - Trigger the ETL transformation to summarize the wager amounts by account and day (version 2)

## API Documentation
The API documentation was constructed by Swagger framework. After started up the ETL application, the API documentation is available at http://localhost:8080/swagger-ui.html

## CURL Commands
<details>
 <summary><code>List of CURL Commands to call the ETL APIs</code></summary>
### Wager APIs' CURL Commands
#### Create a new wager
```bash
curl -X POST "http://localhost:8080/api/v1/wagers/" -H "Content-Type: application/json" -d "{\"account\":\"account1\",\"amount\":100.0,\"timestamp\":\"2022-01-01T00:00:00\"}"
```
#### Get list of wagers
```bash
curl -X GET "http://localhost:8080/api/v1/wagers/" -H "accept: application/json"
```
#### Update a wager
```bash
curl -X PUT "http://localhost:8080/api/v1/wagers/{id}" -H "Content-Type: application/json" -d "{\"account\":\"account1\",\"amount\":200.0,\"timestamp\":\"2022-01-01T00:00:00\"}"
```
#### Delete a wager
```bash
curl -X DELETE "http://localhost:8080/api/v1/wagers/{id}" -H "accept: */*"
```
### Wager Summary APIs' CURL Commands
#### Get list of wager summaries
```bash
curl -X GET "http://localhost:8080/api/v1/wagers-summary/" -H "accept: application/json"
```
#### Create a new wager summary
```bash
curl -X POST "http://localhost:8080/api/v1/wagers-summary/" -H "Content-Type: application/json" -d "{\"account\":\"account1\",\"amount\":100.0,\"date\":\"2022-01-01\"}"
```
#### Update a wager summary
```bash
curl -X PUT "http://localhost:8080/api/v1/wagers-summary/1" -H "Content-Type: application/json" -d "{\"account\":\"account1\",\"amount\":200.0,\"date\":\"2022-01-01\"}"
```
#### Delete a wager summary
```bash
curl -X DELETE "http://localhost:8080/api/v1/wagers-summary/1" -H "accept: */*"
```
### ETL APIs' CURL Commands
#### Trigger ETL transformation (Version 1)
```bash
curl -X POST "http://localhost:8080/api/v1/etl/trigger" -H "accept: */*"
```
#### Trigger ETL transformation (Version 2)
```bash
curl -X POST "http://localhost:8080/api/v2/etl/trigger" -H "accept: */*"
```
</details>
