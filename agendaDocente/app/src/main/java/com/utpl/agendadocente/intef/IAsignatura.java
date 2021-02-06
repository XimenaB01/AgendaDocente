package com.utpl.agendadocente.intef;

import android.content.Context;

import com.utpl.agendadocente.Model.Asignatura;

public class IAsignatura {

    public interface ActualizarAsignaturaListener {
        void onActualizarAsignatura(Asignatura asignatura, int position);
    }

    public interface AsignaturaCreateListener {
        void onCrearAsignatura(Asignatura asignatura);
    }

    public interface asignatura {
        Asignatura agregarAsignatura(Asignatura asignatura, Context context);
    }

}
