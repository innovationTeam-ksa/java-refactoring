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
    git clone https://github.com/yourusername/movierental.git
    cd movierental
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

## Contact

For any questions or issues, please contact [your-email@example.com].

