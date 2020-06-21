package com.utpl.agendadocente.Features.Asignatura.PresentarAsignatura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Features.Asignatura.ActualizarAsignatura.ActualizarAsignaturaListener;
import com.utpl.agendadocente.Features.Asignatura.ActualizarAsignatura.AsignaturaActualizarActivity;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class AsignaturaListaRecycleViewAdapter extends RecyclerView.Adapter<AsignaturaViewHolder> {
    private Context context;
    private List<Asignatura> asignaturaLista;
    private OperacionesAsignatura operacionesAsignatura;

    public AsignaturaListaRecycleViewAdapter(){}

    public AsignaturaListaRecycleViewAdapter(Context context, List<Asignatura> asignaturaLista){
        this.context = context;
        this.asignaturaLista = asignaturaLista;
        operacionesAsignatura = new OperacionesAsignatura(context);
    }

    @NonNull
    @Override
    public AsignaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_asignatura,parent,false);
        return new AsignaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AsignaturaViewHolder holder, int position) {
        final int itemPosicion = position;
        final Asignatura asignatura = asignaturaLista.get(position);

        holder.nombreA.setText(asignatura.getNombreAsignatura());
        holder.creditosA.setText(asignatura.getCreditos());
        holder.horasA.setText(asignatura.getHorario());
        holder.carreraA.setText(asignatura.getCarrera());

        holder.eliminarAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar a esta Asignatura?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarAsignatura(itemPosicion);
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

        holder.editarAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsignaturaActualizarActivity actAsig = AsignaturaActualizarActivity.newInstance(asignatura.getId_asignatura(), itemPosicion, new ActualizarAsignaturaListener(){

                   @Override
                    public void onActualizarAsignatura(Asignatura asig, int position) {
                        asignaturaLista.set(position,asig);
                        notifyDataSetChanged();
                    }
                });
                actAsig.show(((MainActivity) context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleAsignatura detalleAsignatura = DetalleAsignatura.newInstance(asignatura.getId_asignatura());
                detalleAsignatura.setCancelable(false);
                detalleAsignatura.show(((MainActivity)context).getSupportFragmentManager(),"Tag");
            }
        });
    }

    private void eliminarAsignatura(int position){
        Asignatura asig = asignaturaLista.get(position);
        long count = operacionesAsignatura.eliminarAsig(asig.getId_asignatura());
        if (count>0){
            asignaturaLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "DocenteParger eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "DocenteParger no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return asignaturaLista.size();
    }
}
