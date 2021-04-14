package com.utpl.agendadocente.model;

import java.io.Serializable;

public class Docente implements Serializable{
    private int idDocente;
    private String nombreDocente;
    private String apellidoDocente;
    private String email;
    private String cedula;

    public Docente(Integer idDocente, String nombreDocente, String apellidoDocente, String email, String cedula) {
        this.idDocente = idDocente;
        this.nombreDocente = nombreDocente;
        this.apellidoDocente = apellidoDocente;
        this.email = email;
        this.cedula = cedula;
    }

    public Docente() {}

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
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
