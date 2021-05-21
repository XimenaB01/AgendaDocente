package com.utpl.agendadocente.features.tarea;

import com.utpl.agendadocente.model.Tarea;

public class ITarea {

    public interface TareaCrearListener {
        void onCrearTarea(Tarea tarea);
    }

    public interface ActualizarTareaListener {
        void onActualizarTarea(Tarea tarea, int position);
    }
}
