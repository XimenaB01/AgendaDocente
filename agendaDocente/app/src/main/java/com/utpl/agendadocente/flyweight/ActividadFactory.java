package com.utpl.agendadocente.flyweight;

import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.HashMap;

public class ActividadFactory {

    private static final HashMap<String, Tarea> tareaMap = new HashMap<>();

    private ActividadFactory(){
        //Requires constructor
    }

    public static ITarea.Actividad getTarea(String estado) {
        Tarea tarea = tareaMap.get(estado);
        if(tarea == null) {
            tarea = new Tarea(estado);
            tareaMap.put(estado, tarea);
        }
        return tarea;
    }
}
