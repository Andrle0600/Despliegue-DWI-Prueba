package com.libros.reserva_libros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.service.LibroService;

@Controller
@RequestMapping("/dashboard/libros")
public class DashboardLibrosController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String verLibros(Model model) {
        model.addAttribute("libros", libroService.listarLibros());
        model.addAttribute("libro", new Libro());
        return "libros";
    }

    @PostMapping("/guardar")
    public String guardarTecnico(@ModelAttribute Libro libro) {
        libroService.guardarLibro(libro);
        return "redirect:/dashboard/libros";
    }

    @PostMapping("/editar")
    public String editarLibro(@ModelAttribute Libro libro) {
        libroService.actualizar(libro);
        return "redirect:/dashboard/libros";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return "redirect:/dashboard/libros";
    }

    @GetMapping("/buscar")
    public String buscarPorNombre(@RequestParam String nombreLibro, Model model) {
        List<Libro> libros = libroService.buscarPorTitulo(nombreLibro);
        model.addAttribute("libros", libros);
        model.addAttribute("libro", new Libro());
        return "libros";
    }

}