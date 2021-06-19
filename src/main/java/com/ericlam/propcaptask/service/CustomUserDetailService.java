package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.dao.PropUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final PropUserService propUserService;

    public CustomUserDetailService(PropUserService propUserService) {
        this.propUserService = propUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        PropUser user = propUserService.getUser(s);
        return new User(user.getUsername(), user.getPassword(), List.of());
    }
}
