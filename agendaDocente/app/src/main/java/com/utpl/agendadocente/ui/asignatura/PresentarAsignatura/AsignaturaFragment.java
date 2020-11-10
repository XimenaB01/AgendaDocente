package com.utpl.agendadocente.ui.asignatura.PresentarAsignatura;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.ui.asignatura.CrearAsignatura.AsignaturaCrearActivity;
import com.utpl.agendadocente.ui.asignatura.CrearAsignatura.AsignaturaCreateListener;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsignaturaFragment extends Fragment implements AsignaturaCreateListener {

    private TextView ListaAginaturaVacia;
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
        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

        ListaAginaturaVacia = view.findViewById(R.id.emptyListATextView);
        RecyclerView lisAsigRV = view.findViewById(R.id.listaAsig);

        listaAsignaturas = operacionesAsignatura.ListarAsig();

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
        crearAsignatura.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearAsignatura(Asignatura asignatura) {
        listaAsignaturas.add(asignatura);
        asignaturaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaAsignaturas.isEmpty()) {
            ListaAginaturaVacia.setVisibility(View.VISIBLE);
        }else{
            ListaAginaturaVacia.setVisibility(View.GONE);
        }
    }
}
