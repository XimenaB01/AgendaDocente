package com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion;


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
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.IEvaluacion;

import java.util.ArrayList;
import java.util.List;


public class EvaluacionesParalelo extends Fragment implements IEvaluacion.EvaluacionCrearListener {


    private static long idParalelo;
    private TextView listaPeriodoVacia;
    private List<Evaluacion> evaluacionList = new ArrayList<>();
    private EvaluacionListaRecycleViewAdapter evaluacionListaRecycleViewAdapter;

    public static EvaluacionesParalelo newInstance (int id){
        idParalelo = id;
        return new EvaluacionesParalelo();
    }

    public EvaluacionesParalelo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_evaluaciones, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RVevaluaciones);
        listaPeriodoVacia = view.findViewById(R.id.emptyListEvTextView);

        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
        evaluacionList = operacionesEvaluacion.obtenerEvaluacionesId(idParalelo);

        evaluacionListaRecycleViewAdapter = new EvaluacionListaRecycleViewAdapter(getContext(), evaluacionList, "Activity");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(evaluacionListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.evaluacionPE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvaluacionCreateDialog();
            }
        });
        return view;
    }

    private void viewVisibility() {
        if (evaluacionList.isEmpty()) {
            listaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            listaPeriodoVacia.setVisibility(View.GONE);
        }
    }

    private void openEvaluacionCreateDialog(){
        Integer id = (int) idParalelo;
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nueva Evaluación", this, id);
        evaluacionCrearActivity.setCancelable(false);
        evaluacionCrearActivity.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        evaluacionList.add(evaluacion);
        evaluacionListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }
}
