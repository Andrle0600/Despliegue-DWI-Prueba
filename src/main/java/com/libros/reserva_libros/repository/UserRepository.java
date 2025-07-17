package com.libros.reserva_libros.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libros.reserva_libros.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // El email es el username
    boolean existsByDni(String dni);
    Optional<User> findByUsername(String username);
    List<User> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String termino, String termino2);


}
