# Photo Discovery Service for Microservices Architecture

The Photo Discovery Service is a Spring Boot application that serves as a Eureka server for service discovery in a microservices architecture. It enables dynamic registration and discovery of microservices within the photo application ecosystem.

This service is built using Spring Cloud Netflix Eureka Server, providing a robust and scalable solution for managing service instances. By centralizing service registration and discovery, it simplifies the process of inter-service communication and load balancing in a distributed system.

The Photo Discovery Service is designed to be highly available and supports dynamic configuration updates through the @RefreshScope annotation. This feature allows for runtime changes to the service's configuration without requiring a restart, enhancing the flexibility and maintainability of the microservices infrastructure.

## Repository Structure

```
.
└── photo-discovery-service
    ├── pom.xml
    └── src
        ├── main
        │   └── java
        │       └── com
        │           └── apicourse
        │               └── photoapp
        │                   └── discovery
        │                       └── photo_discovery_service
        │                           └── PhotoDiscoveryServiceApplication.java
        └── test
            └── java
                └── com
                    └── apicourse
                        └── photoapp
                            └── discovery
                                └── photo_discovery_service
                                    └── PhotoDiscoveryServiceApplicationTests.java
```

Key Files:
- `pom.xml`: Maven project configuration file
- `PhotoDiscoveryServiceApplication.java`: Main application entry point
- `PhotoDiscoveryServiceApplicationTests.java`: Test class for the application

## Usage Instructions

### Installation

Prerequisites:
- Java Development Kit (JDK) 21
- Maven 3.6.3 or later

Steps:
1. Clone the repository:
   ```
   git clone <repository-url>
   cd photo-discovery-service
   ```

2. Build the project:
   ```
   mvn clean install
   ```

### Getting Started

To run the Photo Discovery Service:

```
java -jar target/photo-discovery-service-0.0.1-SNAPSHOT.jar
```

By default, the Eureka server will start on port 8761. You can access the Eureka dashboard by navigating to `http://localhost:8761` in your web browser.

### Configuration

The service can be configured using Spring Boot's standard configuration mechanisms. Key configuration properties include:

- `server.port`: The port on which the Eureka server will run (default: 8761)
- `eureka.client.register-with-eureka`: Whether to register this server with Eureka (default: false for standalone mode)
- `eureka.client.fetch-registry`: Whether to fetch the registry information from Eureka (default: false for standalone mode)

Example `application.properties`:

```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

### Integration

To integrate a microservice with the Photo Discovery Service:

1. Add the Eureka Client dependency to your microservice's `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Enable the Eureka Client in your microservice's main application class:

```java
@SpringBootApplication
@EnableEurekaClient
public class MicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
    }
}
```

3. Configure the Eureka server URL in your microservice's `application.properties`:

```properties
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
```

### Testing & Quality

To run the tests:

```
mvn test
```

The `PhotoDiscoveryServiceApplicationTests` class includes a basic context load test to ensure the application starts correctly.

### Troubleshooting

Common issues and solutions:

1. Eureka server not starting:
   - Error: "Port 8761 already in use"
   - Solution: Change the port in `application.properties`:
     ```properties
     server.port=8762
     ```

2. Microservices not registering with Eureka:
   - Problem: Microservices cannot connect to the Eureka server
   - Diagnostic steps:
     1. Check if the Eureka server is running and accessible
     2. Verify the Eureka client configuration in the microservice
   - Solution: Ensure the `eureka.client.serviceUrl.defaultZone` property is correctly set in the microservice's configuration

Debugging:
- Enable debug logging by adding the following to `application.properties`:
  ```properties
  logging.level.com.netflix.eureka=DEBUG
  logging.level.com.netflix.discovery=DEBUG
  ```
- Log files are typically located in the `logs` directory of the application root

Performance optimization:
- Monitor the number of registered instances and their health status through the Eureka dashboard
- For large-scale deployments, consider setting up a Eureka cluster for high availability

## Data Flow

The Photo Discovery Service manages the registration and discovery of microservices within the photo application ecosystem. Here's an overview of the data flow:

1. Eureka Server Startup: The PhotoDiscoveryServiceApplication initializes and starts the Eureka server.
2. Microservice Registration: Client microservices register themselves with the Eureka server by sending their metadata.
3. Heartbeat Mechanism: Registered microservices send periodic heartbeats to the Eureka server to maintain their registration.
4. Service Discovery: Client applications query the Eureka server to discover available service instances.
5. Load Balancing: Clients use the discovered service information for load-balanced requests.

```
+----------------+        +------------------------+        +----------------+
|                |        |                        |        |                |
| Microservice A | -----> | Photo Discovery Service| <----- | Microservice B |
|                | Register|                        | Register|                |
+----------------+        |                        |        +----------------+
        ^                 |                        |                 ^
        |                 |                        |                 |
        |                 |                        |                 |
        |                 +------------------------+                 |
        |                           ^                                |
        |                           |                                |
        |                           |                                |
        |                  +------------------+                      |
        |                  |                  |                      |
        +------------------|  Client Service  |----------------------+
                     Discover|                  |Discover
                           +------------------+
```

Note: The Eureka server maintains an in-memory registry of service instances, which is replicated across the cluster in a production environment for high availability.