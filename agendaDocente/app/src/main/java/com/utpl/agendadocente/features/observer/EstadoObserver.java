package com.utpl.agendadocente.features.observer;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.features.tarea.ITarea;

public class EstadoObserver implements Observer{

    private String estado;
    private ITarea.ActualizarTareaListener listener;
    private Integer id;
    private static int posicion;
    private static Context context;

    public EstadoObserver(Subject simpleSubject){
        simpleSubject.registerObserver(this);
    }

    @Override
    public void update(Integer id,String estado, ITarea.ActualizarTareaListener lis) {
        this.estado = estado;
        this.listener = lis;
        this.id = id;
        notificacion();
    }

    private void notificacion(){
        OperacionesTarea operacionesTarea = (OperacionesTarea) OperacionesFactory.getOperacionTarea(context);
        Tarea tarea = operacionesTarea.obtenerTarea(id);
        tarea.setEstadoTarea(estado);
        long op = operacionesTarea.modificarTarea(tarea);
        if (op > 0){
            listener.onActualizarTarea(tarea,posicion);
        }
    }

    public static void position(int pos, Context cont){
        context = cont;
        posicion = pos;
    }
}
