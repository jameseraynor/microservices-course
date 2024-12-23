package com.api.course.photoapp.api.api_gateway;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    Environment env;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization header.", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isAuthorizationValid(jwt)) {
                return onError(exchange, "Authorization header is not valid.", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthorizationValid(String jwt) {
        boolean returnValue = true;
        String subject = null;
        String secretToken = env.getProperty("token.secret");
        if (secretToken == null) {
            throw new IllegalArgumentException("Secret token cannot be null");
        }
        byte[] secretBytes = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretBytes);
        // To parse access token and to read it's claims we will need to use JWT parser
        JwtParser jwtParser = (JwtParser) Jwts.parser()
                .verifyWith(secretKey) // always use verifyWith instead of setSigningKey bcz it is Deprecated
                .build();

        try {
            Claims claims = jwtParser.parseSignedClaims(jwt).getPayload();
            subject = claims.getSubject();
        } catch (Exception exception) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    public static class Config {
        // Put configuration properties here
    }

}
