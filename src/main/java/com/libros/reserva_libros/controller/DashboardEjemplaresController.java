package com.libros.reserva_libros.controller;
import java.beans.PropertyEditorSupport;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.service.EjemplarService;
import com.libros.reserva_libros.service.LibroService;


@Controller
@RequestMapping("/dashboard/ejemplares")
public class DashboardEjemplaresController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String mostrarEjemplares(Model model) {
        model.addAttribute("ejemplares", ejemplarService.listarEjemplares());
        model.addAttribute("libros", libroService.listarLibros());
        model.addAttribute("ejemplar", new Ejemplar());
        model.addAttribute("estados",ejemplarService.getEstados());
        return "ejemplares";
    }

    @PostMapping("/guardar")
    public String guardarEjemplar(@ModelAttribute Ejemplar ejemplar) {
        ejemplarService.guardarEjemplar(ejemplar);
        return "redirect:/dashboard/ejemplares";
    }

    @PostMapping("/editar")
    public String actualizarEjemplar(@ModelAttribute Ejemplar ejemplar) {
        ejemplarService.actualizarEjemplar(ejemplar);
        return "redirect:/dashboard/ejemplares";
    }
        

    @GetMapping("/eliminar/{id}")
    public String eliminarEjemplar(@PathVariable Long id) {
        ejemplarService.eliminarEjemplar(id);
        return "redirect:/dashboard/ejemplares";
    }
    

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Libro.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String id) throws IllegalArgumentException {
                try {
                    Long libroId = Long.valueOf(id);
                    Optional<Libro> libro = libroService.buscarLibroPorId(libroId);
                    setValue(libro);
                } catch (NumberFormatException e) {
                    setValue(null);
                }
            }
        });
    }

}