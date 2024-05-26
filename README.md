<p align="center">
      <img src="https://img.shields.io/badge/Release-V1.0.0-green.svg" alt="Downloads">
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
java -jar etl-1.0.0-SNAPSHOT.jar
```

## Usage
The application provides the following endpoints:  
