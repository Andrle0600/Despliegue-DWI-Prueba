package com.libros.reserva_libros.dto;


import lombok.Data;

@Data
public class RegisterRequest {
    private String dni;
    private String nombres;
    private String apellidos;
    private String fechaNacimiento;
    private String genero;
    private String username;
    private String telefono;
    private String direccion;
    private String password;
}
