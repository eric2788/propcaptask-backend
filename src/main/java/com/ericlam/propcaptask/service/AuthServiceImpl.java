package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.model.AuthRequest;
import com.ericlam.propcaptask.model.RegisterRequest;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final PropUserService propUserService;


    @Override
    public String login(AuthRequest authRequest) throws BadCredentialsException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public String register(RegisterRequest registerRequest) throws UsernameExistException {
        propUserService.createUser(new PropUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword(), new Date()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(registerRequest.getUsername());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public void logout(String token) throws ExpiredJwtException {
        jwtService.invalidateToken(token);
    }

    @Override
    public String updatePassword(String username, String current, String newPassword) throws PasswordMismatchException {
        propUserService.updatePassword(username, current, newPassword);
        return jwtService.generateToken(new User(username, newPassword, List.of()));
    }
}
