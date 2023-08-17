package com.kruk.gateway.security;

import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
@ConfigurationProperties("jwt")
@Setter
public class KeyConfiguration {

    private String secret;

    @Bean
    public Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
