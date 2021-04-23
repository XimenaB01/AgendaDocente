package com.utpl.agendadocente.ui.periodo.presentar_periodo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.periodo.crear_periodo.PeriodoCrearActivity;
import com.utpl.agendadocente.ui.periodo.IPeriodo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeriodoFragment extends Fragment implements IPeriodo.PeriodoCreateListener {

    private TextView listaPeriodoVacia;

    private List<PeriodoAcademico> listaPeriodo = new ArrayList<>();
    private PeriodoListaRecycleViewAdapter periodoListaRecycleViewAdapter;

    public PeriodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_periodo, container, false);
        OperacionesPeriodo operacionesPeriodo = (OperacionesPeriodo) OperacionesFactory.getOperacionPeriodo(getContext());

        RecyclerView listPerRV = view.findViewById(R.id.listaPer);
        listaPeriodoVacia = view.findViewById(R.id.emptyListPTextView);

        listaPeriodo.addAll(operacionesPeriodo.listarPeriodo());

        periodoListaRecycleViewAdapter = new PeriodoListaRecycleViewAdapter(getContext(), listaPeriodo);
        listPerRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listPerRV.setAdapter(periodoListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.periodoFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPeriodoCreateDialog();
            }
        });
        return view;
    }

    private void openPeriodoCreateDialog() {
        PeriodoCrearActivity crearPeriodo = PeriodoCrearActivity.newInstance("Nuevo Periodo",this);
        crearPeriodo.setCancelable(false);
        crearPeriodo.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodo) {
        listaPeriodo.add(periodo);
        periodoListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaPeriodo.isEmpty()) {
            listaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            listaPeriodoVacia.setVisibility(View.GONE);
        }
    }
}
