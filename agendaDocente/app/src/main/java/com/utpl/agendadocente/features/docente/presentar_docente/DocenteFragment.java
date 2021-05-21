package com.utpl.agendadocente.features.docente.presentar_docente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.docente.crear_docente.CrearDocenteActivity;
import com.utpl.agendadocente.features.docente.IDocente;

import java.util.ArrayList;
import java.util.List;

public class DocenteFragment extends Fragment implements IDocente.DocenteCreateListener {

    private TextView listaDocentesVacia;
    private DocenteListaRecycleViewAdapter docenteListaRecycleViewAdapter;

    private List<Docente> listaDocentes = new ArrayList<>();

    public DocenteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_docente, container, false);
        OperacionesDocente operacionesDocente = (OperacionesDocente) OperacionesFactory.getOperacionDocente(getContext());

        RecyclerView lisDocRV = view.findViewById(R.id.listaDoc);
        listaDocentesVacia = view.findViewById(R.id.emptyListTextView);

        listaDocentes = operacionesDocente.listarDocente();

        docenteListaRecycleViewAdapter = new DocenteListaRecycleViewAdapter(getContext(), listaDocentes);
        lisDocRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lisDocRV.setAdapter(docenteListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.docenteFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDocenteCreateDialog();
            }
        });

        return view;

    }

    private void openDocenteCreateDialog() {
        CrearDocenteActivity crearDocente = CrearDocenteActivity.newInstance("Crear Docente", this);
        crearDocente.setCancelable(false);
        crearDocente.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearDocente(Docente docente) {
        listaDocentes.add(listaDocentes.size(),docente);
        docenteListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaDocentes.isEmpty()) {
            listaDocentesVacia.setVisibility(View.VISIBLE);
        }else{
            listaDocentesVacia.setVisibility(View.GONE);
        }
    }
}
