package com.ericlam.propcaptask.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JWTService {

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    boolean isTokenExpired(String token);

    String generateToken(String username);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

    boolean validateToken(String token, String username);

    void invalidateToken(String token);

}
