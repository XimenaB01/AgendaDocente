package com.utpl.agendadocente.ui.horario.PresentarHorario;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Model.Horario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.horario.CrearHorario.HorarioCrearActivity;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HorarioFragment extends Fragment implements IHorario.HorarioCrearListener {

    private TextView ListaHorarioVacia;

    private List<Horario> listaHorario = new ArrayList<>();
    private HorarioListaRecycleViewAdapter horarioListaRecycleViewAdapter;

    public HorarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horario, container, false);
        OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());

        RecyclerView listHorRV = view.findViewById(R.id.listaHor);
        ListaHorarioVacia = view.findViewById(R.id.emptyListHTextView);

        listaHorario.addAll(operacionesHorario.ListarHor());
        horarioListaRecycleViewAdapter = new HorarioListaRecycleViewAdapter(getContext(), listaHorario);
        listHorRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        listHorRV.setAdapter(horarioListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.horarioFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHorarioCreateDialog();
            }
        });

        return view;
    }

    @Override
    public void onCrearHorario(Horario horario) {
        listaHorario.add(horario);
        horarioListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaHorario.isEmpty()) {
            ListaHorarioVacia.setVisibility(View.VISIBLE);
        }else{
            ListaHorarioVacia.setVisibility(View.GONE);
        }
    }

    private void openHorarioCreateDialog() {
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Nuevo Horario", this);
        horarioCrearActivity.setCancelable(false);
        horarioCrearActivity.show(getChildFragmentManager(), utilidades.CREAR);
    }
}
