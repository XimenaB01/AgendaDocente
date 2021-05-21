package com.utpl.agendadocente.features.tarea.presentar_tarea;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.tarea.crear_tarea.TareaCrearActivity;
import com.utpl.agendadocente.features.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TareasParalelo extends Fragment implements ITarea.TareaCrearListener{

    private static long idParalelo;
    private TextView listaTareasVacia;
    private List<Tarea> listaTareas = new ArrayList<>();
    private TareaListaRecycleViewAdapter tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter();

    public static TareasParalelo newInstance (int id){
        idParalelo = id;
        return new TareasParalelo();
    }

    public TareasParalelo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RVtareas);
        listaTareasVacia = view.findViewById(R.id.emptyListTaTextView);

        OperacionesTarea operacionesTarea = (OperacionesTarea) OperacionesFactory.getOperacionTarea(getContext());
        listaTareas = operacionesTarea.obtenerTareasId(idParalelo);

        tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter(getContext(), listaTareas, "Activity");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(tareaListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.tareasPP);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogCrearNuevaTarea();
            }
        });

        return view;
    }

    private void viewVisibility() {
        if (listaTareas.isEmpty()) {
            listaTareasVacia.setVisibility(View.VISIBLE);
        }else{
            listaTareasVacia.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        listaTareas.add(tarea);
        tareaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    private void llamarDialogCrearNuevaTarea(){
        Integer id = (int) idParalelo;
        TareaCrearActivity crearTarea = TareaCrearActivity.newInstance("Nueva Tarea",this, id);
        crearTarea.setCancelable(false);
        crearTarea.show(getChildFragmentManager(), Utilidades.CREAR);
    }
}
