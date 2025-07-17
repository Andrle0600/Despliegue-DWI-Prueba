package com.libros.reserva_libros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.EstadoEjemplar;
import com.libros.reserva_libros.model.Libro;

public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    List<Ejemplar> findByLibro(Libro libro);

    List<Ejemplar> findByEstado(EstadoEjemplar estado);

    List<Ejemplar> findByLibroAndEstado(Libro libro, EstadoEjemplar estado);

    boolean existsByLibroAndEstado(Libro libro, EstadoEjemplar estado);
}
