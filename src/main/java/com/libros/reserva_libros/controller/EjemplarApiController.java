package com.libros.reserva_libros.controller;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.EstadoEjemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.service.EjemplarService;
import com.libros.reserva_libros.service.LibroService;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/ejemplares")
public class EjemplarApiController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private LibroService libroService;

    @GetMapping
    public ResponseEntity<List<Ejemplar>> obtenerEjemplares() {
        return ResponseEntity.ok(ejemplarService.listarEjemplares());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ejemplar> obtenerEjemplar(@PathVariable Long id) {
        return ejemplarService.buscarEjemplarporId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ejemplar> crearEjemplar(@RequestBody Ejemplar ejemplar) {
        // Verificar que el libro asociado existe
        if (ejemplar.getLibro() != null && ejemplar.getLibro().getId() != null) {
            Optional<Libro> libro = libroService.buscarLibroPorId(ejemplar.getLibro().getId());
            if (libro.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            ejemplar.setLibro(libro.get());
        }

        Ejemplar ejemplarGuardado = ejemplarService.guardarEjemplar(ejemplar);
        System.out.println("Datos recibidos: " + ejemplar.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarGuardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ejemplar> actualizarEjemplar(@PathVariable Long id, @RequestBody Ejemplar ejemplar) {
        if (!ejemplar.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        // Verificar que el ejemplar existe
        Optional<Ejemplar> ejemplarExistente = ejemplarService.buscarEjemplarporId(id);
        if (ejemplarExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Mantener la relación con el libro original si no se proporciona uno nuevo
        if (ejemplar.getLibro() == null) {
            ejemplar.setLibro(ejemplarExistente.get().getLibro());
        } else if (ejemplar.getLibro().getId() != null) {
            // Verificar que el nuevo libro existe
            Optional<Libro> libro = libroService.buscarLibroPorId(ejemplar.getLibro().getId());
            if (libro.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            ejemplar.setLibro(libro.get());
        }

        Ejemplar ejemplarActualizado = ejemplarService.guardarEjemplar(ejemplar);
        return ResponseEntity.ok(ejemplarActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEjemplar(@PathVariable Long id) {
        ejemplarService.eliminarEjemplar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoEjemplar>> obtenerEstadosEjemplar() {
        return ResponseEntity.ok(ejemplarService.getEstados());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Ejemplar>> obtenerEjemplaresDisponibles() {
        return ResponseEntity.ok(ejemplarService.listarEjemplaresDisponibles(ejemplarService.listarEjemplares()));
    }

    @GetMapping("/disponibles-por-libro/{libroId}")
    public ResponseEntity<List<Ejemplar>> obtenerEjemplaresDisponiblesPorLibro(@PathVariable Long libroId) {
        Optional<Libro> libro = libroService.buscarLibroPorId(libroId);
        if (libro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ejemplarService.listarEjemplaresDisponibles(libro.get()));
    }

    @GetMapping("/existe-disponible-por-libro/{libroId}")
    public ResponseEntity<Boolean> existeEjemplarDisponiblePorLibro(@PathVariable Long libroId) {
        Optional<Libro> libro = libroService.buscarLibroPorId(libroId);
        if (libro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ejemplarService.hayEjemplarDisponiblePorLibro(libro.get()));
    }
}