package com.ericlam.propcaptask.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PropUser {

    @Column(columnDefinition = "varchar(20)")
    @Id
    private String username;

    private String email;

    private String password;

    private Date registered;

}
