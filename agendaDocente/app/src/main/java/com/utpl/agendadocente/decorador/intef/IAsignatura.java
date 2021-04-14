package com.utpl.agendadocente.decorador.intef;

import android.content.Context;

import com.utpl.agendadocente.model.Asignatura;

public class IAsignatura {

    public interface ActualizarAsignaturaListener {
        void onActualizarAsignatura(Asignatura asignatura, int position);
    }

    public interface AsignaturaCreateListener {
        void onCrearAsignatura(Asignatura asignatura);
    }

    public interface AsignaturaListener {
        Asignatura agregarAsignatura(Asignatura asignatura, Context context);
    }

}
