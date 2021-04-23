package com.utpl.agendadocente.ui.periodo.presentar_periodo;

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

import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.periodo.actualizar_periodo.PeriodoActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.periodo.IPeriodo;

import java.util.List;

public class PeriodoListaRecycleViewAdapter extends RecyclerView.Adapter<PeriodoListaRecycleViewAdapter.PeriodoViewHolder>{

    private Context context;
    private List<PeriodoAcademico> periodoLista;
    private OperacionesPeriodo operacionesPeriodo;

    public PeriodoListaRecycleViewAdapter(){}

    public PeriodoListaRecycleViewAdapter(Context context, List<PeriodoAcademico> periodoLista){
        this.context = context;
        this.periodoLista = periodoLista;
        operacionesPeriodo = (OperacionesPeriodo) OperacionesFactory.getOperacionPeriodo(context);
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

        holder.fechaInicio.setText(periodo.getFechaInicio());
        holder.fechaFin.setText(periodo.getFechaFin());

        holder.opcionesPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesPer);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            PeriodoActualizarActivity actPer = PeriodoActualizarActivity.newInstance(periodo.getIdPeriodo(), itemPosicion, new IPeriodo.ActualizarPeriodoListener(){
                                @Override
                                public void onActualizarPeriodo(PeriodoAcademico periodo, int position) {
                                    periodoLista.set(position,periodo);
                                    notifyDataSetChanged();
                                }
                            });
                            actPer.show(((MainActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                        }else if (menuItem.getTitle().equals("Eliminar")){
                            getDialogEliminarItem(itemPosicion);
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
        long count = operacionesPeriodo.eliminarPeriodo(periodo.getIdPeriodo());
        if (count > 0){
            periodoLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Periodo eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Periodo no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    private void getDialogEliminarItem(final int itemPosicion){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("¿Quiere eliminar este Periodo?");
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

    @Override
    public int getItemCount() {
        return periodoLista.size();
    }

    public class PeriodoViewHolder extends RecyclerView.ViewHolder {
        TextView fechaInicio;
        TextView fechaFin;
        ImageView opcionesPer;
        PeriodoViewHolder (View view){
            super(view);
            fechaInicio = view.findViewById(R.id.PerInTV);
            fechaFin = view.findViewById(R.id.PerFinTV);
            opcionesPer= view.findViewById(R.id.opcionesPer);
        }
    }
}
