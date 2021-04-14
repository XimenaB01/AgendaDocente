package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

import java.util.List;

public class DuracionDecorador extends AsignaturaListenerDecorador {

    private static String duracion;
    private String comp = "Duracion";
    private EnviarComponente enviarComponente = new EnviarComponente();

    public DuracionDecorador(IAsignatura.AsignaturaListener iasignatura) {
        super(iasignatura);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getIdAsignatura();
        asignaturaListenerDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarDuracion(asignatura,context);
        }else {
            agregarDuracion(asignatura,context);
        }
        return asignatura;
    }

    public static void recibirDuracion(String duracion){
        DuracionDecorador.duracion = duracion;
    }

    private void agregarDuracion(Asignatura a, Context context){
        enviarComponente.insertarComponente(comp,context,duracion,a.getIdAsignatura());
    }

    private void actualizarDuracion(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        List<Componente> componenteList = operacionesComponente.obtenerComponentes(a.getIdAsignatura());
        for (int i = 0; i < componenteList.size(); i++){
            if (componenteList.get(i).getComponente().equals(comp)){
                enviarComponente.actualizarComponente(comp,context,duracion,a.getIdAsignatura());
            }else {
                agregarDuracion(a,context);
            }
        }
    }
}
