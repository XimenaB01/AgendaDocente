package com.utpl.agendadocente.Entidades;

public class TareaAsignada extends Tarea{

    private boolean estado;

    public TareaAsignada(Integer id_tarea, String nombreTarea, String descripcionTarea, String fechaTarea, String observacionTarea, boolean estado) {
        super(id_tarea, nombreTarea, descripcionTarea, fechaTarea, observacionTarea);
        this.estado = estado;
    }

    public boolean isEstado() {
        return estado;
    }
}
