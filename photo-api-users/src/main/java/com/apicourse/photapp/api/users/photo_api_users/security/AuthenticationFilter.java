package com.apicourse.photapp.api.users.photo_api_users.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.apicourse.photapp.api.users.photo_api_users.service.UserService;
import com.apicourse.photapp.api.users.photo_api_users.shared.UserDto;
import com.apicourse.photapp.api.users.photo_api_users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import org.springframework.security.core.userdetails.User;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Custom authentication filter for processing user login requests and generating JWT tokens
 * Extends Spring Security's UsernamePasswordAuthenticationFilter
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment environment;
    private UserService userService;

    /**
     * Constructor for AuthenticationFilter
     * @param environment Spring Environment for accessing application properties
     * @param userService Service for user-related operations
     * @param authenticationManager Spring Security's authentication manager
     */
     public AuthenticationFilter(Environment environment, UserService userService,
            AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.environment = environment;
        this.userService = userService;
    }

    /**
     * Attempts to authenticate the user with provided credentials
     * @param req HTTP request containing login credentials
     * @param res HTTP response
     * @return Authentication object if successful
     * @throws AuthenticationException if authentication fails
     */
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        // Parse the incoming JSON request body into LoginRequestModel
        LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

        // Create and return a new authentication token with user credentials
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
    }

    /**
     * Handles successful authentication by generating a JWT token
     * @param req HTTP request
     * @param res HTTP response
     * @param chain Filter chain
     * @param auth Authentication object containing principal
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) {
        // Get username from the authenticated user
        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        // Prepare the secret key for JWT signing
        String tokenSecret = environment.getProperty("token.secret");
        System.out.println("AuthenticationFilter Token secret is " + tokenSecret);
        if (tokenSecret == null) {
            throw new IllegalArgumentException("Token secret cannot be null");
        }
        byte[] tokenSecretBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(tokenSecretBytes);

        // Calculate token expiration time
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(Long.parseLong(environment.getProperty("token.expriration_time")));
        System.out.println("Expiration time is " + environment.getProperty("token.expriration_time"));
        Date expiryDate = Date.from(expiration);

        // Build the JWT token
        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(expiryDate)
                .issuedAt(Date.from(now))
                .signWith(key)
                .compact();

        // Log the generated token (Consider removing in production)
        System.out.println("JWT token is " + token);

        res.addHeader("token", token);
        res.addHeader("userId", userDetails.getUserId());
    }
}
