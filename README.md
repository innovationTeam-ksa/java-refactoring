## Table of Contents

1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
3. [Database Schema](#3-database-schema)
4. [API Documentation](#4-api-documentation)
5. [Logging](#5-logging)
6. [Unit Testing](#6-unit-testing)
7. [Test Coverage](#7-test-coverage)
8. [Docker-multi-stage](#8-docker-multistage)
9. [Helm Chart](#9-helm-chart)
10. [Scaling Support](#10-scaling-support)


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
<img width="700" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/11d4ba64-99f9-44f3-a3e7-176e35ef71ad">
 
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


#### Retrieve Customer Statement
- **URL** : `/v1/customer/{customerId}/statement`
- **Method** : `GET`
- **Description** : `Retrives the rental statement for a specfic customer`



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

## 5. Logging

### Logging Integeration
- This project utilizes Logback for logging.
- I ensured the logging solution is clear and readable.
<img width="600" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/d3b9ae85-4b10-48ae-93f1-65c66c26673b">
<img width="200" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/787f8de7-da08-451c-b51f-0712546e54b9">

*Example of logging output in the console and in the file `/logs/innovation-logs`.*

## 6. Unit Testing

- Unit testing is applied to the following layers: controller, service, and mappers.
- ensures that all possible test scenarios are covered.
- Test coverage is shown in the "test-coverage" section.

<img width="600" alt="coverage" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/f41cc5f8-cda6-4220-a863-76e6a54b5fb5">


## 7. Test Coverage
- Using JaCoCo to measure the test coverage of our codebase.
- To run and generate a report write `mvn clean test jacoco:report` 
- In Jacoco report I have execluded models,enums,repository,constants,entities from the overall coverage as below
<img width="300" alt="Screen Shot 2024-05-25 at 9 22 17 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/7ef50e80-3fbe-4986-b776-41cbb7b3097c">

<img width="600" alt="Screen Shot 2024-05-25 at 9 19 51 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/c9010559-a0de-40b0-ad5d-219413a6b519">

<img width="400" alt="Screen Shot 2024-05-26 at 6 57 42 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/b0fe61fa-3b49-41bb-a4c7-caff29b20b45">

## 8. Docker Multistage
Implemented a multi-stage Dockerfile to optimize the build process and reduce the final image size.

- The first stage builds the application using Maven and OpenJDK 21.
- The second stage creates a minimal runtime image with OpenJDK 21 and the built JAR file.

### Build and Run

To build the Docker image:

```bash
docker build -t innovationTask .
```

To Run the Docker image:
```bash
docker run -p 8080:8080 innovationTask
```

## 9. Helm Chart
- Project includes a Helm chart with multiple profiles: MySQL and H2.
- The Docker image for this project has been built and pushed to a registry hub. You can pull the image using below command

   ```sh
     docker pull abdelmagied820/innovation-task ```
   
<img width="600" alt="Screen Shot 2024-05-26 at 6 12 03 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/4c835ac2-86ac-49a1-b41a-ba2ecae3bd1f">
*Image hosted on registry hub.*

<img width="500" alt="Screen Shot 2024-05-26 at 6 29 08 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/f3efe95e-c90b-4564-a205-538cb03430a1">
*Forward port to 9099 on your local machine.*



### Install Helm locally
#### Prerequisites
   - Ensure you have a local Kubernetes cluster running.
#### Commands

- **Install with H2 Profile**
  ```sh
  helm install innovation-task-h2 ./innovation-task-helm —values ./innovation-task-helm/profiles/h2-values.yaml
 
- **Install with Mysql Profile**
 ```sh
  helm install innovation-task-mysql ./innovation-task-helm —values ./innovation-task-helm/profiles/mysql-values.yaml
 ```
- **After that run the below command to be able to access project in port 8080 in your machine**
   ```sh
   kubectl --namespace default port-forward innovation-task-h2-innovation-task-helm-6c666577b-2f4br 8080:8080
   ```

## 10. Scaling Support
- The project is designed to be horizontally scalable. You can adjust the `replicaCount` in the `values.yaml` file within the Helm chart to scale the number of replicas and manage load effectively.

<img width="500" alt="Screen Shot 2024-05-26 at 6 31 14 PM" src="https://github.com/Aabdelmajeed/InnovationTask/assets/88937645/514157a7-7d3c-482d-ab55-79b4cdbbb0ed">
*Scaling support*

