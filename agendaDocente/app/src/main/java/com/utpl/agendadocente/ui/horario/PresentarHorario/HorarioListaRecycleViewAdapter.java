package com.utpl.agendadocente.ui.horario.PresentarHorario;

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

import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Model.Horario;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.horario.ActualizarHorario.HorarioActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.List;

public class HorarioListaRecycleViewAdapter extends RecyclerView.Adapter<HorarioListaRecycleViewAdapter.HorarioViewHolder>{

    private Context context;
    private List<Horario> horarioLista;
    private OperacionesHorario operacionesHorario;

    public HorarioListaRecycleViewAdapter(){}

    public HorarioListaRecycleViewAdapter (Context context, List<Horario> horarioLista){
        this.context = context;
        this.horarioLista = horarioLista;
        operacionesHorario = new OperacionesHorario(context);
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

        holder.Aula.setText(hor.getAula());
        holder.Dia.setText(hor.getDia());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleHorario detalleHorario = DetalleHorario.newInstance(hor, hor.getDia());
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
                        if (menuItem.getTitle().equals("Editar")){
                            HorarioActualizarActivity actHor = HorarioActualizarActivity.newInstance(hor.getId_horario(), itemPosicion, new IHorario.ActualizarHorarioListener(){
                                @Override
                                public void onActualizarHorario(Horario horario, int position) {
                                    horarioLista.set(position,horario);
                                    notifyDataSetChanged();
                                }
                            });
                            actHor.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
                        }else if (menuItem.getTitle().equals("Eliminar")){
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

    private void eliminarHorario(int position){
        Horario horario = horarioLista.get(position);
        long count = operacionesHorario.eliminarHor(horario.getId_horario());
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
        TextView Dia, Aula;
        ImageView opcionesHor;
        HorarioViewHolder (View view){
            super(view);
            Aula = view.findViewById(R.id.AulaTV);
            Dia = view.findViewById(R.id.HorDia);
            opcionesHor= view.findViewById(R.id.opcionesHor);
        }
    }
}
