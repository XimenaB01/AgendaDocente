package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.DataBase.OperacionesComponente;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

public class NivelDecorador extends AsignaturaDecorador {

    private static String Nivel;
    private String Comp = "Nivel";
    private Componente componente = new Componente();

    public NivelDecorador(IAsignatura.asignatura asignatura) {
        super(asignatura);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getId_asignatura();
        asignaturaDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            actualizarNivel(asignatura,context);
        }else {
            agregarNivel(asignatura, context);
        }
        return asignatura;
    }

    public static void recibirNivel(String nivel){
        NivelDecorador.Nivel = nivel;
    }

    private void agregarNivel(Asignatura a, Context context) {
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Nivel);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.InsertarComponente(componente);
    }

    private void actualizarNivel(Asignatura a, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Nivel);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.ActualizarComponente(componente);
    }

}
