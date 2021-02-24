package com.utpl.agendadocente.observer;

import com.utpl.agendadocente.ui.tarea.ITarea;

public interface Observer {
    void update(Integer id, String estado, ITarea.ActualizarTareaListener listener);
}
