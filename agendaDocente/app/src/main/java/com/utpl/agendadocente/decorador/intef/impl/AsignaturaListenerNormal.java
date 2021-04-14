package com.utpl.agendadocente.decorador.intef.impl;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

public class AsignaturaListenerNormal implements IAsignatura.AsignaturaListener {

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(context);

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
