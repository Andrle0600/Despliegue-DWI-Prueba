package com.libros.reserva_libros.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.EstadoEjemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.repository.EjemplarRepository;

@Service
public class EjemplarService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Ejemplar> listarEjemplares() {
        return ejemplarRepository.findAll();
    }

    

    public Ejemplar guardarEjemplar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    public Optional<Ejemplar> buscarEjemplarporId(Long id) {
        return ejemplarRepository.findById(id);
    }

    public void actualizarEjemplar(Ejemplar ejemplarActualizado) {
        Ejemplar ejemplarExistente = ejemplarRepository.findById(ejemplarActualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ejemplar no encontrado"));

        ejemplarExistente.setCodigoEjemplar(ejemplarActualizado.getCodigoEjemplar());
        ejemplarExistente.setEstado(ejemplarActualizado.getEstado());
        ejemplarExistente.setUbicacion(ejemplarActualizado.getUbicacion());

        // El libro asociado no debería cambiar aquí, pero si llega se mantiene.
        ejemplarRepository.save(ejemplarExistente);
    }

    public void eliminarEjemplar(Long id) {
        ejemplarRepository.deleteById(id);
    }

    public List<EstadoEjemplar> getEstados() {
        return Arrays.asList(EstadoEjemplar.values());
    }

    public List<Ejemplar> listarEjemplaresDisponibles(List<Ejemplar> ejemplares) {
        return ejemplarRepository.findByEstado(EstadoEjemplar.DISPONIBLE);
    }

    public List<Ejemplar> listarEjemplaresDisponibles(Libro libro) {
        return ejemplarRepository.findByLibroAndEstado(libro, EstadoEjemplar.DISPONIBLE);
    }

    public boolean hayEjemplarDisponiblePorLibro(Libro libro) {
        return ejemplarRepository.existsByLibroAndEstado(libro, EstadoEjemplar.DISPONIBLE);
    }
}
