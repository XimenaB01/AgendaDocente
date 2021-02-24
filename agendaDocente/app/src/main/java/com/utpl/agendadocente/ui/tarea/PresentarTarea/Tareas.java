package com.utpl.agendadocente.ui.tarea.PresentarTarea;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.tarea.CrearTarea.TareaCrearActivity;
import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tareas extends Fragment implements ITarea.TareaCrearListener{

    private static long IdParalelo;
    private RecyclerView recyclerView;
    private TextView ListaTareasVacia;
    private List<Tarea> ListaTareas = new ArrayList<>();
    private TareaListaRecycleViewAdapter tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter();

    public static Tareas newInstance (int id){
        IdParalelo = id;
        return new Tareas();
    }

    public Tareas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tareas, container, false);

        recyclerView = view.findViewById(R.id.RVtareas);
        ListaTareasVacia = view.findViewById(R.id.emptyListTaTextView);

        OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
        ListaTareas = operacionesTarea.obtenerTareasId(IdParalelo);

        tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter(getContext(), ListaTareas, "Activity");
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
        if (ListaTareas.isEmpty()) {
            ListaTareasVacia.setVisibility(View.VISIBLE);
        }else{
            ListaTareasVacia.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        ListaTareas.add(tarea);
        tareaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    private void llamarDialogCrearNuevaTarea(){
        Integer Id = (int)IdParalelo;
        TareaCrearActivity crearTarea = TareaCrearActivity.newInstance("Nueva Tarea",this, Id);
        crearTarea.setCancelable(false);
        crearTarea.show(getChildFragmentManager(), utilidades.CREAR);
    }
}
