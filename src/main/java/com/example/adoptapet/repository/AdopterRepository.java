package com.example.adoptapet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.adoptapet.model.Adopter;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Optional<Adopter> findByNombre(String nombre);
    Optional<Adopter> findByGithubId(String githubId);
}

