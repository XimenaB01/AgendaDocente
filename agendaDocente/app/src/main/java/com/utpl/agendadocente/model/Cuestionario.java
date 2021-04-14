package com.utpl.agendadocente.model;

public class Cuestionario {
    private Integer idCuestionario;
    private String nombreCuestionario;
    private String preguntas;

    public Cuestionario() {
    }

    public Cuestionario(Integer idCuestionario, String nombreCuestionario, String preguntas) {
        this.idCuestionario = idCuestionario;
        this.nombreCuestionario = nombreCuestionario;
        this.preguntas = preguntas;
    }

    public Integer getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Integer idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public String getNombreCuestionario() {
        return nombreCuestionario;
    }

    public void setNombreCuestionario(String nombreCuestionario) {
        this.nombreCuestionario = nombreCuestionario;
    }

    public String getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }
}
