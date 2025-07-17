/* 
package com.libros.reserva_libros.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.service.EjemplarService;
import com.libros.reserva_libros.service.LibroService;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private EjemplarService ejemplarService;

    //Endpoints libros
    @GetMapping("/libros")
    @ResponseBody
    public List<Libro> obtenerLibrosApi() {
        return libroService.listarLibros();
    }

    @GetMapping("/libros/{id}")
    @ResponseBody
    public Optional<Libro> obtenerLibroApi(@PathVariable Long id) {
        return libroService.buscarLibroPorId(id);
    }

    @PostMapping("/libros")
    @ResponseBody
    public Libro crearLibroApi(@RequestBody Libro libro) {
        return libroService.guardarLibro(libro);
    }

    @DeleteMapping("/libros/{id}")
    @ResponseBody
    public void eliminarReservaApi(@PathVariable Long id) {
        libroService.eliminarLibro(id);
    }

    // Endpoints ejemplares
    @GetMapping("/ejemplares")
    @ResponseBody
    public List<Ejemplar> obtenerEjemplaresApi() {
        return ejemplarService.listarEjemplares();
    }

    @GetMapping("/ejemplares/{id}")
    @ResponseBody
    public Optional<Ejemplar> obtenerEjemplarApi(@PathVariable Long id) {
        return ejemplarService.buscarEjemplarporId(id);
    }

    @PostMapping("/ejemplares")
    @ResponseBody
    public Ejemplar crearEjemplarApi(@RequestBody Ejemplar ejemplar) {
        return ejemplarService.guardarEjemplar(ejemplar);
    }

    @DeleteMapping("/ejemplares/{id}")
    @ResponseBody
    public void eliminarEjemplarApi(@PathVariable Long id) {
        ejemplarService.eliminarEjemplar(id);
    }
}
*/