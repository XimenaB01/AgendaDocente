package com.utpl.agendadocente.features.decorador.imple;

import android.content.Context;
import android.widget.Toast;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.features.asignatura.IAsignatura;
import com.utpl.agendadocente.features.asignatura.actualizar_asignatura.AsignaturaActualizarActivity;

public class AsignaturaListenerNormal implements IAsignatura.AsignaturaListener {
    private Context context;
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(context);


    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        this.context = context;
        if (asignatura.getIdAsignatura() != null ){
            actualizarAsignatura(asignatura, context);
        }else {
            guardarAsignatura(asignatura, context);
        }

        return asignatura;
    }

    private void actualizarAsignatura(Asignatura asignatura, Context context){
        if (operacionesAsignatura.asignaturaExistente(asignatura)){
            operacionesAsignatura.modificarAsignatura(asignatura);
            AsignaturaActualizarActivity.actualizarListaRV(true);
        }else {
            AsignaturaActualizarActivity.actualizarListaRV(false);
            Toast.makeText(context,"Ya existe esta Asignatura en "+asignatura.getCarrera(), Toast.LENGTH_LONG).show();
        }
    }

    private void guardarAsignatura(Asignatura asignatura, Context context) {
        if (operacionesAsignatura.asignaturaExistente(asignatura)){
            long insercion = operacionesAsignatura.insertarAsignatura(asignatura);
            if (insercion > 0){
                int inser = (int)insercion;
                asignatura.setIdAsignatura(inser);
            }
        }else {
            Toast.makeText(context,"Ya existe esta Asignatura en "+asignatura.getCarrera(),Toast.LENGTH_LONG).show();
        }
    }
}
