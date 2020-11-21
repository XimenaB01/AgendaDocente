package com.utpl.agendadocente.ui.tarea.PresentarTarea;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.tarea.CrearTarea.TareaCrearActivity;
import com.utpl.agendadocente.ui.tarea.CrearTarea.TareaCrearListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TareaFragment extends Fragment implements TareaCrearListener {

    private TextView ListaTareaVacia;

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
        OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());

        RecyclerView listTarRV = view.findViewById(R.id.listaTar);
        ListaTareaVacia = view.findViewById(R.id.emptyListTTextView);

        listaTarea.addAll(operacionesTarea.ListarTar());

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

    public void openTareaCreateDialog() {
        TareaCrearActivity crearTarea = TareaCrearActivity.newInstance("Nueva Tarea",this, null);
        crearTarea.setCancelable(false);
        crearTarea.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        listaTarea.add(tarea);
        tareaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    private void viewVisibility() {
        if (listaTarea.isEmpty()) {
            ListaTareaVacia.setVisibility(View.VISIBLE);
        }else{
            ListaTareaVacia.setVisibility(View.GONE);
        }
    }

}
