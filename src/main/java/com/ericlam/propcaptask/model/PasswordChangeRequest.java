package com.ericlam.propcaptask.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PasswordChangeRequest {

    @NonNull
    private final String pw;
    @NonNull
    private final String new_pw;
}
