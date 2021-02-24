package com.utpl.agendadocente.ui.cuestionario.PresentarCuestionario;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.Model.Cuestionario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.cuestionario.CrearCuestionario.CuestionarioCrearActivity;
import com.utpl.agendadocente.ui.cuestionario.ICuestionario;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CuestionarioFragment extends Fragment implements ICuestionario.CuestionarioCrearListener {

    private List<Cuestionario> listaCuestionario = new ArrayList<>();

    private TextView ListaCuestionarioVacia;
    private CuestionarioListaRecycleViewAdapter cuestionarioListaRecycleViewAdapter;

    public CuestionarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cuestionario, container, false);
        OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());

        RecyclerView lisCuestRV = view.findViewById(R.id.RVCuestionario);
        ListaCuestionarioVacia = view.findViewById(R.id.emptyListCTextView);

        listaCuestionario = operacionesCuestionario.ListarCuest();

        cuestionarioListaRecycleViewAdapter = new CuestionarioListaRecycleViewAdapter(getContext(), listaCuestionario);
        lisCuestRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lisCuestRV.setAdapter(cuestionarioListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.cuestionarioFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCuestionarioCreateDialog();
            }
        });

        return view;
    }

    public void viewVisibility() {
        if (listaCuestionario.isEmpty()) {
            ListaCuestionarioVacia.setVisibility(View.VISIBLE);
        }else{
            ListaCuestionarioVacia.setVisibility(View.GONE);
        }
    }

    private void openCuestionarioCreateDialog() {
        CuestionarioCrearActivity crearCuestionario = CuestionarioCrearActivity.newInstance("Nuevo Cuestionario", this);
        crearCuestionario.setCancelable(false);
        crearCuestionario.show(getChildFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearCuestionario(Cuestionario cuestionario) {
        listaCuestionario.add(cuestionario);
        cuestionarioListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }
}
