package com.kruk.authservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "auth_user_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    public User(
            String name,
            String password
    ) {
        this.name = name;
        this.password = password;
    }

}


