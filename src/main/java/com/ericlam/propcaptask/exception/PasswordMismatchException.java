package com.ericlam.propcaptask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordMismatchException extends AuthenticationException {
    public PasswordMismatchException(String msg) {
        super(msg);
    }
}
