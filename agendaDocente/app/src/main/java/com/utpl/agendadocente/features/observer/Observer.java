package com.utpl.agendadocente.features.observer;

import com.utpl.agendadocente.features.tarea.ITarea;

public interface Observer {
    void update(Integer id, String estado, ITarea.ActualizarTareaListener listener);
}
