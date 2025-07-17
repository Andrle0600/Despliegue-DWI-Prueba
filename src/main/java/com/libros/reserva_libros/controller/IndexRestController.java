package com.libros.reserva_libros.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.libros.reserva_libros.model.Ejemplar;
import com.libros.reserva_libros.model.Libro;
import com.libros.reserva_libros.model.Reserva;
import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.service.EjemplarService;
import com.libros.reserva_libros.service.LibroService;
import com.libros.reserva_libros.service.ReservaService;
import com.libros.reserva_libros.service.UserService;

@RestController
@RequestMapping("/api")
public class IndexRestController {

    @Autowired
    private EjemplarService ejemplarService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/")
    public Map<String, String> home() {
        return Collections.singletonMap("mensaje", "Bienvenido a la API de Reservas");
    }

    @GetMapping("/misreservas")
    public Map<String, Object> mostrarReservas(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal != null) {
            String correo = principal.getName();
            Optional<User> optionalUser = userService.encontrarPorUsername(correo);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                response.put("usuario", user);
                response.put("reservas", user.getReservas());
            } else {
                response.put("error", "Usuario no encontrado");
            }
        }
        return response;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> verDashboard() {
        List<Ejemplar> ejemplares = ejemplarService.listarEjemplares();
        Map<String, Object> data = new HashMap<>();
        data.put("ejemplaresDisponibles", ejemplarService.listarEjemplaresDisponibles(ejemplares));
        data.put("usuarios", userService.findAll());
        data.put("reservasPendientes", reservaService.listarReservasPendientes());
        data.put("reservasRetrasadas", reservaService.listarReservasRetrasadas());
        data.put("reservasRecientes", reservaService.listarReservasRecientes());
        return data;
    }

    @GetMapping("/eventos")
    public Map<String, String> mostrarEventos() {
        return Collections.singletonMap("mensaje", "Listado de eventos (a implementar)");
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String editorial,
            @RequestParam(required = false) String genero,
            @RequestParam(defaultValue = "asc") String orden,
            Model model) {

        List<Libro> libros = libroService.filtrarLibros(titulo, editorial, genero, orden);
        List<String> editoriales = libroService.obtenerEditoriales();
        List<String> generos = libroService.obtenerGeneros();

        model.addAttribute("libros", libros);
        model.addAttribute("orden", orden);
        model.addAttribute("titulo", titulo);
        model.addAttribute("editorial", editorial);
        model.addAttribute("genero", genero);
        model.addAttribute("editoriales", editoriales);
        model.addAttribute("generos", generos);
        return "catalogo";
    }

    @GetMapping("/tucuenta")
    public Map<String, Object> mostrarCuenta(Principal principal) {
        Map<String, Object> data = new HashMap<>();
        if (principal != null) {
            String correo = principal.getName();
            Optional<User> optionalUser = userService.encontrarPorUsername(correo);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                data.put("usuario", user);
                data.put("reservas", user.getReservas());
            } else {
                data.put("error", "Usuario no encontrado");
            }
        }
        return data;
    }

    @GetMapping("/detalle/{slug}")
    public ResponseEntity<?> showDetallePorSlug(@PathVariable String slug) {
        Optional<Libro> libro = libroService.buscarPorSlug(slug);
        if (libro.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Libro no encontrado"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("libro", libro.get());
        response.put("ejemplaresDisponibles", ejemplarService.listarEjemplaresDisponibles(libro.get()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prestamo/{slug}")
    public ResponseEntity<?> showPrestamo(@PathVariable String slug, Principal principal) {
        Optional<Libro> libroOpt = libroService.buscarPorSlug(slug);
        if (libroOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Libro no encontrado"));
        }

        Libro libro = libroOpt.get();
        if (!ejemplarService.hayEjemplarDisponiblePorLibro(libro)) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "No hay ejemplares disponibles"));
        }

        Ejemplar ejemplarAsignado = ejemplarService.listarEjemplaresDisponibles(libro).get(0);

        Map<String, Object> response = new HashMap<>();
        response.put("libro", libro);
        response.put("ejemplarAsignado", ejemplarAsignado);
        response.put("reserva", new Reserva());

        if (principal != null) {
            Optional<User> optionalUser = userService.encontrarPorUsername(principal.getName());
            optionalUser.ifPresent(user -> response.put("usuario", user));
        }

        return ResponseEntity.ok(response);
    }
}