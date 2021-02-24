package com.utpl.agendadocente.ui.cuestionario;

import com.utpl.agendadocente.Model.Cuestionario;

public class ICuestionario {

    public interface ActualizarCuestionarioListener{
        void onActualizarCuestionario(Cuestionario cuestionario, int position);
    }

    public interface CuestionarioCrearListener {
        void onCrearCuestionario(Cuestionario cuestionario);
    }
}
