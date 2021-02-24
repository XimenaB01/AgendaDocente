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
import com.utpl.agendadocente.ui.evaluacion.IEvaluacion;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluacionFragment extends Fragment implements IEvaluacion.EvaluacionCrearListener {

    private TextView ListaPeriodoVacia;

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
        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());

        RecyclerView listEvaRV = view.findViewById(R.id.listaEva);
        ListaPeriodoVacia = view.findViewById(R.id.emptyListETextView);

        listaEvaluacion.addAll(operacionesEvaluacion.ListarEva());

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
            ListaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            ListaPeriodoVacia.setVisibility(View.GONE);
        }
    }

    private void openEvaluacionCreateDialog(){
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nueva Evaluación", this, null);
        evaluacionCrearActivity.setCancelable(false);
        evaluacionCrearActivity.show(getChildFragmentManager(), utilidades.CREAR);
    }
}
