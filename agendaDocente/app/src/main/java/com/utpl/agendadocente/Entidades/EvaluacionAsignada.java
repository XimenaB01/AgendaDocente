package com.utpl.agendadocente.Entidades;

public class EvaluacionAsignada extends Evaluacion{
    private boolean estado;

    public EvaluacionAsignada(Integer id_evaluacion, String nombreEvaluacion, String tipo, String bimestre, String fechaEvaluacion, String observacion, Integer cuestionarioID, boolean estado) {
        super(id_evaluacion, nombreEvaluacion, tipo, bimestre, fechaEvaluacion, observacion, cuestionarioID);
        this.estado = estado;
    }

    public boolean isEstado() {
        return estado;
    }
}
