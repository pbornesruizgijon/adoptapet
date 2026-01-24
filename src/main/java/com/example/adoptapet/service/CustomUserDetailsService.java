package com.example.adoptapet.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.CustomUserDetails;
import com.example.adoptapet.repository.AdopterRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdopterRepository adopterRepository;

    public CustomUserDetailsService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // GitHub ID
        return adopterRepository.findByGithubId(username)
                .map(adopter -> new CustomUserDetails(adopter))
                .orElseGet(() -> {
                    // Nombre local
                    Adopter adopter = adopterRepository.findByNombre(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
                    return new CustomUserDetails(adopter);
                });
    }
    
}
