package com.utpl.agendadocente.features.horario;

import com.utpl.agendadocente.model.Horario;

public class IHorario {

    public interface HorarioCrearListener {
        void onCrearHorario(Horario horario);
    }

    public interface ActualizarHorarioListener {
        void onActualizarHorario(Horario horario, int position);
    }

}
