package com.libros.reserva_libros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.libros.reserva_libros.service.ReservaService;

@Controller
@RequestMapping("/dashboard/reservas")
public class DashboardReservasController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public String mostrarReservas(Model model) {
        model.addAttribute("reservas",reservaService.listarReservas());
        return "reservas";
    }

}