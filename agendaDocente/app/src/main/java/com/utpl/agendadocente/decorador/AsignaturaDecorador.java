package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.intef.IAsignatura;

public abstract class AsignaturaDecorador implements IAsignatura.asignatura {

    protected IAsignatura.asignatura asignaturaDecoradora;

    public AsignaturaDecorador(IAsignatura.asignatura Iasignatura){
        this.asignaturaDecoradora = Iasignatura;
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        return this.asignaturaDecoradora.agregarAsignatura(asignatura,context);
    }
}
