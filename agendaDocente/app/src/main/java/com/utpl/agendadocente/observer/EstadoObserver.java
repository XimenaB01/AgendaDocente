package com.utpl.agendadocente.observer;

import android.content.Context;

import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.ui.tarea.ITarea;

public class EstadoObserver implements Observer{

    private String Estado;
    private ITarea.ActualizarTareaListener listener;
    private Integer Id;
    private static int posicion;
    private static Context context;

    public EstadoObserver(Subject simpleSubject){
        simpleSubject.registerObserver(this);
    }

    @Override
    public void update(Integer id,String estado, ITarea.ActualizarTareaListener lis) {
        this.Estado = estado;
        this.listener = lis;
        this.Id = id;
        notificacion();
    }

    private void notificacion(){
        OperacionesTarea OT = new OperacionesTarea(context);
        Tarea tarea = OT.obtenerTar(Id);
        tarea.setEstadoTarea(Estado);
        tarea.setParaleloId(null);
        long op = OT.ModificarTar(tarea);
        if (op > 0){
            listener.onActualizarTarea(tarea,posicion);
        }
    }

    public static void position(int pos, Context cont){
        context = cont;
        posicion = pos;
    }
}
