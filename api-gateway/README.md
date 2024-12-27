# Spring Cloud API Gateway for Photo App Microservices

This project implements a Spring Cloud API Gateway for a Photo App microservices architecture. It provides a centralized entry point for client applications to access various microservices, handling cross-cutting concerns such as authentication, authorization, and request routing.

The API Gateway is built using Spring Boot 3.4.1 and Spring Cloud 2024.0.0. It leverages Spring Cloud Gateway for routing and filtering capabilities, and integrates with Eureka for service discovery. The gateway implements JWT-based authentication and authorization, ensuring secure access to the underlying microservices.

## Repository Structure

```
api-gateway/
└── api-gateway/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/api/course/photoapp/api/api_gateway/
        │   │       ├── ApiGatewayApplication.java
        │   │       ├── AuthorizationHeaderFilter.java
        │   │       ├── GlobalFiltersConfig.java
        │   │       ├── MyPostFilter.java
        │   │       └── MyPreFilter.java
        │   └── resources/
        │       ├── application.yml
        │       └── META-INF/
        │           └── additional-spring-configuration-metadata.json
        └── test/
            └── java/
                └── com/api/course/photoapp/api/api_gateway/
                    └── ApiGatewayApplicationTests.java
```

Key Files:
- `ApiGatewayApplication.java`: The main entry point for the API Gateway application.
- `AuthorizationHeaderFilter.java`: Implements JWT token validation for incoming requests.
- `GlobalFiltersConfig.java`: Configures global filters for request processing.
- `application.yml`: Contains the main configuration for the API Gateway (currently commented out).

## Usage Instructions

### Installation

Prerequisites:
- Java Development Kit (JDK) 21
- Maven 3.6+
- Spring Boot 3.4.1
- Spring Cloud 2024.0.0

To install and run the API Gateway:

1. Clone the repository:
   ```
   git clone <repository-url>
   cd api-gateway/api-gateway
   ```

2. Build the project:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

### Configuration

The main configuration file is `application.yml`. Ensure to uncomment and adjust the following settings:

```yaml
server:
  port: 8082

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: service1-route
          uri: lb://service1
          predicates:
            - Path=/user-ws/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8010/eureka
```

### JWT Authentication

The `AuthorizationHeaderFilter` class handles JWT token validation. Ensure to set the `token.secret` property in your environment or configuration file.

### Custom Filters

The project includes several custom filters:
- `MyPreFilter`: Logs incoming request details.
- `MyPostFilter`: Executes after the main filter chain.
- `GlobalFiltersConfig`: Configures additional global filters.

### Testing

Run the tests using:

```
mvn test
```

### Troubleshooting

Common issues:
1. Connection refused to Eureka server
   - Ensure Eureka server is running and accessible at the configured URL.
   - Check network connectivity and firewall settings.

2. JWT validation fails
   - Verify the `token.secret` is correctly set and matches the auth service.
   - Ensure the JWT token in the request is valid and not expired.

3. Routes not working as expected
   - Double-check the route configurations in `application.yml`.
   - Verify service names match those registered in Eureka.

For debugging:
- Enable debug logging by adding `logging.level.org.springframework.cloud.gateway=DEBUG` to `application.yml`.
- Check application logs for detailed error messages and request processing information.

## Data Flow

1. Client sends a request to the API Gateway.
2. `MyPreFilter` logs the incoming request details.
3. `AuthorizationHeaderFilter` validates the JWT token if present.
4. The request is routed to the appropriate microservice based on the path.
5. The microservice processes the request and sends a response.
6. `MyPostFilter` executes after the main filter chain.
7. The response is sent back to the client.

```
Client -> API Gateway (Filters) -> Microservice -> API Gateway -> Client
```

## Infrastructure

The API Gateway is designed to run as a Spring Boot application. Key infrastructure components include:

- Spring Cloud Gateway: Handles routing and filtering of requests.
- Eureka Client: Enables service discovery and registration.
- Spring Cloud Config: Provides centralized configuration management.
- Spring Cloud Bus AMQP: Enables dynamic configuration updates across instances.