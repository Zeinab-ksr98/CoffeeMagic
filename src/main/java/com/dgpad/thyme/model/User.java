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

    @Pattern(regexp = "^[0-9]*$", message = "Phone must contain only digits")
    @Size(min = 3, message = "Phone number must be at least 3 digits")
    private String phone;
    private String about;
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

    public boolean deleted;
    private boolean enabled;
    public User(String username, String email, String password, String phone, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.enabled = true;
        this.deleted = false;
    }
}
