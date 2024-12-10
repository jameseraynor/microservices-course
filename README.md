# Microservices-based Photo Application API

This project is a microservices-based API for a photo application, providing user management, discovery, and API gateway services.

The application is built using Spring Boot and Spring Cloud, implementing a microservices architecture. It consists of several interconnected services that work together to provide a scalable and maintainable solution for managing photo-related functionalities.

The main components of this application include:

1. API Gateway: Acts as the entry point for client requests, routing them to appropriate services.
2. Discovery Service: Implements service registration and discovery using Netflix Eureka.
3. User Service: Manages user-related operations such as user creation and authentication.
4. Mobile App: Provides the client-side interface for interacting with the photo application.
5. Photo API Management: Handles photo-related operations and management tasks.

These services are designed to work together, communicating via RESTful APIs and utilizing service discovery for dynamic scaling and fault tolerance.

## Repository Structure

The repository is organized into several modules, each representing a different service or component of the application:

```
.
├── api-gateway
├── mobile-app
├── photo-api-management
├── photo-api-users
└── photo-discovery-service
```

### Key Files:

- `api-gateway/api-gateway/src/main/java/com/api/course/photoapp/api/api_gateway/ApiGatewayApplication.java`: Entry point for the API Gateway service.
- `mobile-app/src/main/java/com/developcourse/app/ws/mobile_app/MobileAppApplication.java`: Main application file for the mobile app component.
- `photo-api-users/src/main/java/com/apicourse/photapp/api/users/photo_api_users/PhotoApiUsersApplication.java`: User service application file.
- `photo-discovery-service/src/main/java/com/apicourse/photoapp/discovery/photo_discovery_service/PhotoDiscoveryServiceApplication.java`: Discovery service application file.

### Important Integration Points:

- API Gateway: Serves as the main entry point for external requests, routing them to appropriate services.
- Discovery Service: Eureka server for service registration and discovery.
- User Service: Manages user data and authentication, integrating with other services as needed.

## Usage Instructions

### Installation

Prerequisites:
- Java Development Kit (JDK) 21
- Maven 3.6+
- Spring Boot 3.4.0
- Spring Cloud 2024.0.0

Steps:
1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to each service directory and build the project:
   ```
   cd <service-directory>
   mvn clean install
   ```

### Getting Started

1. Start the Discovery Service:
   ```
   cd photo-discovery-service
   mvn spring-boot:run
   ```

2. Start the API Gateway:
   ```
   cd api-gateway/api-gateway
   mvn spring-boot:run
   ```

3. Start the User Service:
   ```
   cd photo-api-users
   mvn spring-boot:run
   ```

4. Start other services as needed (Photo API Management, Mobile App, etc.)

### Configuration

Each service has its own `application.yml` file for configuration. Key configurations include:

- Server ports
- Eureka client/server settings
- Database connections (if applicable)
- Security settings

### Common Use Cases

1. Creating a new user:
   ```
   POST /users
   {
     "firstName": "John",
     "lastName": "Doe",
     "email": "john@example.com",
     "password": "securepassword"
   }
   ```

2. Retrieving user details:
   ```
   GET /users/{userId}
   ```

### Testing & Quality

Run tests for each service:
```
cd <service-directory>
mvn test
```

### Troubleshooting

1. Service Discovery Issues:
   - Problem: Services not registering with Eureka
   - Solution: 
     1. Ensure Eureka server is running
     2. Check Eureka client configuration in `application.yml`
     3. Verify network connectivity between services and Eureka server

2. API Gateway Routing Problems:
   - Problem: Requests not being routed to the correct service
   - Solution:
     1. Review API Gateway route configurations
     2. Check service registration status in Eureka
     3. Verify service naming consistency across the application

### Debugging

To enable debug logging:
1. Add the following to the `application.yml` of the service:
   ```yaml
   logging:
     level:
       root: DEBUG
   ```
2. Restart the service and check the console output for detailed logs

Log files are typically located in the `logs` directory of each service.

## Data Flow

The request data flow through the application follows this general pattern:

1. Client sends a request to the API Gateway
2. API Gateway routes the request to the appropriate service based on the URL path
3. The service processes the request, potentially communicating with other services or databases
4. The response is sent back through the API Gateway to the client

```
Client -> API Gateway -> Service (User/Photo/etc.) -> Database
  ^                                   |
  |                                   v
  +-----------------------------------+
         (Response flow)
```

Note: The Discovery Service (Eureka) is constantly communicated with by all services for registration and discovery purposes.

## Infrastructure

The application uses Spring Cloud components for its infrastructure:

- Eureka Server (Discovery Service):
  - Type: Spring Cloud Netflix Eureka Server
  - Purpose: Service registration and discovery

- API Gateway:
  - Type: Spring Cloud Gateway
  - Purpose: Request routing and load balancing

- Microservices:
  - Type: Spring Boot applications
  - Purpose: Implement business logic for different domains (users, photos, etc.)

Each service is configured to register with Eureka upon startup, enabling dynamic service discovery and load balancing.