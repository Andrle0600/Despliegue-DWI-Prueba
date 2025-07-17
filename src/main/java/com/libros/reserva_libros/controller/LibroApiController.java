package com.libros.reserva_libros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.service.LibroService;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/libros")
public class LibroApiController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public ResponseEntity<List<Libro>> obtenerLibros() {
        return ResponseEntity.ok(libroService.listarLibros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        return libroService.buscarLibroPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        Libro libroGuardado = libroService.guardarLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(libroGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @RequestBody Libro libro) {
        if (!libro.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Libro libroActualizado = libroService.guardarLibro(libro);
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Libro>> buscarLibros(@RequestParam String nombreLibro) {
        return ResponseEntity.ok(libroService.buscarPorTitulo(nombreLibro));
    }

    @GetMapping("/editoriales")
    public ResponseEntity<List<String>> obtenerEditoriales() {
        return ResponseEntity.ok(libroService.obtenerEditoriales());
    }

    @GetMapping("/generos")
    public ResponseEntity<List<String>> obtenerGeneros() {
        return ResponseEntity.ok(libroService.obtenerGeneros());
    }
}
