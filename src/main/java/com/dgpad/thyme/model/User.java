package com.dgpad.thyme.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @NotBlank(message = "Username cannot be blank")
    @Column(nullable = false, unique = true)
    private String username;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    @Column(nullable = false, unique = true)
    public String email;


    @Column(nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters")
    public String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    public Boolean IsAdministrative=false;

    public boolean deleted;
    private boolean enabled;
    public User(String username, String email, String password, Role role,Boolean IsAdministrative) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.deleted = false;
    }
}
