package com.utpl.agendadocente.ui.docente;

import com.utpl.agendadocente.model.Docente;

public class IDocente {

    public interface ActualizarDocenteListener {
        void onActualizarDocente(Docente docente, int position);
    }

    public interface DocenteCreateListener {
        void onCrearDocente(Docente docente);
    }
}
