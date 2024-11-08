package com.utpl.agendadocente.features.decorador;

import android.content.Context;

import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.features.asignatura.IAsignatura;

public abstract class AsignaturaListenerDecorador implements IAsignatura.AsignaturaListener {

    protected IAsignatura.AsignaturaListener asignaturaListenerDecoradora;

    public AsignaturaListenerDecorador(IAsignatura.AsignaturaListener asignaturaListener){
        this.asignaturaListenerDecoradora = asignaturaListener;
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        return this.asignaturaListenerDecoradora.agregarAsignatura(asignatura,context);
    }
}
