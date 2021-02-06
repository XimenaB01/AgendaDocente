package com.utpl.agendadocente.ui.periodo.PresentarPeriodo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Model.PeriodoAcademico;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.periodo.ActualizarPeriodo.ActualizarPeriodoListener;
import com.utpl.agendadocente.ui.periodo.ActualizarPeriodo.PeriodoActualizarActivity;
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
    public void onBindViewHolder(final PeriodoViewHolder holder, int position) {
        final int itemPosicion = position;
        final PeriodoAcademico periodo = periodoLista.get(position);

        holder.FechaInicio.setText(periodo.getFechaInicio());
        holder.FechaFin.setText(periodo.getFechaFin());

        holder.opcionesPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesPer);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            PeriodoActualizarActivity actPer = PeriodoActualizarActivity.newInstance(periodo.getId_periodo(), itemPosicion, new ActualizarPeriodoListener(){
                                @Override
                                public void onActualizarPeriodo(PeriodoAcademico periodo, int position) {
                                    periodoLista.set(position,periodo);
                                    notifyDataSetChanged();
                                }
                            });
                            actPer.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
                        }else if (menuItem.getTitle().equals("Eliminar")){
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
                        return true;
                    }
                });

                popupMenuEstados.show();

            }
        });

    }

    private void eliminarPeriodo(int position){
        PeriodoAcademico periodo = periodoLista.get(position);
        long count = operacionesPeriodo.eliminarPer(periodo.getId_periodo());
        if (count > 0){
            periodoLista.remove(position);
            notifyDataSetChanged();
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
        ImageView opcionesPer;
        PeriodoViewHolder (View view){
            super(view);
            FechaInicio = view.findViewById(R.id.PerInTV);
            FechaFin = view.findViewById(R.id.PerFinTV);
            opcionesPer= view.findViewById(R.id.opcionesPer);
        }
    }
}
