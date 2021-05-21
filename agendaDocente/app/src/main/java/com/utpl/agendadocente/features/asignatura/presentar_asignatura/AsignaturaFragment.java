package com.utpl.agendadocente.features.asignatura.presentar_asignatura;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.features.asignatura.crear_asignatura.AsignaturaCrearActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.asignatura.IAsignatura;

import java.util.ArrayList;
import java.util.List;

public class AsignaturaFragment extends Fragment implements IAsignatura.AsignaturaCreateListener{

    private TextView listaAginaturaVacia;
    private AsignaturaListaRecycleViewAdapter asignaturaListaRecycleViewAdapter;

    private List<Asignatura> listaAsignaturas = new ArrayList<>();

    public AsignaturaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asignatura, container, false);
        OperacionesAsignatura operacionesAsignatura = (OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(getContext());

        listaAginaturaVacia = view.findViewById(R.id.emptyListATextView);
        RecyclerView lisAsigRV = view.findViewById(R.id.listaAsig);

        listaAsignaturas = operacionesAsignatura.listarAsignatura();

        asignaturaListaRecycleViewAdapter = new AsignaturaListaRecycleViewAdapter(getContext(), listaAsignaturas);
        lisAsigRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lisAsigRV.setAdapter(asignaturaListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.asignaturaFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAsignaturaCreateDialog();
            }
        });
        return view;
    }

    private void openAsignaturaCreateDialog() {
        AsignaturaCrearActivity crearAsignatura = AsignaturaCrearActivity.newInstance("Crear Asignatura", this);
        crearAsignatura.setCancelable(false);
        crearAsignatura.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearAsignatura(Asignatura asignatura) {
        listaAsignaturas.add(asignatura);
        asignaturaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaAsignaturas.isEmpty()) {
            listaAginaturaVacia.setVisibility(View.VISIBLE);
        }else{
            listaAginaturaVacia.setVisibility(View.GONE);
        }
    }
}
