package com.utpl.agendadocente.ui.periodo.PresentarPeriodo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Model.PeriodoAcademico;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.periodo.CrearPeriodo.PeriodoCrearActivity;
import com.utpl.agendadocente.ui.periodo.CrearPeriodo.PeriodoCreateListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeriodoFragment extends Fragment implements PeriodoCreateListener {

    private RecyclerView listPerRV;
    private TextView ListaPeriodoVacia;

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
        OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());

        listPerRV = view.findViewById(R.id.listaPer);
        ListaPeriodoVacia = view.findViewById(R.id.emptyListPTextView);

        listaPeriodo.addAll(operacionesPeriodo.ListarPer());

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
        crearPeriodo.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodo) {
        listaPeriodo.add(periodo);
        periodoListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaPeriodo.isEmpty()) {
            ListaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            ListaPeriodoVacia.setVisibility(View.GONE);
        }
    }
}
