package com.utpl.agendadocente.ui.asignatura.PresentarAsignatura;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

public class AsignaturaViewHolder extends RecyclerView.ViewHolder {
    TextView nombreA, horasA, creditosA, carreraA;
    ImageView opcionesAsig;
    
    public AsignaturaViewHolder (View view){
        super(view);
        nombreA = view.findViewById(R.id.nomAsigTV);
        horasA = view.findViewById(R.id.horAsigTV);
        creditosA = view.findViewById(R.id.credAsigTV);
        carreraA = view.findViewById(R.id.carreraAsigTV);
        opcionesAsig = view.findViewById(R.id.opcionesAsig);
    }
}
