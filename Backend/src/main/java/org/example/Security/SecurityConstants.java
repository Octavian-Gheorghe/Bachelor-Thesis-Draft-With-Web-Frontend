package org.example.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SecurityConstants {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    public static String JWT_SECRET;
    public static long JWT_EXPIRATION;

    @PostConstruct
    public void init() {
        JWT_SECRET = this.jwtSecret;
        JWT_EXPIRATION = this.jwtExpiration;
    }
}
