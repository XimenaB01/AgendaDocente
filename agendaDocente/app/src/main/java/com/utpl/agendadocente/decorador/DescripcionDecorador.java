package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.DataBase.OperacionesComponente;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.decorador.intef.IAsignatura;

public class DescripcionDecorador extends AsignaturaDecorador {

    private static String Descripcion;
    private String Comp = "Descripcion";
    private Componente componente = new Componente();

    public DescripcionDecorador(IAsignatura.asignatura asignatura) {
        super(asignatura);
    }

    @Override
    public Asignatura agregarAsignatura(Asignatura asignatura, Context context) {
        Integer id = asignatura.getId_asignatura();
        Asignatura a = asignaturaDecoradora.agregarAsignatura(asignatura,context);
        if (id != null){
            atualizarDescripcion(a,context);
        }else {
            agregarDescripcion(a,context);
        }
        return a;
    }

    public static void recibirDescripcion(String descripcion){
        DescripcionDecorador.Descripcion = descripcion;
    }

    private void agregarDescripcion(Asignatura asignatura, Context context){
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Descripcion);
        componente.setIdAsig(asignatura.getId_asignatura());
        operacionesComponente.InsertarComponente(componente);
    }

    private void atualizarDescripcion(Asignatura a, Context context) {
        OperacionesComponente operacionesComponente = new OperacionesComponente(context);
        componente.setComponente(Comp);
        componente.setValor(Descripcion);
        componente.setIdAsig(a.getId_asignatura());
        operacionesComponente.ActualizarComponente(componente);

    }

}
