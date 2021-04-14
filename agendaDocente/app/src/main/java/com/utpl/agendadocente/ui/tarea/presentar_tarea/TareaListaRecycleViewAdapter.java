package com.utpl.agendadocente.ui.tarea.presentar_tarea;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.observer.EstadoObserver;
import com.utpl.agendadocente.observer.ImageEstadoObserver;
import com.utpl.agendadocente.observer.SimpleSubject;
import com.utpl.agendadocente.ui.paralelo.presentar_paralelo.DetalleParaleloActivity;
import com.utpl.agendadocente.ui.tarea.actualizar_tarea.TareaActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.List;

public class TareaListaRecycleViewAdapter extends RecyclerView.Adapter<TareaListaRecycleViewAdapter.TareaViewHolder> {

    private Context context;
    private String componente;
    private List<Tarea> tareaLista;
    private OperacionesTarea operacionesTarea;
    private static int mImagen = 0;
    private static int mColor = 0;


    public TareaListaRecycleViewAdapter(){}

    public TareaListaRecycleViewAdapter(Context context, List<Tarea> tareaLista, String componente){
        this.context = context;
        this.tareaLista = tareaLista;
        this.componente = componente;
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
        final SimpleSubject simpleSubject = new SimpleSubject();

        holder.nombreTar.setText(tarea.getNombreTarea());
        holder.fechaTar.setText(tarea.getFechaTarea());
        holder.estadoTar.setText(tarea.getEstadoTarea());

        new ImageEstadoObserver(simpleSubject);
        simpleSubject.setValues(tarea.getIdTarea(), tarea.getEstadoTarea(), new ITarea.ActualizarTareaListener(){
            @Override
            public void onActualizarTarea(Tarea tarea, int position) {
                tareaLista.set(position,tarea);
                notifyDataSetChanged();
            }
        });
        holder.estadoTar.setCompoundDrawablesRelativeWithIntrinsicBounds(mImagen,0,0,0);
        holder.estadoTar.setTextColor(ContextCompat.getColor(context,mColor));

        holder.opcionesTar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesTar);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            TareaActualizarActivity actTar = TareaActualizarActivity.newInstance(tarea.getIdTarea(), itemPosicion, new ITarea.ActualizarTareaListener(){
                                @Override
                                public void onActualizarTarea(Tarea tarea1, int position) {
                                    tareaLista.set(position,tarea1);
                                    notifyDataSetChanged();
                                }
                            });
                            if (componente.equals("Fragment")){
                                actTar.show(((MainActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                            }else if (componente.equals("Activity")){
                                actTar.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
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
                        }else {
                            new EstadoObserver(simpleSubject);
                            new ImageEstadoObserver(simpleSubject);
                            EstadoObserver.position(itemPosicion,context);
                            simpleSubject.setValues(tarea.getIdTarea(), menuItem.getTitle().toString(), new ITarea.ActualizarTareaListener(){
                                @Override
                                public void onActualizarTarea(Tarea tarea, int position) {
                                    tareaLista.set(position,tarea);
                                    notifyDataSetChanged();
                                }
                            });
                            holder.estadoTar.setCompoundDrawablesRelativeWithIntrinsicBounds(mImagen,0,0,0);
                            holder.estadoTar.setTextColor(ContextCompat.getColor(context,mColor));
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
                TareaDetalle tareaDetalle = TareaDetalle.newInstance(tarea.getIdTarea());
                tareaDetalle.setCancelable(false);
                if (componente.equals("Fragment")) {
                    tareaDetalle.show(((MainActivity) context).getSupportFragmentManager(), "tag");
                }else if (componente.equals("Activity")){
                    tareaDetalle.show(((DetalleParaleloActivity)context).getSupportFragmentManager(), "tag");
                }
            }
        });
    }

    private void eliminarTarea(int position){
        Tarea tarea = tareaLista.get(position);
        long count = operacionesTarea.eliminarTarea(tarea.getIdTarea());
        if (count > 0){
            tareaLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Tarea eliminada exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Tarea no eliminada. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
            return tareaLista.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTar;
        TextView fechaTar;
        TextView estadoTar;
        ImageView opcionesTar;
        TareaViewHolder (View view){
            super(view);
            nombreTar = view.findViewById(R.id.nomTarTV);
            fechaTar = view.findViewById(R.id.fechTarTV);
            estadoTar = view.findViewById(R.id.estadoTar);
            opcionesTar= view.findViewById(R.id.opcionesTar);
        }
    }

    public static void imagen(List<Integer> list){
        mImagen = list.get(0);
        mColor = list.get(1);
    }


}
