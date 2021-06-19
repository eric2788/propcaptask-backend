package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.model.AuthRequest;
import com.ericlam.propcaptask.model.RegisterRequest;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    String login(AuthRequest authRequest) throws BadCredentialsException;

    String register(RegisterRequest registerRequest) throws UsernameExistException;

    void logout(String token) throws ExpiredJwtException;

    String updatePassword(String username, String current, String newPassword) throws PasswordMismatchException;

}
