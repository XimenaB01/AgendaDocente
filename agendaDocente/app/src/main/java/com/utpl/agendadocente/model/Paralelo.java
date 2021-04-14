package com.utpl.agendadocente.model;

import java.io.Serializable;

public class Paralelo implements Serializable {
    private int idParalelo;
    private String nombreParalelo;
    private int numEstudiantes;
    private Integer horaioID;
    private Integer asignaturaID;
    private Integer periodoID;

    public Paralelo() {
    }

    public Paralelo(int idParalelo, String nombreParalelo, int numEstudiantes, Integer horaioID, Integer asignaturaID, Integer periodoID) {
        this.idParalelo = idParalelo;
        this.nombreParalelo = nombreParalelo;
        this.numEstudiantes = numEstudiantes;
        this.horaioID = horaioID;
        this.asignaturaID = asignaturaID;
        this.periodoID = periodoID;
    }

    public int getIdParalelo() {
        return idParalelo;
    }

    public void setIdParalelo(int idParalelo) {
        this.idParalelo = idParalelo;
    }

    public String getNombreParalelo() {
        return nombreParalelo;
    }

    public void setNombreParalelo(String nombreParalelo) {
        this.nombreParalelo = nombreParalelo;
    }

    public int getNumEstudiantes() {
        return numEstudiantes;
    }

    public void setNumEstudiantes(int numEstudiantes) {
        this.numEstudiantes = numEstudiantes;
    }

    public Integer getHoraioID() {
        return horaioID;
    }

    public void setHoraioID(Integer horaioID) {
        this.horaioID = horaioID;
    }

    public Integer getAsignaturaID() {
        return asignaturaID;
    }

    public void setAsignaturaID(Integer asignaturaID) {
        this.asignaturaID = asignaturaID;
    }

    public Integer getPeriodoID() {
        return periodoID;
    }

    public void setPeriodoID(Integer periodoID) {
        this.periodoID = periodoID;
    }
}

