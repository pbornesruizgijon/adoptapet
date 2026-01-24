package com.example.adoptapet.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.adoptapet.model.Adopter;
import com.example.adoptapet.model.Role;
import com.example.adoptapet.repository.AdopterRepository;
import com.example.adoptapet.repository.RoleRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultService = new DefaultOAuth2UserService();

    @Autowired
    AdopterRepository adopterRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = defaultService.loadUser(request);

        String githubId = oAuth2User.getAttribute("login");
       
        // Buscar por GitHub ID
        Adopter adopter = adopterRepo.findByGithubId(githubId)
                .orElse(new Adopter());

        adopter.setGithubId(githubId);
        adopter.setEmail(oAuth2User.getAttribute("email"));
        adopter.setNombre(oAuth2User.getAttribute("name"));

        Role userRole = roleRepo.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Role USER no encontrado"));
        adopter.setRoles(Set.of(userRole));

        adopterRepo.save(adopter);

        return oAuth2User;
    }
}
