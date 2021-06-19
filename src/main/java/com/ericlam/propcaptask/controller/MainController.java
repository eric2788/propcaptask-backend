package com.ericlam.propcaptask.controller;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.model.*;
import com.ericlam.propcaptask.service.AuthService;
import com.ericlam.propcaptask.service.PropUserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class MainController {

    private final PropUserService propUserService;
    private final AuthService authService;


    @GetMapping("users")
    public ResponseEntity<PropUsersResponse> getUsers(@RequestParam("page") int page) {
        Page<PropUser> users = propUserService.getUsersWithPage(page);
        var res = new PropUsersResponse(users.getSize(), users.getContent().stream().map(PropUsersResponse.PropUserDetail::new).collect(Collectors.toList()));
        return ResponseEntity.ok(res);
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = authService.login(authRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid Credentials."));
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String token = authService.register(registerRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UsernameExistException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Username already exist."));
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (authentication == null) throw new JwtException("Invalid Credentials.");
            authService.logout(authentication.getCredentials().toString());
            return ResponseEntity.ok().build();
        } catch (JwtException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid Credentials."));
        try {
            String token = authService.updatePassword(authentication.getName(), passwordChangeRequest.getPw(), passwordChangeRequest.getNew_pw());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (PasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Password mismatch."));
        }
    }
}
