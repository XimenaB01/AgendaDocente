package com.utpl.agendadocente.ui.horario.PresentarHorario;

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

import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.horario.ActualizarHorario.ActualizarHorarioListener;
import com.utpl.agendadocente.ui.horario.ActualizarHorario.HorarioActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

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
        holder.HoraEntrada.setText(hor.getHora_entrada());
        holder.HoraSalida.setText(hor.getHora_salida());

        holder.eliminarHor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esté Horario ?");
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
        });
        holder.editarHor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HorarioActualizarActivity actHor = HorarioActualizarActivity.newInstance(hor.getId_horario(), itemPosicion, new ActualizarHorarioListener(){
                    @Override
                    public void onActualizarHorario(Horario horario, int position) {
                        horarioLista.set(position,horario);
                        notifyDataSetChanged();
                    }
                });
                actHor.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
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
        TextView HoraEntrada , HoraSalida, Aula;
        ImageView eliminarHor, editarHor;
        HorarioViewHolder (View view){
            super(view);
            Aula = view.findViewById(R.id.AulaTV);
            HoraEntrada = view.findViewById(R.id.HorEnTV);
            HoraSalida = view.findViewById(R.id.HorSalTV);
            editarHor= view.findViewById(R.id.editarHor);
            eliminarHor = view.findViewById(R.id.eliminarHor);
        }
    }
}
