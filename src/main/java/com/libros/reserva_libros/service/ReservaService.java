package com.libros.reserva_libros.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.EstadoEjemplar;
import com.libros.reserva_libros.model.EstadoReserva;
import com.libros.reserva_libros.model.Reserva;
import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.repository.EjemplarRepository;
import com.libros.reserva_libros.repository.ReservaRepository;
import com.libros.reserva_libros.repository.UserRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva crearReserva(Long usuarioId, Long ejemplarId, LocalDate fechaRecojo) {
    User usuario = userRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    Ejemplar ejemplar = ejemplarRepository.findById(ejemplarId).orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

    ejemplar.setEstado(EstadoEjemplar.RESERVADO);
    ejemplarRepository.save(ejemplar);

    Reserva reserva = new Reserva();
    reserva.setUser(usuario);
    reserva.setEjemplar(ejemplar);
    LocalDateTime fechaInicio = fechaRecojo.atStartOfDay();
    reserva.setFechaReserva(fechaInicio);
    reserva.setFechaEstimadaDevolucion(fechaInicio.plusDays(7));
    reserva.setFechaRealDevolucion(null);
    reserva.setEstado(EstadoReserva.PENDIENTE);

    return reservaRepository.save(reserva);
}

    public List<Reserva> listarReservasPendientes() {
        return reservaRepository.findByEstado(EstadoReserva.PENDIENTE);
    }

    public List<Reserva> listarReservasRetrasadas() {
        return reservaRepository.buscarReservasRetrasadas();
    }

    public List<Reserva> listarReservasRecientes() {
        return reservaRepository.findTop10ByOrderByFechaReservaDesc();
    }
}
