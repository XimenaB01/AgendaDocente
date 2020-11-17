package com.utpl.agendadocente.Entidades;

import java.io.Serializable;

public class Evaluacion implements Serializable {
    private Integer id_evaluacion;
    private String nombreEvaluacion;
    private String tipo;
    private String bimestre;
    private String fechaEvaluacion;
    private String observacion;
    private Integer paraleloID;
    private Integer cuestionarioID;

    public Evaluacion(Integer id_evaluacion, String nombreEvaluacion, String tipo, String bimestre, String fechaEvaluacion, String observacion, Integer paraleloID, Integer cuestionarioID) {
        this.id_evaluacion = id_evaluacion;
        this.nombreEvaluacion = nombreEvaluacion;
        this.tipo = tipo;
        this.bimestre = bimestre;
        this.fechaEvaluacion = fechaEvaluacion;
        this.observacion = observacion;
        this.paraleloID = paraleloID;
        this.cuestionarioID = cuestionarioID;
    }

    public Evaluacion() {
    }

    public Integer getId_evaluacion() {
        return id_evaluacion;
    }

    public void setId_evaluacion(Integer id_evaluacion) {
        this.id_evaluacion = id_evaluacion;
    }

    public String getNombreEvaluacion() {
        return nombreEvaluacion;
    }

    public void setNombreEvaluacion(String nombreEvaluacion) {
        this.nombreEvaluacion = nombreEvaluacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBimestre() {
        return bimestre;
    }

    public void setBimestre(String bimestre) {
        this.bimestre = bimestre;
    }

    public String getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(String fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getCuestionarioID() {
        return cuestionarioID;
    }

    public void setCuestionarioID(Integer cuestionarioID) {
        this.cuestionarioID = cuestionarioID;
    }

    public Integer getParaleloID() {
        return paraleloID;
    }

    public void setParaleloID(Integer paraleloID) {
        this.paraleloID = paraleloID;
    }
}
