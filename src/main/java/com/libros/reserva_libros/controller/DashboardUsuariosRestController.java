/* 
package com.libros.reserva_libros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.libros.reserva_libros.model.User;
import com.libros.reserva_libros.service.UserService;

@RestController
@RequestMapping("/api/dashboard/usuarios")
public class DashboardUsuariosRestController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> listarUsuarios() {
        return userService.findAll();
    }
}
    */