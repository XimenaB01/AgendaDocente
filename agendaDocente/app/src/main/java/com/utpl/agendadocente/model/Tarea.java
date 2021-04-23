package com.utpl.agendadocente.model;

public class Tarea {
    private Integer idTarea;
    private String nombreTarea;
    private String descripcionTarea;
    private String fechaTarea;
    private String observacionTarea;
    private String estadoTarea;
    private Integer paraleloId;

    public Tarea(Integer idTarea, String nombreTarea, String descripcionTarea, String fechaTarea, String observacionTarea, String estadoTarea, Integer paraleloId) {
        this.idTarea = idTarea;
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.fechaTarea = fechaTarea;
        this.observacionTarea = observacionTarea;
        this.estadoTarea = estadoTarea;
        this.paraleloId = paraleloId;
    }

    public Tarea() {
    }

    public Tarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public Integer getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Integer idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getDescripcionTarea() {
        return descripcionTarea;
    }

    public void setDescripcionTarea(String descripcionTarea) {
        this.descripcionTarea = descripcionTarea;
    }

    public String getFechaTarea() {
        return fechaTarea;
    }

    public void setFechaTarea(String fechaTarea) {
        this.fechaTarea = fechaTarea;
    }

    public String getObservacionTarea() {
        return observacionTarea;
    }

    public void setObservacionTarea(String observacionTarea) {
        this.observacionTarea = observacionTarea;
    }

    public String getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public Integer getParaleloId() {
        return paraleloId;
    }

    public void setParaleloId(Integer paraleloId) {
        this.paraleloId = paraleloId;
    }

}
