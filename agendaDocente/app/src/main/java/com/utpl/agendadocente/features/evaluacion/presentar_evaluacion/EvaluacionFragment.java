package com.utpl.agendadocente.features.evaluacion.presentar_evaluacion;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.features.evaluacion.IEvaluacion;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluacionFragment extends Fragment implements IEvaluacion.EvaluacionCrearListener {

    private TextView listaPeriodoVacia;

    private List<Evaluacion> listaEvaluacion = new ArrayList<>();
    private EvaluacionListaRecycleViewAdapter evaluacionListaRecycleViewAdapter;

    public EvaluacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evaluacion, container, false);
        OperacionesEvaluacion operacionesEvaluacion = (OperacionesEvaluacion) OperacionesFactory.getOperacionEvaluacion(getContext());

        RecyclerView listEvaRV = view.findViewById(R.id.listaEva);
        listaPeriodoVacia = view.findViewById(R.id.emptyListETextView);

        listaEvaluacion.addAll(operacionesEvaluacion.listarEvaluacion());

        evaluacionListaRecycleViewAdapter = new EvaluacionListaRecycleViewAdapter(getContext(), listaEvaluacion, "Fragment");
        listEvaRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listEvaRV.setAdapter(evaluacionListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.evaluacionFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvaluacionCreateDialog();
            }
        });

        return view;
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        listaEvaluacion.add(evaluacion);
        evaluacionListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    private void viewVisibility() {
        if (listaEvaluacion.isEmpty()) {
            listaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            listaPeriodoVacia.setVisibility(View.GONE);
        }
    }

    private void openEvaluacionCreateDialog(){
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nueva Evaluación", this, null);
        evaluacionCrearActivity.setCancelable(false);
        evaluacionCrearActivity.show(getChildFragmentManager(), Utilidades.CREAR);
    }
}
