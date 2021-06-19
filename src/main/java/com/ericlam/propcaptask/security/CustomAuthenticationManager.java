package com.ericlam.propcaptask.security;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.service.PropUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final PropUserService propUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PropUser user = propUserService.getUser(authentication.getName());
        if (bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            return authentication;
        }
        throw new BadCredentialsException("Invalid Credentials.");
    }
}
