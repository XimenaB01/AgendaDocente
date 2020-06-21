package com.utpl.agendadocente.Entidades;

public class PeriodoAcademico {
    private Integer id_periodo;
    private String fechaInicio;
    private String fechaFin;

    public PeriodoAcademico() {
    }

    public PeriodoAcademico(Integer id_periodo, String fechaInicio, String fechaFin) {
        this.id_periodo = id_periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getId_periodo() {
        return id_periodo;
    }

    public void setId_periodo(Integer id_periodo) {
        this.id_periodo = id_periodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
