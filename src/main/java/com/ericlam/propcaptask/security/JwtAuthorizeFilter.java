package com.ericlam.propcaptask.security;

import com.ericlam.propcaptask.service.JWTService;
import org.hibernate.annotations.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizeFilter extends BasicAuthenticationFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizeFilter.class);

    private final JWTService jwtService;

    public JwtAuthorizeFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            LOGGER.info("no header, skipped");
            chain.doFilter(request, response);
            return;
        }

        LOGGER.info("checking authentication");
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        LOGGER.info("authentication: "+authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            token = token.replace(TOKEN_PREFIX, "");

            String username = jwtService.getUsernameFromToken(token);

            if (username != null) {
                LOGGER.info("found token to username: "+username);
                if (!jwtService.validateToken(token, username)) {
                    LOGGER.info("the token is invalid.");
                    return null;
                }

                return new UsernamePasswordAuthenticationToken(username, token, List.of());
            }

            return null;
        }

        return null;
    }
}
