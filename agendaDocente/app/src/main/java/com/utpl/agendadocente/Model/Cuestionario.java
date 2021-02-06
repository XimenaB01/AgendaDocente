package com.utpl.agendadocente.Model;

public class Cuestionario {
    private Integer id_cuestionario;
    private String nombreCuestionario;
    private String preguntas;

    public Cuestionario() {
    }

    public Cuestionario(Integer id_cuestionario, String nombreCuestionario, String preguntas) {
        this.id_cuestionario = id_cuestionario;
        this.nombreCuestionario = nombreCuestionario;
        this.preguntas = preguntas;
    }

    public Integer getId_cuestionario() {
        return id_cuestionario;
    }

    public void setId_cuestionario(Integer id_cuestionario) {
        this.id_cuestionario = id_cuestionario;
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
