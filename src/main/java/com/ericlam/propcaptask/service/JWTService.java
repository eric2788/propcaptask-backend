package com.ericlam.propcaptask.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface JWTService {

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    boolean isTokenExpired(String token);

    String generateToken(UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);

    void invalidateToken(String token);

}
