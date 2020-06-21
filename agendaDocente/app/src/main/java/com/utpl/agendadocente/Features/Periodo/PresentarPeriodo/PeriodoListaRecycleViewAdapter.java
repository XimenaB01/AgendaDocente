package com.utpl.agendadocente.Features.Periodo.PresentarPeriodo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Features.Periodo.ActualizarPeriodo.ActualizarPeriodoListener;
import com.utpl.agendadocente.Features.Periodo.ActualizarPeriodo.PeriodoActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class PeriodoListaRecycleViewAdapter extends RecyclerView.Adapter<PeriodoListaRecycleViewAdapter.PeriodoViewHolder>{

    private Context context;
    private List<PeriodoAcademico> periodoLista;
    private OperacionesPeriodo operacionesPeriodo;

    public PeriodoListaRecycleViewAdapter(){}

    public PeriodoListaRecycleViewAdapter(Context context, List<PeriodoAcademico> periodoLista){
        this.context = context;
        this.periodoLista = periodoLista;
        operacionesPeriodo = new OperacionesPeriodo(context);
    }

    @NonNull
    @Override
    public PeriodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_periodo,parent,false);
        return new PeriodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PeriodoViewHolder holder, int position) {
        final int itemPosicion = position;
        final PeriodoAcademico periodo = periodoLista.get(position);

        holder.FechaInicio.setText(periodo.getFechaInicio());
        holder.FechaFin.setText(periodo.getFechaFin());

        holder.eliminarPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar este Periodo?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarPeriodo(itemPosicion);
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

        holder.editarPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PeriodoActualizarActivity actPer = PeriodoActualizarActivity.newInstance(periodo.getId_periodo(), itemPosicion, new ActualizarPeriodoListener(){
                    @Override
                    public void onActualizarPeriodo(PeriodoAcademico periodo, int position) {
                        periodoLista.set(position,periodo);
                        notifyDataSetChanged();
                    }
                });
                actPer.show(((PeriodoListarActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });
    }

    private void eliminarPeriodo(int position){
        PeriodoAcademico periodo = periodoLista.get(position);
        long count = operacionesPeriodo.eliminarPer(periodo.getId_periodo());
        if (count > 0){
            periodoLista.remove(position);
            notifyDataSetChanged();
            ((PeriodoListarActivity) context).viewVisibility();
            Toast.makeText(context, "Periodo eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Periodo no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return periodoLista.size();
    }

    public class PeriodoViewHolder extends RecyclerView.ViewHolder {
        TextView FechaInicio , FechaFin;
        ImageView eliminarPer, editarPer;
        PeriodoViewHolder (View view){
            super(view);
            //context = view.getContext();
            FechaInicio = view.findViewById(R.id.PerInTV);
            FechaFin = view.findViewById(R.id.PerFinTV);
            editarPer= view.findViewById(R.id.editarPer);
            eliminarPer = view.findViewById(R.id.eliminarPer);
        }
    }
}
