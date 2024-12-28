package com.api.course.photoapp.api.api_gateway;

import java.util.Base64;
import javax.crypto.SecretKey;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

/**
 * Gateway filter for JWT token authorization in API requests.
 * This filter validates the presence and authenticity of JWT tokens in request
 * headers.
 * 
 * @RefreshScope enables runtime configuration updates
 * @Component marks this as a Spring-managed component
 */
@Component
@RefreshScope
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    /**
     * Environment instance for accessing application properties
     */
    @Autowired
    Environment env;

    /**
     * Constructor initializing the filter with Config class
     */
    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    /**
     * Applies the filter to incoming requests
     * This method implements the main authorization logic
     *
     * @param config The filter configuration
     * @return GatewayFilter instance that performs the authorization
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Check if Authorization header exists
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header.", HttpStatus.UNAUTHORIZED);
            }

            // Extract JWT token from Authorization header
            String authorizationHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            // Validate the JWT token
            if (!isAuthorizationValid(jwt)) {
                return onError(exchange, "Authorization header is not valid.", HttpStatus.UNAUTHORIZED);
            }

            // If validation successful, continue with the filter chain
            return chain.filter(exchange);
        };
    }

    /**
     * Handles error responses for failed authorization
     *
     * @param exchange   The current server exchange
     * @param err        Error message to be logged
     * @param httpStatus HTTP status code to return
     * @return Mono<Void> completing the response
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     * Validates the JWT token
     * Checks if the token is properly signed and contains valid claims
     *
     * @param jwt The JWT token to validate
     * @return boolean indicating if the token is valid
     */
    private boolean isAuthorizationValid(String jwt) {
        boolean returnValue = true;
        String subject = null;

        // Get secret token from environment
        String secretToken = env.getProperty("token.secret");
        System.out.println("AuthorizationHeaderFilter Secret token: " + secretToken);

        // Validate secret token existence
        if (secretToken == null) {
            throw new IllegalArgumentException("Secret token cannot be null");
        }

        // Create signing key from secret
        byte[] secretBytes = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretBytes);

        // Create JWT parser with the signing key
        JwtParser jwtParser = (JwtParser) Jwts.parser()
                .verifyWith(secretKey) // Using verifyWith instead of deprecated setSigningKey
                .build();

        try {
            // Parse and validate the JWT token
            Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
            subject = claims.getSubject();
        } catch (Exception exception) {
            // If any exception occurs during parsing, token is invalid
            returnValue = false;
        }

        // Ensure subject claim exists and is not empty
        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * Configuration class for the filter
     * Can be extended to include additional configuration properties
     */
    public static class Config {
        // Configuration properties can be added here
    }
}
