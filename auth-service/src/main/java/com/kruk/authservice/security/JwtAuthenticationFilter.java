package com.kruk.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.kruk.authservice.domain.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static com.kruk.authservice.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        User credentials;
        try {
            credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
        } catch (Exception ex) {
            logger.error("Can't authenticate user", ex);
            throw new InternalAuthenticationServiceException("Can't authenticate user");
        }

        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                credentials.getName(),
                credentials.getPassword(),
                Collections.emptyList()
        );
        return authenticationManager.authenticate(userToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        String token = jwtUtil.generateToken(((User) authResult.getPrincipal()).getName());
        response.addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token);
    }
}
