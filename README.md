<img width="446" alt="Screen Shot 2024-05-25 at 12 33 36 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/003f9bb2-aa13-4c37-bec4-a8f3f712eb32">
## Table of Contents

1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
3. [Database Schema](#3-database-schema)
4. [API Documentation](#4-api-documentation)
5. [Unit Testing](#5-unit-testing)
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


## 4. Api Documentation

## CustomerController
Manages operations related to customers.

### Endpoints

#### Retrieve all customers
- **URL**: `/v1/customers`
- **Method**: `GET`
- **Description**: Fetches a list of all customers.

#### Create a new customer
- **URL**: `v1/customers`
- **Method**: `POST`
- **Description**: Creates a new customer.
- **Request Body**:
  ```json
  {
    "name": "string"
  }





## MovieController
Manages operations related to movies.

### Endpoints
### Retrieve all movies
- **URL**: /v1/movies
- **Method**: GET
- **Description**: Fetches a list of all movies.

### Create a new movie
- **URL**: /v1/movies
- **Method**: POST
- **Description**: Creates a new movie.
- **Request Body**:
```json
 {
  "title": "string",
  "code": "string",
 }
```

## MovieRentalController
Manages operations related to movie rentals.

### Endpoints
### Rent a movie
- **URL**: /v1/rentals
- **Method**: POST
- **Description**: Rents a movie.
- **Request Body**:
```json
{
  "customerId": "integer",
  "movieId": "integer",
  "days": "integer"
}
```
#### Get All rented movies by customer
- **URL**: `/v1/rentals/customers/{customerId}`
- **Method**: `GET`
- **Description**: Get all rentals for specfic Customer.


## 5. Unit Testing

- Unit testing is applied to the following layers: controller, service, and mappers.
- ensures that all possible test scenarios are covered.
- Test coverage is shown in the "test-coverage" section.
 <img width="700" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/0a91790f-bfb0-4bf8-a574-658be7296ccd">

 
