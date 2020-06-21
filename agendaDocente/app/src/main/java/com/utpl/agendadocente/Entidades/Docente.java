package com.utpl.agendadocente.Entidades;

import java.io.Serializable;

public class Docente implements Serializable{
    private int id_docente;
    private String nombreDocente;
    private String apellidoDocente;
    private String email;
    private String cedula;

    public Docente(Integer id_docente, String nombreDocente, String apellidoDocente, String email, String cedula) {
        this.id_docente = id_docente;
        this.nombreDocente = nombreDocente;
        this.apellidoDocente = apellidoDocente;
        this.email = email;
        this.cedula = cedula;
    }

    public Docente() {}

    public int getId_docente() {
        return id_docente;
    }

    public void setId_docente(int id_docente) {
        this.id_docente = id_docente;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public String getApellidoDocente() {
        return apellidoDocente;
    }

    public void setApellidoDocente(String apellidoDocente) {
        this.apellidoDocente = apellidoDocente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
