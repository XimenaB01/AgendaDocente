package com.utpl.agendadocente.Features.Docente.PresentarDocente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesDocente;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Features.Docente.ActualizarDocente.ActualizarDocenteListener;
import com.utpl.agendadocente.Features.Docente.ActualizarDocente.actualizarDocente;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class DocentLisaRecycleViewAdapter extends RecyclerView.Adapter<DocenteViewHolder>{

    private Context context;
    private List<Docente> docenteLista;
    private OperacionesDocente operacionesDocente;

    public DocentLisaRecycleViewAdapter(){}

    public DocentLisaRecycleViewAdapter(Context context, List<Docente> docentLisa){
        this.context = context;
        this.docenteLista = docentLisa;
        operacionesDocente = new OperacionesDocente(context);
    }

    @NonNull
    @Override
    public DocenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_docente,parent,false);
        return new DocenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocenteViewHolder holder, int position) {
        final int itemPosition = position;
        final Docente docente = docenteLista.get(position);

        holder.nombre.setText(String.format("%s %s",docente.getNombreDocente(), docente.getApellidoDocente()));
        holder.email.setText(docente.getEmail());
        holder.cedula.setText(docente.getCedula());

        holder.eliminarDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar a este DocenteParger?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminateDocent(itemPosition);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editarDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarDocente actDoc = actualizarDocente.newInstance(docente.getId_docente(), itemPosition,new ActualizarDocenteListener(){
                    @Override
                    public void onActualizarDocente(Docente doc, int position) {
                        docenteLista.set(position,doc);
                        notifyDataSetChanged();
                    }
                });
                actDoc.show(((MainActivity) context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });
    }

    private void eliminateDocent(int position){
        Docente doc = docenteLista.get(position);
        long count = operacionesDocente.eliminarDoc(doc.getId_docente());
        if (count>0){
            docenteLista.remove(position);
            notifyDataSetChanged();
            //((DocentePager)context).viewVisibility();
            Toast.makeText(context, "Docente eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Docente no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return docenteLista.size();
    }
}