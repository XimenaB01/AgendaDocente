package com.utpl.agendadocente.ui.tarea.PresentarTarea;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.tarea.ActualizarTarea.ActualizarTareaListener;
import com.utpl.agendadocente.ui.tarea.ActualizarTarea.TareaActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class TareaListaRecycleViewAdapter extends RecyclerView.Adapter<TareaListaRecycleViewAdapter.TareaViewHolder> {

    private Context context;
    private List<Tarea> tareaLista;
    private OperacionesTarea operacionesTarea;


    public TareaListaRecycleViewAdapter(){}

    public TareaListaRecycleViewAdapter(Context context, List<Tarea> tareaLista){
        this.context = context;
        this.tareaLista = tareaLista;
        operacionesTarea = new OperacionesTarea(context);
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea,parent,false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TareaViewHolder holder, int position) {
        final int itemPosicion = position;
        final Tarea tarea = tareaLista.get(position);

        holder.nombreTar.setText(tarea.getNombreTarea());
        holder.descripcionTar.setText(tarea.getDescripcionTarea());
        holder.fechaTar.setText(tarea.getFechaTarea());

        holder.eliminarTar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esta Tarea?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarTarea(itemPosicion);
                            }
                        }
                );
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

        holder.editarTar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TareaActualizarActivity actTar = TareaActualizarActivity.newInstance(tarea.getId_tarea(), itemPosicion, new ActualizarTareaListener(){
                    @Override
                    public void onActualizarTarea(Tarea tarea1, int position) {
                        tareaLista.set(position,tarea1);
                        notifyDataSetChanged();
                    }
                });
                actTar.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaDetalle tareaDetalle = TareaDetalle.newInstance(tarea.getId_tarea());
                tareaDetalle.setCancelable(false);
                tareaDetalle.show(((MainActivity)context).getSupportFragmentManager(),"tag");
            }
        });
    }

    private void eliminarTarea(int position){
        Tarea tarea = tareaLista.get(position);
        long count = operacionesTarea.eliminarTar(tarea.getId_tarea());
        if (count > 0){
            tareaLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Periodo eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Periodo no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
            return tareaLista.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTar , descripcionTar, fechaTar;
        ImageView eliminarTar, editarTar;
        TareaViewHolder (View view){
            super(view);
            nombreTar = view.findViewById(R.id.nomTarTV);
            descripcionTar = view.findViewById(R.id.desTarTV);
            fechaTar = view.findViewById(R.id.fechTarTV);
            editarTar= view.findViewById(R.id.editarTar);
            eliminarTar = view.findViewById(R.id.eliminarTar);
        }
    }
}
