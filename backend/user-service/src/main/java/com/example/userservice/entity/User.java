package com.example.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users", schema = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // UNIQUE

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotNull
    private String email; // UNIQUE

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "role", nullable = false)
    @Pattern(regexp = "ROLE_ADMIN|ROLE_PLAYER", message = "the role must be ROLE_ADMIN|ROLE_PLAYER") // if we have this code do we need to define admin as a separate class?
    private String role;

    // The error is somewhere here
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ClanUser> clanUser;

    // Separate constructor so that Admin can use super()
    public User(Long userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
