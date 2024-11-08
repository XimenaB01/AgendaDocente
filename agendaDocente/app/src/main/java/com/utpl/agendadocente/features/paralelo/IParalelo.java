package com.utpl.agendadocente.features.paralelo;

import com.utpl.agendadocente.model.Paralelo;

public class IParalelo {

    public interface ParaleloCrearListener {
        void onCrearParalelo(Paralelo paralelo);
    }

    public interface ActualizarParaleloListener {
        void onActualizarParalelo(Paralelo paralelo, int position);
    }

    public interface ReplicarParaleloListener {
        void onReplicarParalelo(Paralelo paralelo);
    }
}
