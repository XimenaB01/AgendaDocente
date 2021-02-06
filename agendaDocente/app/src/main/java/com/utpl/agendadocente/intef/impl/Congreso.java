package com.utpl.agendadocente.intef.impl;

import android.content.Context;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.intef.IAsignatura;

public class Congreso implements IAsignatura.asignatura {

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(context);
        operacionesAsignatura.InsertarAsignatura(asignatura);
        return asignatura;
    }

}
