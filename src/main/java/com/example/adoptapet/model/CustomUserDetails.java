package com.example.adoptapet.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private Adopter adopter;
    
    public CustomUserDetails(Adopter adopter) { this.adopter = adopter; }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return adopter.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
            .collect(Collectors.toList());
    }
    
    @Override public String getPassword() { return adopter.getPassword() != null ? adopter.getPassword() : ""; }
    @Override public String getUsername() { return adopter.getGithubId() != null ? adopter.getGithubId() : adopter.getNombre(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
