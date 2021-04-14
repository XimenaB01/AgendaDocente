package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

import java.util.List;

public class DescripcionDecorador extends AsignaturaListenerDecorador {

    private static String descripcion;
    private String comp = "Descripcion";
    private EnviarComponente enviarComponente = new EnviarComponente();

    public DescripcionDecorador(IAsignatura.AsignaturaListener asignaturaListener) {
        super(asignaturaListener);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getIdAsignatura();
        Asignatura a = asignaturaListenerDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            atualizarDescripcion(a,context);
        }else {
            agregarDescripcion(a,context);
        }
        return a;
    }

    public static void recibirDescripcion(String descripcion){
        DescripcionDecorador.descripcion = descripcion;
    }

    private void agregarDescripcion(Asignatura asignatura, Context context){
        enviarComponente.insertarComponente(comp,context,descripcion,asignatura.getIdAsignatura());
    }

    private void atualizarDescripcion(Asignatura a, Context context) {
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        List<Componente> componenteList = operacionesComponente.obtenerComponentes(a.getIdAsignatura());
        for (int i = 0; i < componenteList.size(); i++){
            if (componenteList.get(i).getComponente().equals(comp)){
                enviarComponente.actualizarComponente(comp,context,descripcion,a.getIdAsignatura());
            }else {
                agregarDescripcion(a,context);
            }
        }
    }
}
