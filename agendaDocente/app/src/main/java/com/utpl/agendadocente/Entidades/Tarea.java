package com.utpl.agendadocente.Entidades;

import java.io.Serializable;

public class Tarea implements Serializable {
    private Integer id_tarea;
    private String nombreTarea;
    private String descripcionTarea;
    private String fechaTarea;
    private String observacionTarea;

    public Tarea(Integer id_tarea, String nombreTarea, String descripcionTarea, String fechaTarea, String observacionTarea) {
        this.id_tarea = id_tarea;
        this.nombreTarea = nombreTarea;
        this.descripcionTarea = descripcionTarea;
        this.fechaTarea = fechaTarea;
        this.observacionTarea = observacionTarea;
    }

    public Tarea() {
    }

    public Integer getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(Integer id_tarea) {
        this.id_tarea = id_tarea;
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
}
