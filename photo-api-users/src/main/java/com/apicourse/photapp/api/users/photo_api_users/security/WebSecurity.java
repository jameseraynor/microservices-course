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

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private Environment environment;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.environment = environment;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // Configure authentication manager
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Disable CSRF protection and configure H2 console access
        http.csrf(csrfConfigurer -> {
            csrfConfigurer.disable();
            csrfConfigurer.ignoringRequestMatchers(PathRequest.toH2Console());
        });

        // Configure authorization rules for HTTP requests
        http.authorizeHttpRequests(auth -> auth
                // Allow unrestricted access to H2 console and user registration endpoint
                .requestMatchers(
                        PathRequest.toH2Console(),
                        AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/users"))
                .permitAll()
                // Restrict access to /users/** endpoints to requests from specified gateway IP
                .requestMatchers(new AntPathRequestMatcher("/users/**")).access(
                        new WebExpressionAuthorizationManager(
                                "hasIpAddress('" + environment.getProperty("gateway.ip") + "')"))
                // Require authentication for all other requests
                .anyRequest().authenticated());

        http.addFilter(new AuthenticationFilter(environment, userService ,authenticationManager));

        http.authenticationManager(authenticationManager);
        // Configure session management to be stateless (no session cookies)
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Configure frame options to allow H2 console to work properly in same origin
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }
}