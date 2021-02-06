package com.utpl.agendadocente.intef.impl;

import android.content.Context;
import android.util.Log;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.intef.IAsignatura;

public class AsignaturaNormal implements IAsignatura.asignatura {

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(context);

        if (asignatura.getId_asignatura() != null){

            operacionesAsignatura.ModificarAsig(asignatura);

        }else {

            long operacion = operacionesAsignatura.InsertarAsignatura(asignatura);

            if (operacion > 0){
                int id = (int) operacion;
                asignatura.setId_asignatura(id);
            }

        }

        return asignatura;
    }
}
