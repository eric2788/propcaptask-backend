package com.ericlam.propcaptask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class AuthRequest {

    @NonNull
    private final String username;
    @NonNull
    private final String password;

}
