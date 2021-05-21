package com.utpl.agendadocente.features.paralelo.presentar_paralelo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.paralelo.crear_paralelo.CrearParaleloActivity;
import com.utpl.agendadocente.features.paralelo.IParalelo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParaleloFragment extends Fragment implements IParalelo.ParaleloCrearListener {

    private TextView listaParaleloVacia;

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

        OperacionesParalelo operacionesParalelo = (OperacionesParalelo) OperacionesFactory.getOperacionParalelo(getContext());

        RecyclerView listParRV = view.findViewById(R.id.listaPar);
        listaParaleloVacia = view.findViewById(R.id.emptyListPaTextView);

        listaParalelo = operacionesParalelo.listarParalelo();

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
        CrearParaleloActivity crearPalelo = CrearParaleloActivity.newInstance("Crear Paralelo", this);
        crearPalelo.setCancelable(false);
        crearPalelo.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    @Override
    public void onCrearParalelo(Paralelo paralelo) {
        listaParalelo.add(paralelo);
        paraleloListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaParalelo.isEmpty()) {
            listaParaleloVacia.setVisibility(View.VISIBLE);
        }else{
            listaParaleloVacia.setVisibility(View.GONE);
        }
    }

}
