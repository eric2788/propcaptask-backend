package com.ericlam.propcaptask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<?> handleHttpStatusError(HttpClientErrorException e, WebRequest request) {
        boolean stackTrace = Boolean.parseBoolean(request.getParameter("trace"));
        return new ResponseEntity<>(
                new ErrorResponse(e.getClass().getSimpleName(),
                        e.getRawStatusCode(),
                        e.getMessage(),
                        stackTrace ? Arrays.stream(e.getStackTrace())
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList()) : List.of()), e.getStatusCode());
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {
        private final Date timestamp = new Date();
        private final String error;
        private final int status;
        private final String message;
        private final List<String> trace;
    }
}
