package com.utpl.agendadocente.ui.asignatura.presentar_asignatura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.ui.asignatura.actualizar_asignatura.AsignaturaActualizarActivity;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.asignatura.IAsignatura;

import java.util.List;

public class AsignaturaListaRecycleViewAdapter extends RecyclerView.Adapter<AsignaturaViewHolder> {
    private Context context;
    private List<Asignatura> asignaturaLista;
    private OperacionesAsignatura operacionesAsignatura;

    public AsignaturaListaRecycleViewAdapter(){
        //Ignore method
    }

    public AsignaturaListaRecycleViewAdapter(Context context, List<Asignatura> asignaturaLista){
        this.context = context;
        this.asignaturaLista = asignaturaLista;
        operacionesAsignatura = (OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(context);
    }

    @NonNull
    @Override
    public AsignaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_asignatura,parent,false);
        return new AsignaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AsignaturaViewHolder holder, int position) {
        final int itemPosicion = position;
        final Asignatura asignatura = asignaturaLista.get(position);

        holder.nombreA.setText(asignatura.getNombreAsignatura());
        holder.creditosA.setText(asignatura.getCreditos());
        holder.horasA.setText(asignatura.getHorario());
        holder.carreraA.setText(asignatura.getCarrera());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalleAsignatura detalleAsignatura = DetalleAsignatura.newInstance(asignatura.getIdAsignatura());
                detalleAsignatura.setCancelable(false);
                detalleAsignatura.show(((MainActivity)context).getSupportFragmentManager(),"Tag");
            }
        });

        holder.opcionesAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesAsig);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            AsignaturaActualizarActivity actAsig = AsignaturaActualizarActivity.newInstance(asignatura.getIdAsignatura(), itemPosicion, new IAsignatura.ActualizarAsignaturaListener(){

                                @Override
                                public void onActualizarAsignatura(Asignatura asig, int position) {
                                    asignaturaLista.set(position,asig);
                                    notifyDataSetChanged();
                                }
                            });
                            actAsig.show(((MainActivity) context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                        }else if (menuItem.getTitle().equals("Eliminar")){
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setMessage("¿Quiere eliminar a esta Asignatura?");
                            dialogBuilder.setPositiveButton("Eliminar",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            eliminarAsignatura(itemPosicion);
                                        }
                                    }
                            );
                            dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.show();
                        }
                        return true;
                    }
                });

                popupMenuEstados.show();
            }
        });
    }

    private void eliminarAsignatura(int position){
        Asignatura asig = asignaturaLista.get(position);
        long count = operacionesAsignatura.eliminarAsignatura(asig.getIdAsignatura());
        if (count>0){
            asignaturaLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Asignatura eliminada exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Asignatura no eliminada. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return asignaturaLista.size();
    }
}
