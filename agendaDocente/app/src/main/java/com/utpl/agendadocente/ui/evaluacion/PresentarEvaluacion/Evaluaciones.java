package com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.Model.Evaluacion;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.EvaluacionCrearListener;

import java.util.ArrayList;
import java.util.List;


public class Evaluaciones extends Fragment implements EvaluacionCrearListener {


    private static long IdParalelo;
    private TextView ListaPeriodoVacia;
    private List<Evaluacion> ListaEvaluacion = new ArrayList<>();
    private EvaluacionListaRecycleViewAdapter evaluacionListaRecycleViewAdapter;

    public static Evaluaciones newInstance (int id){
        IdParalelo = id;
        return new Evaluaciones();
    }

    public Evaluaciones() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_evaluaciones, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.RVevaluaciones);
        ListaPeriodoVacia = view.findViewById(R.id.emptyListEvTextView);

        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
        ListaEvaluacion = operacionesEvaluacion.obtenerEvaluacionesId(IdParalelo);

        evaluacionListaRecycleViewAdapter = new EvaluacionListaRecycleViewAdapter(getContext(), ListaEvaluacion, "Activity");
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
        if (ListaEvaluacion.isEmpty()) {
            ListaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            ListaPeriodoVacia.setVisibility(View.GONE);
        }
    }

    private void openEvaluacionCreateDialog(){
        Integer Id = (int) IdParalelo;
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nueva Evaluación", this, Id);
        evaluacionCrearActivity.setCancelable(false);
        evaluacionCrearActivity.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        ListaEvaluacion.add(evaluacion);
        evaluacionListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }
}
