package com.utpl.agendadocente.observer;

import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.tarea.ITarea;
import com.utpl.agendadocente.ui.tarea.PresentarTarea.TareaListaRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImageEstadoObserver implements Observer {

    public ImageEstadoObserver(Subject simpleSubject){
        simpleSubject.registerObserver(this);
    }
    @Override
    public void update(Integer id, String estado, ITarea.ActualizarTareaListener listener) {
        TareaListaRecycleViewAdapter.imagen(notificarImagen(estado));
    }

    private List<Integer> notificarImagen(String estado){
        List<Integer> list = new ArrayList<>();
        switch (estado){
            case "Sin Enviar":
                list.add(R.drawable.ic_sin_enviar);
                list.add(R.color.material_on_primary_emphasis_high_type);
                break;
            case "Enviada":
                list.add(R.drawable.ic_enviada);
                list.add(R.color.colorPrimaryDark);
                break;
            case "Calificada":
                list.add(R.drawable.ic_calificada);
                list.add(R.color.colorImagen);
                break;
        }
        return list;
    }
}
