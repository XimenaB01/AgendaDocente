package com.utpl.agendadocente.model;

public class Evaluacion {
    private Integer idEvaluacion;
    private String nombreEvaluacion;
    private String tipo;
    private String bimestre;
    private String fechaEvaluacion;
    private String observacion;
    private Integer paraleloID;
    private Integer cuestionarioID;

    public Evaluacion(Integer idEvaluacion, String nombreEvaluacion, String tipo, String bimestre, String fechaEvaluacion, String observacion, Integer paraleloID, Integer cuestionarioID) {
        this.idEvaluacion = idEvaluacion;
        this.nombreEvaluacion = nombreEvaluacion;
        this.tipo = tipo;
        this.bimestre = bimestre;
        this.fechaEvaluacion = fechaEvaluacion;
        this.observacion = observacion;
        this.paraleloID = paraleloID;
        this.cuestionarioID = cuestionarioID;
    }

    public Evaluacion(String bimestre) {
        this.bimestre = bimestre;
    }

    public Evaluacion() {
    }

    public Integer getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Integer idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
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
