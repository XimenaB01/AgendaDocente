package com.utpl.agendadocente.features.horario.presentar_horario;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.util.MainActivity;
import com.utpl.agendadocente.features.horario.actualizar_horario.HorarioActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.horario.IHorario;

import java.util.List;

public class HorarioListaRecycleViewAdapter extends RecyclerView.Adapter<HorarioListaRecycleViewAdapter.HorarioViewHolder>{

    private Context context;
    private List<Horario> horarioLista;
    private OperacionesHorario operacionesHorario;

    public HorarioListaRecycleViewAdapter(){}

    public HorarioListaRecycleViewAdapter (Context context, List<Horario> horarioLista){
        this.context = context;
        this.horarioLista = horarioLista;
        operacionesHorario = (OperacionesHorario) OperacionesFactory.getOperacionHorario(context);
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horario,parent,false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HorarioViewHolder holder, int position) {
        final int itemPosicion = position;
        final Horario hor = horarioLista.get(position);

        holder.aula.setText(hor.getAula());
        holder.dia.setText(hor.getDia());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleHorario detalleHorario = DetalleHorario.newInstance(hor);
                detalleHorario.setCancelable(false);
                detalleHorario.show(((MainActivity)context).getSupportFragmentManager(),"Tag");
            }
        });

        holder.opcionesHor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesHor);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());
                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Eliminar")){
                            getDialogForEliminarItem(itemPosicion);
                        }else if (menuItem.getTitle().equals("Editar")){
                            HorarioActualizarActivity actHor = HorarioActualizarActivity.newInstance(hor.getIdHorario(), itemPosicion, new IHorario.ActualizarHorarioListener(){
                                @Override
                                public void onActualizarHorario(Horario horario, int position) {
                                    horarioLista.set(position,horario);
                                    notifyDataSetChanged();
                                }
                            });
                            actHor.show(((MainActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                        }
                        return true;
                    }
                });
                popupMenuEstados.show();
            }
        });
    }

    private void getDialogForEliminarItem(final int itemPosicion) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("¿Quiere eliminar esté Horario ?");
        alertDialogBuilder.setPositiveButton("Eliminar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eliminarHorario(itemPosicion);
                    }
                }
        );
        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialogHorario = alertDialogBuilder.create();
        dialogHorario.show();
    }

    private void eliminarHorario(int position){
        Horario horario = horarioLista.get(position);
        long count = operacionesHorario.eliminarHorario(horario.getIdHorario());
        if (count > 0){
            horarioLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Periodo eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Periodo no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return horarioLista.size();
    }

    public class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView dia;
        TextView aula;
        ImageView opcionesHor;
        HorarioViewHolder (View view){
            super(view);
            aula = view.findViewById(R.id.AulaTV);
            dia = view.findViewById(R.id.HorDia);
            opcionesHor= view.findViewById(R.id.opcionesHor);
        }
    }
}
