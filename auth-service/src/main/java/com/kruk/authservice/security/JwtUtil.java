package com.kruk.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.kruk.authservice.security.SecurityConstants.TOKEN_PREFIX;

@Component
public class JwtUtil {

    @Value("${jwt.expiration-time}")
    private Long expirationTime;

    @Autowired
    public Algorithm algorithm;

    public Date makeExpirationDate() {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    public String generateToken(String subject) {
        return JWT.create()
                .withIssuer("http://asfdff.com")
                .withIssuedAt(new Date())
                .withExpiresAt(makeExpirationDate())
                .withSubject(subject)
                .withExpiresAt(makeExpirationDate())
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }
}
