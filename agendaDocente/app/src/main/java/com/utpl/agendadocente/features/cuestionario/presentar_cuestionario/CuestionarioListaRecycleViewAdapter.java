package com.utpl.agendadocente.features.cuestionario.presentar_cuestionario;

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

import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.util.MainActivity;
import com.utpl.agendadocente.features.cuestionario.actualizar_cuestionario.CuestionarioActualizarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.cuestionario.ICuestionario;

import java.util.List;

public class CuestionarioListaRecycleViewAdapter extends RecyclerView.Adapter<CuestionarioViewHolder> {

    private Context context;
    private List<Cuestionario> cuestionarioLista;
    private OperacionesCuestionario operacionesCuestionario;

    public CuestionarioListaRecycleViewAdapter(){}

    public CuestionarioListaRecycleViewAdapter (Context context, List<Cuestionario> cuestionarioLista){
        this.context = context;
        this.cuestionarioLista = cuestionarioLista;
        operacionesCuestionario = (OperacionesCuestionario) OperacionesFactory.getOperacionCuestionario(context);
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

        holder.opcionesCues.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                PopupMenu popupMenuEstados = new PopupMenu(context,holder.opcionesCues);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados2,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Editar")){
                            CuestionarioActualizarActivity actCuest = CuestionarioActualizarActivity.newInstance(cuest.getIdCuestionario(), itemPosicion, new ICuestionario.ActualizarCuestionarioListener(){
                                @Override
                                public void onActualizarCuestionario(Cuestionario cuestionario, int position) {
                                    cuestionarioLista.set(position,cuestionario);
                                    notifyDataSetChanged();
                                }
                            });
                            actCuest.show(((MainActivity) context).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
                        }else if (menuItem.getTitle().equals("Eliminar")){
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
                        return true;
                    }
                });

                popupMenuEstados.show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CuestionarioDetalle cuestionarioDetalle = CuestionarioDetalle.newInstance(cuest.getIdCuestionario());
                cuestionarioDetalle.setCancelable(false);
                cuestionarioDetalle.show(((MainActivity) context).getSupportFragmentManager(),"tag");
            }
        });
    }

    private void eliminarCuestioniario(int position){
        Cuestionario cuestionario = cuestionarioLista.get(position);
        long count = operacionesCuestionario.eliminarCuestionario(cuestionario.getIdCuestionario());
        if (count > 0){
            cuestionarioLista.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Cuestionario eliminado exitosamente", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Cuestionario no eliminado. ¡Algo salio mal!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return cuestionarioLista.size();
    }

}
