package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import com.ericlam.propcaptask.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class PropUserServiceImpl implements PropUserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public Page<PropUser> getUsersWithPage(int page) {
        return userRepository.findAll(Pageable.ofSize(15).withPage(page));
    }

    @Transactional
    @Override
    public void createUser(PropUser propUser) throws UsernameExistException {
        if (userRepository.findById(propUser.getUsername()).isPresent()) {
            throw new UsernameExistException();
        }
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
        if (user.getPassword().equals(current)) {
            throw new PasswordMismatchException();
        } else {
            user.setPassword(password);
            userRepository.save(user);
        }
    }
}
