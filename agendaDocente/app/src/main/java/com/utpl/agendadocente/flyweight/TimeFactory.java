package com.utpl.agendadocente.flyweight;

import android.util.Log;

import com.utpl.agendadocente.Model.Horario;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.HashMap;

public class TimeFactory {

    private static final HashMap<String, Horario> horarioMap = new HashMap<>();

    public static IHorario.Time getHorario(String dia) {
        Horario horario = horarioMap.get(dia);
        if(horario == null) {
            horario = new Horario(dia);
            horarioMap.put(dia, horario);
            //Log.e(""+dia,"Creating circle of color : " + dia);
        }
        return horario;
    }
}