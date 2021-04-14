package com.utpl.agendadocente.flyweight;

import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.HashMap;

public class TimeFactory {

    private static final HashMap<String, Horario> horarioMap = new HashMap<>();

    private TimeFactory(){
        //Requires constructor
    }

    public static IHorario.Time getHorario(String dia) {
        Horario horario = horarioMap.get(dia);
        if(horario == null) {
            horario = new Horario(dia);
            horarioMap.put(dia, horario);
        }
        return horario;
    }
}