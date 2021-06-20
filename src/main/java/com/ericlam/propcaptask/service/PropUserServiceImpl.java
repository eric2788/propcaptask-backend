package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PropUserServiceImpl implements PropUserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PropUserService.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public Page<PropUser> getUsersWithPage(int page, Sort sort) {
        return userRepository.findAll(PageRequest.of(Math.max(page - 1, 0), 15, sort));
    }

    @Transactional
    @Override
    public void createUser(PropUser propUser) throws UsernameExistException {
        if (userRepository.findById(propUser.getUsername()).isPresent()) {
            throw new UsernameExistException();
        }
        propUser.setPassword(bCryptPasswordEncoder.encode(propUser.getPassword()));
        userRepository.save(propUser);
    }

    @Transactional
    @Override
    public PropUser getUser(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    @Override
    public void updatePassword(String username, String current, String password) throws PasswordMismatchException, UsernameNotFoundException {
        PropUser user = getUser(username);
        if (!bCryptPasswordEncoder.matches(current, user.getPassword())) {
            throw new PasswordMismatchException("Your current password is incorrect.");
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        }
    }
}
