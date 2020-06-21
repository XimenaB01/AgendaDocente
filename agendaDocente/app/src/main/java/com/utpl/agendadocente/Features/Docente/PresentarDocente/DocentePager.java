package com.utpl.agendadocente.Features.Docente.PresentarDocente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesDocente;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Features.Docente.CrearDocente.DocenteCreateListener;
import com.utpl.agendadocente.Features.Docente.CrearDocente.CrearDocenteActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocentePager extends Fragment implements DocenteCreateListener {

    private TextView ListaDocentesVacia;
    private DocentLisaRecycleViewAdapter docenteListaRecycleViewAdapter;

    private List<Docente> listaDocentes = new ArrayList<>();

    public DocentePager() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_docente_pager, container, false);

        OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());

        RecyclerView lisDocRV = view.findViewById(R.id.listaDoc);
        ListaDocentesVacia = view.findViewById(R.id.emptyListTextView);

        listaDocentes = operacionesDocente.listarDoc();

        docenteListaRecycleViewAdapter = new DocentLisaRecycleViewAdapter(getContext(), listaDocentes);
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
        crearDocente.show(Objects.requireNonNull(getFragmentManager()), utilidades.CREAR);
    }

    @Override
    public void onCrearDocente(Docente docente) {
        listaDocentes.add(listaDocentes.size(),docente);
        docenteListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaDocentes.isEmpty()) {
            ListaDocentesVacia.setVisibility(View.VISIBLE);
        }else{
            ListaDocentesVacia.setVisibility(View.GONE);
        }
    }

}
