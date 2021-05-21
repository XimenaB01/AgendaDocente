package com.utpl.agendadocente.features.periodo;

import com.utpl.agendadocente.model.PeriodoAcademico;

public class IPeriodo {

    public interface PeriodoCreateListener {
        void onCrearPeriodo(PeriodoAcademico periodoAcademico);
    }

    public interface ActualizarPeriodoListener {
        void onActualizarPeriodo(PeriodoAcademico periodoAcademico, int position);
    }
}
