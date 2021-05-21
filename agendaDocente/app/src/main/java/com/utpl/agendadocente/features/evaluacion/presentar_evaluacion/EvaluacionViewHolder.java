package com.utpl.agendadocente.features.evaluacion.presentar_evaluacion;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

public class EvaluacionViewHolder  extends RecyclerView.ViewHolder {
    TextView fechEva;
    TextView tipoEva;
    TextView nombrePar;
    TextView nombEva;
    ImageView opcionesEva;
    EvaluacionViewHolder (View view){
        super(view);
        nombEva = view.findViewById(R.id.nomEvaTV);
        nombrePar = view.findViewById(R.id.nomParE);
        tipoEva = view.findViewById(R.id.TipoEvaTV);
        fechEva = view.findViewById(R.id.FechEvaTV);
        opcionesEva = view.findViewById(R.id.opcionesEva);
    }
}
