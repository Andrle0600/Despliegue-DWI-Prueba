package com.libros.reserva_libros.dto;

import java.time.LocalDate;

public class ReservaRequestDTO {
    private Long usuarioId;
    private Long ejemplarId;
    private LocalDate fechaRecojo;

    // Getters y setters
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEjemplarId() {
        return ejemplarId;
    }

    public void setEjemplarId(Long ejemplarId) {
        this.ejemplarId = ejemplarId;
    }

    public LocalDate getFechaRecojo() {
        return fechaRecojo;
    }

    public void setFechaRecojo(LocalDate fechaRecojo) {
        this.fechaRecojo = fechaRecojo;
    }
}