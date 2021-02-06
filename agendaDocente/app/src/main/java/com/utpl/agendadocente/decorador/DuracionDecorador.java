package com.utpl.agendadocente.decorador;

import android.content.Context;
import android.util.Log;

import com.utpl.agendadocente.DataBase.OperacionesComponente;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.intef.IAsignatura;

public class DuracionDecorador extends AsignaturaDecorador {

    private static String Duracion;
    private String Comp = "Duracion";
    private Componente componente = new Componente();

    public DuracionDecorador(IAsignatura.asignatura Iasignatura) {
        super(Iasignatura);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getId_asignatura();
        asignaturaDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarDuracion(asignatura,context);
        }else {
            agregarDuracion(asignatura,context);
        }
        return asignatura;
    }

    public static void recibirDuracion(String duracion){
        DuracionDecorador.Duracion = duracion;
    }

    private void agregarDuracion(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Duracion);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.InsertarComponente(componente);
    }

    private void actualizarDuracion(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Duracion);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.ActualizarComponente(componente);
    }
}
