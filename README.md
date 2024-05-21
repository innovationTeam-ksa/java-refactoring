
## Table of Contents

1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
3. [Database Schema](#3-database-schema)


---

## 1. Introduction

### Overview
- The task, assigned by the Innovation Team, focuses on refactoring Java code to improve its structure and efficiency.

### Project Requirements and Objectives

- code refactoring to be using java 21.
- spring boot 3.2.x.
- use appropriate db from your choice and make sure to justify why you have selected it.
- code coverage / unit testing / integration testing.
- build docker for you solution with multi-stage.
- provide helm chart for the solution.
- make sure you have appropriate apis documentations.
- make sure that all apis are behaving on reactive manner.
- make sure all apis are secured.
- make sure your logs are readable for tracing.
- make sure your solution have different profiles defined inside your application packaging at helm chart level.
- build benchmarking for your provided service consider this service is going to handle 10 M of transactions per hour.
- your service must be production ready and can support scaling at any time.

## 2. Getting Started

### System Requirements
- Java 21 
- Maven build tool
- spring-boot 3.2.5

### Installing 
To install and run Innovation Task:
1. Clone the repository: `[git clone https://github.com/Aabdelmajeed/InnovationTask.git)`
2. Build the project: `mvn clean install`
3. Run the project: `mvn spring-boot:run`


## 3. Database Schema
### Why choosing Relational DB?
- as our Data is structured also to maintain data integrity so each record in rentalMovie table have a valid records in both customer and movie table.This ensures that each rental transaction is accurately linked to existing customer and movie data,
<img width="700" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/c642addc-901e-4ed9-a966-177b234418b4">

