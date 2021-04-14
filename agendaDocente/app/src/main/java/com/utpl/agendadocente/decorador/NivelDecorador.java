package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

import java.util.List;

public class NivelDecorador extends AsignaturaListenerDecorador {

    private static String nivel;
    private String comp = "Nivel";
    private EnviarComponente enviarComponente = new EnviarComponente();

    public NivelDecorador(IAsignatura.AsignaturaListener asignaturaListener) {
        super(asignaturaListener);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getIdAsignatura();
        asignaturaListenerDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarNivel(asignatura,context);
        }else {
            agregarNivel(asignatura, context);
        }
        return asignatura;
    }

    public static void recibirNivel(String nivel){
        NivelDecorador.nivel = nivel;
    }

    private void agregarNivel(Asignatura a, Context context) {
        enviarComponente.insertarComponente(comp,context,nivel,a.getIdAsignatura());
    }

    private void actualizarNivel(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        List<Componente> componenteList = operacionesComponente.obtenerComponentes(a.getIdAsignatura());
        for (int i = 0; i < componenteList.size(); i++){
            if (componenteList.get(i).getComponente().equals(comp)){
                enviarComponente.actualizarComponente(comp,context,nivel,a.getIdAsignatura());
            }else {
                agregarNivel(a,context);
            }
        }
    }

}
