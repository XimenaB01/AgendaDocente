package com.utpl.agendadocente.features.cuestionario;

import com.utpl.agendadocente.model.Cuestionario;

public class ICuestionario {

    public interface ActualizarCuestionarioListener{
        void onActualizarCuestionario(Cuestionario cuestionario, int position);
    }

    public interface CuestionarioCrearListener {
        void onCrearCuestionario(Cuestionario cuestionario);
    }
}
