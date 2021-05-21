package com.utpl.agendadocente.features.cuestionario.presentar_cuestionario;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

public class CuestionarioViewHolder extends RecyclerView.ViewHolder {

    TextView nombCuest;
    ImageView opcionesCues;

    public CuestionarioViewHolder(@NonNull View view) {
        super(view);
        nombCuest = view.findViewById(R.id.txttitleCuestionario);
        opcionesCues = view.findViewById(R.id.opcionesCues);
    }
}
