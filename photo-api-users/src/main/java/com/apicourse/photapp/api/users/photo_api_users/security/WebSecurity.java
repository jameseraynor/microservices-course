package com.apicourse.photapp.api.users.photo_api_users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.apicourse.photapp.api.users.photo_api_users.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;

/**
 * Security configuration class for the application.
 * Handles all security-related configurations including authentication,
 * authorization,
 * and security filter chains.
 */
@Configuration
@EnableWebSecurity
public class WebSecurity {

    // Core dependencies required for security configuration
    private Environment environment; // For accessing application properties
    private UserService userService; // For user-related operations
    private BCryptPasswordEncoder bCryptPasswordEncoder; // For password encryption

    /**
     * Constructor to initialize security configuration with required dependencies
     * 
     * @param environment           Application environment properties
     * @param userService           Service for user operations
     * @param bCryptPasswordEncoder Password encoder instance
     */
    @Autowired
    public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Configures the security filter chain for HTTP security
     * 
     * @param http HttpSecurity instance to be configured
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // Step 1: Configure Authentication Manager
        // Sets up the authentication manager with user details service and password
        // encoder
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(environment, userService,
                authenticationManager);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        // Step 2: Configure CSRF Protection
        // Disable CSRF and configure H2 console access
        http.csrf(csrfConfigurer -> {
            csrfConfigurer.disable(); // Disable CSRF protection
            csrfConfigurer.ignoringRequestMatchers(PathRequest.toH2Console()); // Ignore CSRF for H2 console
        });

        // Step 3: Configure Authorization Rules
        http.authorizeHttpRequests(auth -> auth
                // Allow public access to H2 console and user registration
                .requestMatchers(
                        PathRequest.toH2Console(),
                        AntPathRequestMatcher.antMatcher("/users"))
                .permitAll()
                // Allow public access to login endpoint
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                // Restrict access to /users/** endpoints to requests from gateway IP only
                .requestMatchers(new AntPathRequestMatcher("/users/**")).access(
                        new WebExpressionAuthorizationManager(
                                "hasIpAddress('" + environment.getProperty("gateway.ip") + "')"))
                // Require authentication for all other requests
                .anyRequest().authenticated());

        // Step 4: Add Custom Authentication Filter
        // Adds our custom authentication filter to the security chain
        http.addFilter(authenticationFilter);

        // Step 5: Set Authentication Manager
        http.authenticationManager(authenticationManager);

        // Step 6: Configure Session Management
        // Set session creation policy to STATELESS for REST API
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Step 7: Configure Frame Options
        // Allow H2 console to be displayed in frames from same origin
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }
}
