package com.libros.reserva_libros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libros.reserva_libros.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    
    Optional<Role> findByName(String name); // debe retornar Optional<Role>
}
