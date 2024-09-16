package com.example.practiceauth2.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
    private String provider;

    private String password;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @Setter
    @Column(nullable = false)
    private boolean active;

    @Setter
    private LocalDateTime lastLogin;

    public Account(String email, String name, String provider, Set<String> roles) {
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.roles = roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> (GrantedAuthority) () -> role).toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
