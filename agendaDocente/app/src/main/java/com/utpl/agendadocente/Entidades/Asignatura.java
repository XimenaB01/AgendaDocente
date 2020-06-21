package com.utpl.agendadocente.Entidades;

import java.io.Serializable;

public class Asignatura implements Serializable {

    private Integer id_asignatura;
    private String nombreAsignatura;
    private String area;
    private String creditos;
    private String descripcionAsigantura;
    private String nivel;
    private String carrera;
    private String horario;

    public Asignatura(Integer id_asignatura, String nombreAsignatura, String area, String creditos, String descripcionAsigantura, String nivel, String carrera, String horario) {
        this.id_asignatura = id_asignatura;
        this.nombreAsignatura = nombreAsignatura;
        this.area = area;
        this.creditos = creditos;
        this.descripcionAsigantura = descripcionAsigantura;
        this.nivel = nivel;
        this.carrera = carrera;
        this.horario = horario;
    }

    public Asignatura() {
    }

    public Integer getId_asignatura() {

        return id_asignatura;
    }

    public void setId_asignatura(Integer id_asignatura) {

        this.id_asignatura = id_asignatura;
    }

    public String getNombreAsignatura() {

        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getArea() {

        return area;
    }

    public void setArea(String area) {

        this.area = area;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {

        this.creditos = creditos;
    }

    public String getDescripcionAsigantura() {

        return descripcionAsigantura;
    }

    public void setDescripcionAsigantura(String descripcionAsigantura) {

        this.descripcionAsigantura = descripcionAsigantura;
    }

    public String getNivel() {

        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCarrera() {

        return carrera;
    }

    public void setCarrera(String carrera) {

        this.carrera = carrera;
    }
    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
