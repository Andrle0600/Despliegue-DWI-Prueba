package com.libros.reserva_libros.service;

import com.libros.reserva_libros.dto.*;
import com.libros.reserva_libros.model.*;
import com.libros.reserva_libros.repository.*;
import com.libros.reserva_libros.config.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setDni(request.getDni());
        user.setNombres(request.getNombres());
        user.setApellidos(request.getApellidos());

        // Convertir String a java.sql.Date
        user.setFechaNacimiento(Date.valueOf(request.getFechaNacimiento()));

        user.setGenero(request.getGenero());
        user.setTelefono(request.getTelefono());
        user.setDireccion(request.getDireccion());
        user.setRoles(Set.of(role));

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
