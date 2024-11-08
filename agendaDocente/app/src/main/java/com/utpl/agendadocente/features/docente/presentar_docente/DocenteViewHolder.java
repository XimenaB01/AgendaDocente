package com.utpl.agendadocente.features.docente.presentar_docente;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

class DocenteViewHolder extends RecyclerView.ViewHolder {
    TextView nombre;
    TextView email;
    TextView cedula;
    ImageView opcionesDoc;

    public DocenteViewHolder (View view){
        super(view);

        nombre = view.findViewById(R.id.nombreTV);
        email = view.findViewById(R.id.emailTV);
        cedula = view.findViewById(R.id.cedulaTV);
        opcionesDoc = view.findViewById(R.id.opcionesDoc);

    }
}
