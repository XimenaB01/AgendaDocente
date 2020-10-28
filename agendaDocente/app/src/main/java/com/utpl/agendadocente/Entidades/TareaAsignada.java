package com.utpl.agendadocente.Entidades;

public class TareaAsignada extends Tarea{

    private boolean estado;
    private Integer id_asig;
    private String nomPar;

    public TareaAsignada(Integer id_tarea, String nombreTarea, String descripcionTarea, String fechaTarea, String observacionTarea, String estadoTarea,
                         boolean estado, Integer id_asig, String nomPar) {
        super(id_tarea, nombreTarea, descripcionTarea, fechaTarea, observacionTarea, estadoTarea);
        this.estado = estado;
        this.id_asig = id_asig;
        this.nomPar = nomPar;
    }

    public boolean isEstado() {
        return estado;
    }

    public Integer getId_asig() {
        return id_asig;
    }

    public void setId_asig(Integer id_asig) {
        this.id_asig = id_asig;
    }

    public String getNomPar() {
        return nomPar;
    }

    public void setNomPar(String nomPar) {
        this.nomPar = nomPar;
    }
}
