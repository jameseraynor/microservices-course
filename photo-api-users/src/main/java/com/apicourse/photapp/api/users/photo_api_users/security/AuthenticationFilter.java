package com.apicourse.photapp.api.users.photo_api_users.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment environment;
    private UserService userService;

    public AuthenticationFilter(Environment environment, UserService userService,
            AuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.environment = environment;
        this.userService = userService;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {

        LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
            Authentication auth) {
        String userName = ((User) auth.getPrincipal()).getName();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);
        String tokenSecret = environment.getProperty("token.secret");
        byte[] tokenSecretBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(tokenSecretBytes);

        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(Long.parseLong(environment.getProperty("token.expriration_time")));
        Date expiryDate = Date.from(expiration);

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(expiryDate)
                .issuedAt(Date.from(now))
                .signWith(key)
                .compact();
        System.out.println("JWT token is " + token);
        UserDto userDetail = userService.getUserDetailsByEmail(userName);
    }
}
