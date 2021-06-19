package com.ericlam.propcaptask.controller;

import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.model.*;
import com.ericlam.propcaptask.service.AuthService;
import com.ericlam.propcaptask.service.PropUserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final PropUserService propUserService;
    private final AuthService authService;


    @GetMapping
    public ResponseEntity<IndexResponse> index(){
        LOGGER.info("printing Greeting");
        return ResponseEntity.ok(new IndexResponse("running", "hello world"));
    }


    @GetMapping("users")
    public ResponseEntity<PropUsersResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "sortBy", defaultValue = "username") String sortBy,
                                                      @RequestParam(value = "descending", defaultValue = "false") boolean descending) {
        Sort sort = Sort.by(sortBy);
        if (descending) sort = sort.descending();
        Page<PropUsersResponse.PropUserDetail> users = propUserService.getUsersWithPage(page, sort).map(PropUsersResponse.PropUserDetail::new);
        LOGGER.info("listing users with total size: "+users.getContent().size());
        var res = new PropUsersResponse((int) users.getTotalElements(), users.getTotalPages(), users.getContent());
        return ResponseEntity.ok(res);
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws HttpClientErrorException {
        LOGGER.info("doing login for: "+authRequest.getUsername());
        try {
            String token = authService.login(authRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid Credentials.");
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws HttpClientErrorException{
        LOGGER.info("doing register for: "+registerRequest.getUsername());
        try {
            String token = authService.register(registerRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UsernameExistException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Username already exist.");
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            LOGGER.info("doing logout for: "+authentication.getName());
            authService.logout(authentication.getCredentials().toString());
            return ResponseEntity.ok().build();
        } catch (JwtException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (AuthenticationException e){
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            LOGGER.info("doing changepassword for: "+authentication.getName());
            String token = authService.updatePassword(authentication.getName(), passwordChangeRequest.getPw(), passwordChangeRequest.getNew_pw());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
