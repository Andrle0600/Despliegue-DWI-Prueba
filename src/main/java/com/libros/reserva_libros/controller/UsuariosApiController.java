package com.libros.reserva_libros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.service.UserService;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/usuarios")
public class UsuariosApiController {

    @Autowired
    private UserService userService;

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> listarUsuarios() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<User> obtenerUsuarioPorId(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Desactivar usuario
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        boolean exito = userService.deshabilitarUsuario(id);
        return exito ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Activar usuario (opcional)
    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable Long id) {
        boolean exito = userService.habilitarUsuario(id);
        return exito ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Buscar usuarios por nombre o apellido
    @GetMapping("/buscar")
    public ResponseEntity<List<User>> buscarPorNombreOApellido(@RequestParam("q") String termino) {
        return ResponseEntity.ok(userService.buscarPorNombreOApellido(termino));
    }
}
