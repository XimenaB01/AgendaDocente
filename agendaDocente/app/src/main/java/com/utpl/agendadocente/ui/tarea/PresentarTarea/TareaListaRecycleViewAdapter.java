package com.utpl.agendadocente.ui.tarea.PresentarTarea;

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

import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.ui.paralelo.PresentarParalelo.DetalleParaleloActivity;
import com.utpl.agendadocente.ui.tarea.ActualizarTarea.ActualizarTareaListener;
import com.utpl.agendadocente.ui.tarea.ActualizarTarea.TareaActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class TareaListaRecycleViewAdapter extends RecyclerView.Adapter<TareaListaRecycleViewAdapter.TareaViewHolder> {

    private Context context;
    private String Componente;
    private List<Tarea> tareaLista;
    private OperacionesTarea operacionesTarea;


    public TareaListaRecycleViewAdapter(){}

    public TareaListaRecycleViewAdapter(Context context, List<Tarea> tareaLista, String componente){
        this.context = context;
        this.tareaLista = tareaLista;
        this.Componente = componente;
        operacionesTarea = new OperacionesTarea(context);
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea,parent,false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TareaViewHolder holder, int position) {
        final int itemPosicion = position;
        final Tarea tarea = tareaLista.get(position);

        holder.nombreTar.setText(tarea.getNombreTarea());
        holder.fechaTar.setText(tarea.getFechaTarea());
        holder.estadoTar.setText(tarea.getEstadoTarea());

        holder.opcionesTar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesTar);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            TareaActualizarActivity actTar = TareaActualizarActivity.newInstance(tarea.getId_tarea(), itemPosicion, new ActualizarTareaListener(){
                                @Override
                                public void onActualizarTarea(Tarea tarea1, int position) {
                                    tareaLista.set(position,tarea1);
                                    notifyDataSetChanged();
                                }
                            });
                            if (Componente.equals("Fragment")){
                                actTar.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
                            }else if (Componente.equals("Activity")){
                                actTar.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
                            }
                        }else if (menuItem.getTitle().equals("Eliminar")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esta Tarea?");
                            alertDialogBuilder.setPositiveButton("Eliminar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            eliminarTarea(itemPosicion);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaDetalle tareaDetalle = TareaDetalle.newInstance(tarea.getId_tarea());
                tareaDetalle.setCancelable(false);
                if (Componente.equals("Fragment")) {
                    tareaDetalle.show(((MainActivity) context).getSupportFragmentManager(), "tag");
                }else if (Componente.equals("Activity")){
                    tareaDetalle.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), "tag");
                }
            }
        });
    }

    private void eliminarTarea(int position){
        Tarea tarea = tareaLista.get(position);
        long count = operacionesTarea.eliminarTar(tarea.getId_tarea());
        if (count > 0){
            tareaLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Periodo eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Periodo no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
            return tareaLista.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTar , fechaTar, estadoTar;
        ImageView opcionesTar;
        TareaViewHolder (View view){
            super(view);
            nombreTar = view.findViewById(R.id.nomTarTV);
            fechaTar = view.findViewById(R.id.fechTarTV);
            estadoTar = view.findViewById(R.id.estadoTar);
            opcionesTar= view.findViewById(R.id.opcionesTar);
        }
    }


}
