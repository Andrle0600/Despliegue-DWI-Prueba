package com.libros.reserva_libros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.AuthenticationException;

import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.service.UserService;
import com.libros.reserva_libros.dto.LoginRequest;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Validated @RequestBody User user,
            BindingResult result) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Corrige los errores del formulario");
        }

        try {
            userService.registrarUsuario(
                    user.getDni(),
                    user.getNombres(),
                    user.getApellidos(),
                    user.getFechaNacimiento(),
                    user.getGenero(),
                    user.getUsername(),
                    user.getTelefono(),
                    user.getDireccion(),
                    user.getPassword());

            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.encontrarPorUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return ResponseEntity.ok(user); // Puedes retornar solo los datos necesarios
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}
