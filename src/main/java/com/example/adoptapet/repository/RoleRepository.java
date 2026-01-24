package com.example.adoptapet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.adoptapet.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByNombre(String nombre);  
    Optional<Role> findByNombre(String nombre);
}
