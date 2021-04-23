package com.utpl.agendadocente.ui.tarea.presentar_tarea;


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
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.tarea.crear_tarea.TareaCrearActivity;
import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TareaFragment extends Fragment implements ITarea.TareaCrearListener {

    private TextView listaTareaVacia;

    private List<Tarea> listaTarea = new ArrayList<>();
    private TareaListaRecycleViewAdapter tareaListaRecycleViewAdapter;

    public TareaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tarea, container, false);
        OperacionesTarea operacionesTarea = (OperacionesTarea) OperacionesFactory.getOperacionTarea(getContext());

        RecyclerView listTarRV = view.findViewById(R.id.listaTar);
        listaTareaVacia = view.findViewById(R.id.emptyListTTextView);

        listaTarea.addAll(operacionesTarea.listarTarea());

        tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter(getContext(), listaTarea,"Fragment");
        listTarRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listTarRV.setAdapter(tareaListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.tareaFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTareaCreateDialog();
            }
        });

        return view;
    }

    private void openTareaCreateDialog() {
        TareaCrearActivity crearTarea = TareaCrearActivity.newInstance("Nueva Tarea",this, null);
        crearTarea.setCancelable(false);
        crearTarea.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        listaTarea.add(tarea);
        tareaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    private void viewVisibility() {
        if (listaTarea.isEmpty()) {
            listaTareaVacia.setVisibility(View.VISIBLE);
        }else{
            listaTareaVacia.setVisibility(View.GONE);
        }
    }

}
