package com.utpl.agendadocente.features.decorador;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.features.asignatura.IAsignatura;

import java.util.List;

public class TemasDecorador extends AsignaturaListenerDecorador {

    private static String temas;
    private String comp = "Temas";
    private EnviarComponente enviarComponente = new EnviarComponente();

    public TemasDecorador(IAsignatura.AsignaturaListener asignaturaListener) {
        super(asignaturaListener);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getIdAsignatura();
        asignaturaListenerDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarTemas(asignatura,context);
        }else {
            agregarTemas(asignatura, context);
        }
        return asignatura;
    }

    public static void recibirTemas(String temas){
        TemasDecorador.temas = temas;
    }

    private void agregarTemas(Asignatura a, Context context){
        enviarComponente.insertarComponente(comp,context,temas,a.getIdAsignatura());
    }

    private void actualizarTemas(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = (OperacionesComponente) OperacionesFactory.getOperacionComponente(context);
        List<Componente> componenteList = operacionesComponente.obtenerComponentes(a.getIdAsignatura());
        for (int i = 0; i < componenteList.size(); i++){
            if (componenteList.get(i).getComponente().equals(comp)){
                enviarComponente.actualizarComponente(comp,context,temas,a.getIdAsignatura());
            }else {
                agregarTemas(a,context);
            }
        }
    }



}
