package com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion;

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

import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.evaluacion.ActualizarEvaluacion.ActualizarEvaluacionListener;
import com.utpl.agendadocente.ui.evaluacion.ActualizarEvaluacion.EvaluacionActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class EvaluacionListaRecycleViewAdapter extends RecyclerView.Adapter<EvaluacionListaRecycleViewAdapter.EvaluacionViewHolder> {

    private Context context;
    private List<Evaluacion> evaluacionLista;
    private OperacionesEvaluacion operacionesEvaluacion;

    public EvaluacionListaRecycleViewAdapter(){}

    EvaluacionListaRecycleViewAdapter(Context context, List<Evaluacion> evaluacionLista){
        this.context = context;
        this.evaluacionLista = evaluacionLista;
        operacionesEvaluacion = new OperacionesEvaluacion(context);
    }

    @NonNull
    @Override
    public EvaluacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_evaluacion,parent,false);
        return new EvaluacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EvaluacionViewHolder holder, int position) {
        final int itemPosicion = position;
        final Evaluacion eva = evaluacionLista.get(position);

        holder.nombEva.setText(eva.getNombreEvaluacion());
        holder.tipoEva.setText(eva.getTipo());
        holder.fechEva.setText(eva.getFechaEvaluacion());

        holder.eliminarEva.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esté Evaluación ?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarEvaluacion(itemPosicion);
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

        holder.editarEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EvaluacionActualizarActivity actEva = EvaluacionActualizarActivity.newInstance(eva.getId_evaluacion(), itemPosicion, new ActualizarEvaluacionListener(){
                    @Override
                    public void onActualizarEvaluacion(Evaluacion evaluacion, int position) {
                        evaluacionLista.set(position,evaluacion);
                        notifyDataSetChanged();
                    }
                });
                actEva.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EvaluacionDetalle evaluacionDetalle = EvaluacionDetalle.newInstance(eva.getId_evaluacion());
                evaluacionDetalle.setCancelable(false);
                evaluacionDetalle.show(((MainActivity)context).getSupportFragmentManager(),"tag");
            }
        });
    }

    private void eliminarEvaluacion(int position){
        Evaluacion evaluacion = evaluacionLista.get(position);
        long count = operacionesEvaluacion.eliminarEva(evaluacion.getId_evaluacion());
        if (count > 0){
            evaluacionLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Evaluación eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Evaluación no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return evaluacionLista.size();
    }

    public class EvaluacionViewHolder extends RecyclerView.ViewHolder {
        TextView fechEva, tipoEva, nombEva;
        ImageView eliminarEva, editarEva;
        EvaluacionViewHolder (View view){
            super(view);
            nombEva = view.findViewById(R.id.nomEvaTV);
            tipoEva = view.findViewById(R.id.TipoEvaTV);
            fechEva = view.findViewById(R.id.FechEvaTV);
            editarEva = view.findViewById(R.id.editarEva);
            eliminarEva = view.findViewById(R.id.eliminarEva);
        }
    }

}
