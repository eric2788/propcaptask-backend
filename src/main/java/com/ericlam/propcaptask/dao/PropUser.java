package com.ericlam.propcaptask.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PropUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;

    private String email;

    private String password;

    private Date registered;

}
