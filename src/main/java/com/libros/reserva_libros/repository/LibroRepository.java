package com.libros.reserva_libros.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.libros.reserva_libros.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findAllByOrderByTituloAsc();

    List<Libro> findAllByOrderByTituloDesc();

    @Query("SELECT r FROM Libro r WHERE LOWER(r.titulo) LIKE LOWER(CONCAT('%',:nombre,'%'))")
    List<Libro> buscarPorTitulo(@Param("nombre") String nombre);

    Optional<Libro> findBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Query("SELECT DISTINCT l.editorial FROM Libro l WHERE l.editorial IS NOT NULL")
    List<String> findDistinctEditoriales();

    @Query("SELECT DISTINCT l.genero FROM Libro l WHERE l.genero IS NOT NULL")
    List<String> findDistinctGeneros();
}
