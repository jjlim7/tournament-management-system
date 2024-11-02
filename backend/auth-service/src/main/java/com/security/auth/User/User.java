package com.security.auth.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.auth.Security.model.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")  // Use a different name to avoid conflict with the "user" keyword
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="email", nullable = false, unique = true)
    @Email @NotNull
    private String email;
    @Column(name="password", nullable = false)
    @NotNull
    private String password;
    @Column(name="role", nullable = false)
    @Pattern(regexp = "ROLE_ADMIN|ROLE_PLAYER",
            message = "the role must be ROLE_ADMIN|ROLE_PLAYER")
    @NotNull
    private String role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Token> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public List<Token> getTokens() {
        return tokens;
    }

}
