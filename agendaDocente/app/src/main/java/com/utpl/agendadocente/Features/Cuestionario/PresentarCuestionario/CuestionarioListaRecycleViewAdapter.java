package com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Features.Cuestionario.ActualizarCuestionario.ActualizarCuestionarioListener;
import com.utpl.agendadocente.Features.Cuestionario.ActualizarCuestionario.CuestionarioActualizarActivity;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class CuestionarioListaRecycleViewAdapter extends RecyclerView.Adapter<CuestionarioListaRecycleViewAdapter.CuestionarioViewHolder> {

    private Context context;
    private List<Cuestionario> cuestionarioLista;
    private OperacionesCuestionario operacionesCuestionario;

    public CuestionarioListaRecycleViewAdapter(){}

    public CuestionarioListaRecycleViewAdapter (Context context, List<Cuestionario> cuestionarioLista){
        this.context = context;
        this.cuestionarioLista = cuestionarioLista;
        operacionesCuestionario = new OperacionesCuestionario(context);
    }

    @NonNull
    @Override
    public CuestionarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cuestionario,parent,false);
        return new CuestionarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CuestionarioViewHolder holder, int position) {
        final int itemPosicion = position;
        final Cuestionario cuest = cuestionarioLista.get(position);

        holder.nombCuest.setText(cuest.getNombreCuestionario());

        holder.eliminarCuest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esté Cuestioniario ?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarCuestioniario(itemPosicion);
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
        holder.editarCuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CuestionarioActualizarActivity actCuest = CuestionarioActualizarActivity.newInstance(cuest.getId_cuestionario(), itemPosicion, new ActualizarCuestionarioListener(){
                    @Override
                    public void onActualizarCuestionario(Cuestionario cuestionario, int position) {
                        cuestionarioLista.set(position,cuestionario);
                        notifyDataSetChanged();
                    }
                });
                actCuest.show(((CuestionarioListarActivity) context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CuestionarioDetalle cuestionarioDetalle = CuestionarioDetalle.newInstance(cuest.getId_cuestionario());
                cuestionarioDetalle.setCancelable(false);
                cuestionarioDetalle.show(((CuestionarioListarActivity)context).getSupportFragmentManager(),"tag");
            }
        });
    }

    private void eliminarCuestioniario(int position){
        Cuestionario cuestionario = cuestionarioLista.get(position);
        long count = operacionesCuestionario.eliminarCues(cuestionario.getId_cuestionario());
        if (count > 0){
            cuestionarioLista.remove(position);
            notifyDataSetChanged();
            ((CuestionarioListarActivity) context).viewVisibility();
            Toast.makeText(context, "Cuestionario eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Cuestionario no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return cuestionarioLista.size();
    }

    public class CuestionarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombCuest;
        ImageView eliminarCuest, editarCuest;
        CuestionarioViewHolder (View view){
            super(view);
            nombCuest = view.findViewById(R.id.txttitleCuestionario);
            editarCuest = view.findViewById(R.id.editarCuest);
            eliminarCuest = view.findViewById(R.id.eliminarCues);
        }
    }

}
