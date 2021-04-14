package com.utpl.agendadocente.decorador;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Componente;

public class EnviarComponente {

    private OperacionesComponente operacionesComponente;
    private Componente componente1 = new Componente();

    public void insertarComponente(String componente, Context context, String valor, Integer id){
        operacionesComponente = new OperacionesComponente(context);
        componente1.setComponente(componente);
        componente1.setValor(valor);
        componente1.setIdAsig(id);
        operacionesComponente.insertarComponente(componente1);
    }

    public void actualizarComponente(String componente, Context context, String valor, Integer id){
        operacionesComponente = new OperacionesComponente(context);
        componente1.setComponente(componente);
        componente1.setValor(valor);
        componente1.setIdAsig(id);
        operacionesComponente.actualizarComponente(componente1);
    }

}
