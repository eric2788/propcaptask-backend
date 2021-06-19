package com.ericlam.propcaptask.service;

import com.ericlam.propcaptask.dao.PropUser;
import com.ericlam.propcaptask.exception.PasswordMismatchException;
import com.ericlam.propcaptask.exception.UsernameExistException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface PropUserService {

    Page<PropUser> getUsersWithPage(int page);

    void createUser(PropUser propUser) throws UsernameExistException;

    PropUser getUser(String username) throws UsernameNotFoundException;

    void updatePassword(String username, String current, String password) throws PasswordMismatchException;

}
