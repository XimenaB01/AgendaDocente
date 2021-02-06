package com.utpl.agendadocente.Model;

public class DocenteAsignado extends Docente {

    private Boolean estado;

    public DocenteAsignado(Integer id_docente, String nombreDocente, String apellidoDocente, String email, String cedula, Boolean estado) {
        super(id_docente, nombreDocente, apellidoDocente, email, cedula);
        this.estado = estado;
    }

    public Boolean getEstado() {
        return estado;
    }
}
