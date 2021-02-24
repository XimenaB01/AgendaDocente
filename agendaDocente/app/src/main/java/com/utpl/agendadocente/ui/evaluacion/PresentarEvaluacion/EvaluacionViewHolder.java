package com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

public class EvaluacionViewHolder  extends RecyclerView.ViewHolder {
    TextView fechEva, tipoEva, nombEva;
    ImageView opcionesEva;
    EvaluacionViewHolder (View view){
        super(view);
        nombEva = view.findViewById(R.id.nomEvaTV);
        tipoEva = view.findViewById(R.id.TipoEvaTV);
        fechEva = view.findViewById(R.id.FechEvaTV);
        opcionesEva = view.findViewById(R.id.opcionesEva);
    }
}
