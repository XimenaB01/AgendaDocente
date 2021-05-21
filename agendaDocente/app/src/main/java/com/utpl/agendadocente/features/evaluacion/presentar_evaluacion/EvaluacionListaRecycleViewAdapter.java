package com.utpl.agendadocente.features.evaluacion.presentar_evaluacion;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.util.MainActivity;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.features.evaluacion.actualizar_evaluacion.EvaluacionActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.evaluacion.IEvaluacion;
import com.utpl.agendadocente.features.paralelo.presentar_paralelo.DetalleParaleloActivity;

import java.util.List;

public class EvaluacionListaRecycleViewAdapter extends RecyclerView.Adapter<EvaluacionViewHolder> {

    private Context context;
    private String componente;
    private List<Evaluacion> evaluacionLista;
    private OperacionesEvaluacion operacionesEvaluacion;

    public EvaluacionListaRecycleViewAdapter(){}

    EvaluacionListaRecycleViewAdapter(Context context, List<Evaluacion> evaluacionLista, String componente){
        this.context = context;
        this.componente = componente;
        this.evaluacionLista = evaluacionLista;
        operacionesEvaluacion = (OperacionesEvaluacion)OperacionesFactory.getOperacionEvaluacion(context);
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
        holder.nombrePar.setText(getParalelo(eva.getParaleloID()));

        holder.opcionesEva.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesEva);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            EvaluacionActualizarActivity actEva = EvaluacionActualizarActivity.newInstance(eva.getIdEvaluacion(), itemPosicion, new IEvaluacion.ActualizarEvaluacionListener(){
                                @Override
                                public void onActualizarEvaluacion(Evaluacion evaluacion, int position) {
                                    evaluacionLista.set(position,evaluacion);
                                    notifyDataSetChanged();
                                }
                            });

                            if (componente.equals("Fragment")){
                                actEva.show(((MainActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                            }else if (componente.equals("Activity")){
                                actEva.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                            }
                        }else if (menuItem.getTitle().equals("Eliminar")){
                            getDialogForEliminarItemEvaluacion(itemPosicion);
                        }
                        return true;
                    }
                });

                popupMenuEstados.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialogForDetalleItemEvaluacion(eva);
            }
        });
    }

    public String getParalelo(Integer paraleloID) {
        String nombreParalelo;
        if (paraleloID != null && paraleloID != 0){
            OperacionesAsignatura opA =(OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(context);
            OperacionesParalelo opP = (OperacionesParalelo) OperacionesFactory.getOperacionParalelo(context);

            Paralelo paralelo = opP.obtenerParalelo(paraleloID);
            Asignatura asignatura = opA.obtenerAsignatura(paralelo.getAsignaturaID());
            String formato = "%s - %s";
            nombreParalelo = String.format(formato, asignatura.getNombreAsignatura(), paralelo.getNombreParalelo());
        }else {
            nombreParalelo = "Sin Asignar";
        }

        return nombreParalelo;
    }

    private void getDialogForDetalleItemEvaluacion(Evaluacion evaluacion) {
        EvaluacionDetalle evaluacionDetalle = EvaluacionDetalle.newInstance(evaluacion);
        evaluacionDetalle.setCancelable(false);
        if (componente.equals("Fragment")){
            evaluacionDetalle.show(((MainActivity)context).getSupportFragmentManager(),"tag");
        }else if (componente.equals("Activity")){
            evaluacionDetalle.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), "tag");
        }
    }

    private void getDialogForEliminarItemEvaluacion(final int itemPosicion) {
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

    private void eliminarEvaluacion(int position){
        Evaluacion evaluacion = evaluacionLista.get(position);
        long count = operacionesEvaluacion.eliminarEvaluacion(evaluacion.getIdEvaluacion());
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

}
