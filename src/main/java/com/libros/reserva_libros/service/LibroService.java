package com.libros.reserva_libros.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.repository.LibroRepository;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosAsc() {
        return libroRepository.findAllByOrderByTituloAsc();
    }

    public List<Libro> listarLibrosDesc() {
        return libroRepository.findAllByOrderByTituloDesc();
    }

    public Libro guardarLibro(Libro libro) {
        Long idActual = libro.getId();

        if (libro.getSlug() == null || libro.getSlug().isEmpty()) {
            libro.setSlug(generarSlugUnico(libro.getTitulo(), idActual));
        } else {
            if (libroRepository.existsBySlugAndIdNot(libro.getSlug(), idActual)) {
                libro.setSlug(generarSlugUnico(libro.getTitulo(), idActual));
            }
        }

        return libroRepository.save(libro);
    }

    public Optional<Libro> buscarLibroPorId(Long id) {
        return libroRepository.findById(id);
    }

    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }

    public void actualizar(Libro libroActualizado) {
        Libro libroExistente = libroRepository.findById(libroActualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        libroExistente.setTitulo(libroActualizado.getTitulo());
        libroExistente.setAutor(libroActualizado.getAutor());
        libroExistente.setIsbn(libroActualizado.getIsbn());
        libroExistente.setEditorial(libroActualizado.getEditorial());
        libroExistente.setGenero(libroActualizado.getGenero());
        libroExistente.setFechaPublicacion(libroActualizado.getFechaPublicacion());

        // Regeneramos slug si cambió el título
        libroExistente.setSlug(generarSlugUnico(libroActualizado.getTitulo(), libroExistente.getId()));

        libroRepository.save(libroExistente);
    }

    
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.buscarPorTitulo(titulo);
    }

    public Optional<Libro> buscarPorSlug(String slug) {
        return libroRepository.findBySlug(slug);
    }

    // generar identificadores para url
    public String generarSlug(String texto) {
        return texto.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }

    private String generarSlugUnico(String titulo, Long idActual) {
        String baseSlug = generarSlug(titulo);
        String slug = baseSlug;
        int contador = 1;

        while ((idActual == null && libroRepository.findBySlug(slug).isPresent())
                || (idActual != null && libroRepository.existsBySlugAndIdNot(slug, idActual))) {
            slug = baseSlug + "-" + contador;
            contador++;
        }

        return slug;
    }

public List<Libro> filtrarLibros(String titulo, String editorial, String genero, String orden) {
    List<Libro> libros = libroRepository.findAll(); // puedes optimizar esto con consultas si deseas

    return libros.stream()
            .filter(libro -> titulo == null || titulo.isBlank() || 
                    libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .filter(libro -> editorial == null || editorial.isBlank() || 
                    libro.getEditorial().equalsIgnoreCase(editorial))
            .filter(libro -> genero == null || genero.isBlank() || 
                    libro.getGenero().equalsIgnoreCase(genero))
            .sorted((l1, l2) -> "desc".equalsIgnoreCase(orden)
                    ? l2.getTitulo().compareToIgnoreCase(l1.getTitulo())
                    : l1.getTitulo().compareToIgnoreCase(l2.getTitulo()))
            .toList();
}

public List<String> obtenerEditoriales() {
    return libroRepository.findDistinctEditoriales();
}

public List<String> obtenerGeneros() {
    return libroRepository.findDistinctGeneros();
}

}
