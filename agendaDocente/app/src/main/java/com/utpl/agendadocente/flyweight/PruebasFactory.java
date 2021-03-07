package com.utpl.agendadocente.flyweight;

import android.util.Log;

import com.utpl.agendadocente.Model.Evaluacion;
import com.utpl.agendadocente.ui.evaluacion.IEvaluacion;

import java.util.HashMap;

public class PruebasFactory {
    private static final HashMap<String, Evaluacion> evaluacionMap = new HashMap<>();

    public static IEvaluacion.Prueba getPrueba(String bimestre) {
        Evaluacion evaluacion = evaluacionMap.get(bimestre);
        if(evaluacion == null) {
            evaluacion = new Evaluacion(bimestre);
            evaluacionMap.put(bimestre, evaluacion);
            Log.e(""+bimestre," " + bimestre);
        }
        return evaluacion;
    }
}
