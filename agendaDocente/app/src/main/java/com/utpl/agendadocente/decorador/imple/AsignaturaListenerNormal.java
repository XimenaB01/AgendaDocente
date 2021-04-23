package com.utpl.agendadocente.decorador.imple;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.ui.asignatura.IAsignatura;

public class AsignaturaListenerNormal implements IAsignatura.AsignaturaListener {

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {

        OperacionesAsignatura operacionesAsignatura = (OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(context);

        if (asignatura.getIdAsignatura() != null){

            operacionesAsignatura.modificarAsignatura(asignatura);

        }else {

            long operacion = operacionesAsignatura.insertarAsignatura(asignatura);

            if (operacion > 0){
                int id = (int) operacion;
                asignatura.setIdAsignatura(id);
            }

        }

        return asignatura;
    }
}
