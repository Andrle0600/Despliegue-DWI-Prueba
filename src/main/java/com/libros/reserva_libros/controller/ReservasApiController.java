package com.libros.reserva_libros.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libros.reserva_libros.model.Reserva;
import com.libros.reserva_libros.service.ReservaService;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/reservas")
public class ReservasApiController {

    @Autowired
    private ReservaService reservaService;

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerReservas() {
        return ResponseEntity.ok(reservaService.listarReservas());
    }

    // Obtener reservas pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<Reserva>> obtenerReservasPendientes() {
        return ResponseEntity.ok(reservaService.listarReservasPendientes());
    }

    // Obtener reservas atrasadas
    @GetMapping("/atrasadas")
    public ResponseEntity<List<Reserva>> obtenerReservasRetrasadas() {
        return ResponseEntity.ok(reservaService.listarReservasRetrasadas());
    }

    // Obtener las 10 más recientes (para dashboard)
    @GetMapping("/recientes")
    public ResponseEntity<List<Reserva>> obtenerReservasRecientes() {
        return ResponseEntity.ok(reservaService.listarReservasRecientes());
    }

    // Crear nueva reserva
    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(
            @RequestParam Long usuarioId,
            @RequestParam Long ejemplarId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaRecojo) {

        try {
            Reserva nuevaReserva = reservaService.crearReserva(usuarioId, ejemplarId, fechaRecojo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Actualizar una reserva existente
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(@PathVariable Long id, @RequestBody Reserva reservaActualizada) {
        if (!id.equals(reservaActualizada.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // Aquí puedes agregar lógica adicional si deseas evitar ciertas modificaciones (e.g., usuario o ejemplar no se cambia).
        Reserva actualizada = reservaService.guardarReserva(reservaActualizada);
        return ResponseEntity.ok(actualizada);
    }
}
