package com.utpl.agendadocente.Entidades;

import java.io.Serializable;

public class Paralelo implements Serializable {
    private Integer id_paralelo;
    private String nombreParalelo;
    private int num_estudiantes;
    private Integer horaioID;
    private Integer asignaturaID;
    private Integer periodoID;

    public Paralelo() {
    }

    public Paralelo(Integer id_paralelo, String nombreParalelo, int num_estudiantes, Integer horaioID, Integer asignaturaID, Integer periodoID) {
        this.id_paralelo = id_paralelo;
        this.nombreParalelo = nombreParalelo;
        this.num_estudiantes = num_estudiantes;
        this.horaioID = horaioID;
        this.asignaturaID = asignaturaID;
        this.periodoID = periodoID;
    }

    public Integer getId_paralelo() {
        return id_paralelo;
    }

    public void setId_paralelo(Integer id_paralelo) {
        this.id_paralelo = id_paralelo;
    }

    public String getNombreParalelo() {
        return nombreParalelo;
    }

    public void setNombreParalelo(String nombreParalelo) {
        this.nombreParalelo = nombreParalelo;
    }

    public int getNum_estudiantes() {
        return num_estudiantes;
    }

    public void setNum_estudiantes(int num_estudiantes) {
        this.num_estudiantes = num_estudiantes;
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

