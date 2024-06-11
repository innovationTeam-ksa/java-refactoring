# Movie Rental Service

## Overview

The Movie Rental Service is a Spring Boot application that manages movie rentals for customers. The application uses MongoDB for data storage, adopts the Strategy Pattern for flexible rental calculation logic, and is built and deployed using Docker.

## Technology Choices

### MongoDB

We chose MongoDB for the following reasons:
- **Scalability**: MongoDB is highly scalable and can handle large amounts of data, which is essential for an application managing a significant number of movie rentals.
- **Flexibility**: The document-oriented data model of MongoDB allows for flexible schema design, which is beneficial when dealing with varied and evolving data structures.
- **Performance**: MongoDB offers high performance for read and write operations, which is crucial for ensuring the responsiveness of the application.

### Strategy Pattern

We refactored the code to adopt the Strategy Pattern to:
- **Encapsulation**: By encapsulating the rental calculation logic in separate strategy classes, we adhere to the Single Responsibility Principle, making the code easier to maintain and extend.
- **Flexibility**: The Strategy Pattern allows us to add new types of movies and rental calculation strategies without modifying the existing codebase, enhancing the extensibility of the application.
- **Testability**: Each strategy can be tested independently, improving the overall testability of the code.

## Architecture

### Components

- **Controllers**: Handle HTTP requests and responses.
- **Services**: Contain the business logic of the application.
- **Repositories**: Interface with MongoDB to perform CRUD operations.
- **Strategies**: Implement different rental calculation strategies for various types of movies.
- **Configurations**: Set up application settings, including database connections and security.

### Design Patterns

- **Strategy Pattern**: Used for implementing different rental calculation strategies based on movie types.
- **Component-Based Design**: Ensures separation of concerns and modularity within the application.

## Building and Running Locally

### Prerequisites

- Docker

### Steps

1. **Clone the repository**:

    ```sh
    git clone https://github.com/hatemMT/refactoring-java.git
    ```

2. **Build the Docker image**:

    ```sh
    docker build -t movie-rental-app .
    ```

3. **Run the Docker container**:

    ```sh
    docker run -p 8080:8080 movie-rental-app
    ```
#### OR
**Run the Docker container with MongoDB using docker-compose**:

    ```sh
    docker-compose up -d
    ```
#### Run the tests:
1. run only the database from docker compose file
    ```sh
    docker-compose up -d mongo
    ```
2. run the tests
    ```sh
    ./gradlew clean test
    ```
### Accessing the Application

After running the above commands, the application will be accessible at `http://localhost:8080`.

## Detailed Explanation

### Multi-Stage Docker Build

We use a multi-stage Docker build to optimize the build process and reduce the final image size. The first stage uses a Gradle image to build the application, and the second stage uses a minimal JRE image to run the application. This approach ensures that the final image is small and secure, containing only the necessary runtime dependencies.

### Caching Dependencies

To speed up the build process, we cache the Gradle dependencies by copying the Gradle wrapper and build files first and running the `./gradlew dependencies` command. This layer is cached and reused if the dependencies do not change, significantly reducing build times for subsequent builds.

## Load Testing with JMeter

### Overview

A JMeter `.jmx` file is provided to simulate a load of 10 million requests per hour. This helps in understanding the performance and scalability of the application under high load conditions.

### Prerequisites

- Apache JMeter installed
- Ensure the application is running locally on port 8080

### Running the JMeter Test

1. **Navigate to the directory containing the `.jmx` file**:

    ```sh
    cd src/test/resources/
    ```

2. **Execute the JMeter test plan from the command line**:

    ```sh
    jmeter -n -t load_test.jmx -l results.jtl
    ```

   - `-n`: Non-GUI mode
   - `-t`: The path to the JMeter test plan file (`.jmx` file)
   - `-l`: The path to the results file (`.jtl` file)

### Viewing Results

After running the test, you can view the results in the `results.jtl` file or load the results into JMeter GUI for a more detailed analysis.

```sh
jmeter -g results.jtl -o /path/to/output/report
```

## Deploying MongoDB and Movie Rental Service Using Helm

### Step 1: Clone the Repository

Clone the repository to your local machine:

### Step 2: Build the Docker Image

Build the Docker image for the Movie Rental Service application:

`docker build -t movie-rental-app .`

### Step 3: Push the Docker Image to a Container Registry (should be done within CICD pipeline)

Push the Docker image to a container registry (e.g., Docker Hub). Ensure you replace `yourusername` with your actual Docker Hub username:

`docker tag movie-rental-app:latest yourusername/movie-rental-app:latest`

`docker push yourusername/movie-rental-app:latest`

### Step 4: Update the Helm Chart Values

Update the `helm/movie-rental/values.yaml` file to use your Docker image from the container registry by setting the following fields:

- `image.repository` to `yourusername/movie-rental-app`
- `image.tag` to `latest`
- `mongodb.host` to `mongodb-mongodb`
- `mongodb.port` to `27017`

### Step 5: Deploy MongoDB

Deploy MongoDB using Helm:

`helm install mongodb-release helm/mongodb`

This command deploys MongoDB using the Helm chart located in the `helm/mongodb` directory. The release name `mongodb-release` is used to identify this deployment.

### Step 6: Deploy the Movie Rental Service

Deploy the Movie Rental Service using Helm:

`helm install movie-rental-release helm/movie-rental`

This command deploys the Movie Rental Service using the Helm chart located in the `helm/movie-rental` directory. The release name `movie-rental-release` is used to identify this deployment.

### Step 7: Check the Status of the Deployments

Check the status of both deployments to ensure they are running correctly:

`helm status mongodb-release`

`helm status movie-rental-release`

### Accessing the Application

After running the above commands, the Movie Rental Service application will be accessible at `http://<your-cluster-ip>:8080`.



## Contact

For any questions or issues, please contact [hatem.mohtaha@gmail.com].

