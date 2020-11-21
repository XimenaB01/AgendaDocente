package com.utpl.agendadocente.ui.paralelo.PresentarParalelo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.paralelo.CrearParalelo.ParaleloCrearListener;
import com.utpl.agendadocente.ui.paralelo.CrearParalelo.crearParaleloActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParaleloFragment extends Fragment implements ParaleloCrearListener {

    private TextView ListaParaleloVacia;

    private List<Paralelo> listaParalelo = new ArrayList<>();
    private ParaleloListaRecycleViewAdapter paraleloListaRecycleViewAdapter;

    public ParaleloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paralelo, container, false);

        OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

        RecyclerView listParRV = view.findViewById(R.id.listaPar);
        ListaParaleloVacia = view.findViewById(R.id.emptyListPaTextView);

        listaParalelo = operacionesParalelo.ListarPar();

        paraleloListaRecycleViewAdapter = new ParaleloListaRecycleViewAdapter(getContext(), listaParalelo);
        listParRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        listParRV.setAdapter(paraleloListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton  = view.findViewById(R.id.paraleloFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openParaleloCreateDialog();
            }
        });

        return view;
    }


    private void openParaleloCreateDialog() {
        crearParaleloActivity crearPalelo = crearParaleloActivity.newInstance("Crear Paralelo", this);
        crearPalelo.setCancelable(false);
        crearPalelo.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearParalelo(Paralelo paralelo) {
        listaParalelo.add(paralelo);
        paraleloListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaParalelo.isEmpty()) {
            ListaParaleloVacia.setVisibility(View.VISIBLE);
        }else{
            ListaParaleloVacia.setVisibility(View.GONE);
        }
    }

}
