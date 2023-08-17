package com.kruk.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;

    @Autowired
    public JwtUtil(Key key) {
        this.key = key;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String tokenString) {
        String jwtToken = tokenString.replace(SecurityConstants.TOKEN_PREFIX, "");
        boolean result = true;
        try {
            result = getAllClaimsFromToken(jwtToken)
                    .getExpiration()
                    .before(new Date());
        } catch (Exception ex) {
            log.info("An exception was thrown during JWT verification: {}", ex.getMessage());
        }
        return result;
    }

    public boolean isInvalid(String token) {
        return isTokenExpired(token);
    }
}
