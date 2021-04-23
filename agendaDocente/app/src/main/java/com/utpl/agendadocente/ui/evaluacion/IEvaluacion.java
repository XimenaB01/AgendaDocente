package com.utpl.agendadocente.ui.evaluacion;

import com.utpl.agendadocente.model.Evaluacion;

public class IEvaluacion {

    public interface EvaluacionCrearListener {
        void onCrearEvaluacion(Evaluacion evaluacion);
    }

    public interface ActualizarEvaluacionListener {
        void onActualizarEvaluacion(Evaluacion evaluacion, int position);
    }

}
