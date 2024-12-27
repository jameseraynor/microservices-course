# Photo API Users Service

A Spring Boot microservice for managing user accounts in a photo application.

This service provides user registration, authentication, and profile management functionalities. It integrates with other microservices to offer a comprehensive photo management solution.

The Photo API Users Service is built using Spring Boot and leverages Spring Cloud for microservices architecture. It includes features such as user creation, authentication using JWT, and integration with an Album service for retrieving user albums.

## Repository Structure

```
photo-api-users/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── apicourse/
    │               └── photapp/
    │                   └── api/
    │                       └── users/
    │                           └── photo_api_users/
    │                               ├── data/
    │                               │   ├── AlbumServiceClient.java
    │                               │   ├── UserEntity.java
    │                               │   └── UsersRepository.java
    │                               ├── PhotoApiUsersApplication.java
    │                               ├── security/
    │                               │   ├── AuthenticationFilter.java
    │                               │   └── WebSecurity.java
    │                               ├── service/
    │                               │   ├── UserService.java
    │                               │   └── UserServiceImpl.java
    │                               ├── shared/
    │                               │   ├── FeignErrorDecoder.java
    │                               │   └── UserDto.java
    │                               └── ui/
    │                                   ├── controllers/
    │                                   │   └── UserController.java
    │                                   └── model/
    │                                       ├── AlbumResponseModel.java
    │                                       ├── CreateUserRequestModel.java
    │                                       ├── CreateUserResponseModel.java
    │                                       ├── LoginRequestModel.java
    │                                       └── UserResponseModel.java
    └── test/
        └── java/
            └── com/
                └── apicourse/
                    └── photapp/
                        └── api/
                            └── users/
                                └── photo_api_users/
                                    └── PhotoApiUsersApplicationTests.java
```

### Key Files:

- `PhotoApiUsersApplication.java`: The main entry point for the application.
- `WebSecurity.java`: Configures security settings for the application.
- `UserController.java`: Handles HTTP requests related to user operations.
- `UserServiceImpl.java`: Implements the business logic for user-related operations.
- `AuthenticationFilter.java`: Custom filter for user authentication and JWT token generation.

## Usage Instructions

### Prerequisites

- Java 21
- Maven 3.6+
- MySQL 8.0+

### Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   cd photo-api-users
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   java -jar target/photo-api-users-0.0.1-SNAPSHOT.jar
   ```

### Configuration

The application uses Spring Cloud Config for externalized configuration. Ensure that the Config Server is running and accessible. Key configuration properties include:

- `spring.datasource.url`: Database connection URL
- `spring.datasource.username`: Database username
- `spring.datasource.password`: Database password
- `token.secret`: Secret key for JWT token generation
- `token.expiration_time`: JWT token expiration time in seconds

### API Endpoints

- `POST /users`: Create a new user
- `GET /users/{userId}`: Retrieve user details by ID
- `POST /users/login`: Authenticate user and generate JWT token

Example of creating a user:

```bash
curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json" \
     -d '{"firstName":"John","lastName":"Doe","email":"john@example.com","password":"password123"}'
```

### Testing

Run the tests using:

```
mvn test
```

### Troubleshooting

1. Issue: Unable to connect to the database
   - Ensure MySQL is running and accessible
   - Verify database credentials in the configuration
   - Check for any firewall restrictions

2. Issue: JWT token not generated
   - Verify that the `token.secret` is correctly set in the configuration
   - Ensure the authentication process is completed successfully

3. Issue: Microservice discovery failing
   - Check if Eureka server is running and accessible
   - Verify network connectivity between services

For verbose logging, add the following to your application.properties:
```
logging.level.com.apicourse.photapp=DEBUG
```

## Data Flow

1. User sends a request to create an account or login.
2. The request is intercepted by the `AuthenticationFilter`.
3. For new user creation, `UserController` receives the request and calls `UserServiceImpl`.
4. `UserServiceImpl` creates a new user entity and stores it in the database via `UsersRepository`.
5. For login, `AuthenticationFilter` validates credentials and generates a JWT token.
6. When retrieving user details, `UserController` calls `UserServiceImpl`.
7. `UserServiceImpl` fetches user data from the database and album data from `AlbumServiceClient`.
8. The combined user and album data is returned to the client.

```
[Client] <-> [AuthenticationFilter] <-> [UserController] <-> [UserServiceImpl] <-> [UsersRepository]
                                                                               <-> [AlbumServiceClient]
```

## Deployment

This service is designed to be deployed as part of a microservices architecture. It should be registered with a service discovery system (e.g., Eureka) and configured to use a centralized configuration service.

1. Build the Docker image:
   ```
   docker build -t photo-api-users .
   ```

2. Run the container:
   ```
   docker run -p 8080:8080 photo-api-users
   ```

Ensure that environment variables for database connection, service discovery, and other microservices are properly configured in your deployment environment.