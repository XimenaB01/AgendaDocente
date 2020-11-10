package com.utpl.agendadocente.ui.docente.PresentarDocente;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

class DocenteViewHolder extends RecyclerView.ViewHolder {
    TextView nombre, email, cedula;
    ImageView eliminarDoc, editarDoc;

    public DocenteViewHolder (View view){
        super(view);

        nombre = view.findViewById(R.id.nombreTV);
        email = view.findViewById(R.id.emailTV);
        cedula = view.findViewById(R.id.cedulaTV);
        editarDoc = view.findViewById(R.id.editarDoc);
        eliminarDoc = view.findViewById(R.id.eliminarDoc);

    }
}
