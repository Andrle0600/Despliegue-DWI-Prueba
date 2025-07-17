package com.libros.reserva_libros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.libros.reserva_libros.model.EstadoReserva;
import com.libros.reserva_libros.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findTop10ByOrderByFechaReservaDesc();

    @Query("""
    SELECT r FROM Reserva r
    WHERE r.fechaEstimadaDevolucion < CURRENT_TIMESTAMP
    AND r.fechaRealDevolucion IS NULL
    AND r.estado = EstadoReserva.PENDIENTE
    """)
    List<Reserva> buscarReservasRetrasadas();

}
