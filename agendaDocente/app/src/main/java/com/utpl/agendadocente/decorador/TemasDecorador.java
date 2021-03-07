package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.DataBase.OperacionesComponente;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

public class TemasDecorador extends AsignaturaDecorador {

    private static String Temas;
    private String Comp = "Temas";
    private Componente componente = new Componente();

    public TemasDecorador(IAsignatura.asignatura asignatura) {
        super(asignatura);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getId_asignatura();
        asignaturaDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarTemas(asignatura,context);
        }else {
            agregarTemas(asignatura, context);
        }
        return asignatura;
    }

    public static void recibirTemas(String temas){
        TemasDecorador.Temas = temas;
    }

    private void agregarTemas(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Temas);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.InsertarComponente(componente);

    }

    private void actualizarTemas(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Temas);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.ActualizarComponente(componente);
    }



}
