package com.ericlam.propcaptask.model;

import lombok.Getter;

@Getter
public class PasswordChangeRequest {
    private String pw;
    private String new_pw;
}
