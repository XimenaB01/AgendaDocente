package com.utpl.agendadocente.ui.evaluacion.crear_evaluacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

import java.util.List;

public class ParaleloAsigadoAdapter extends RecyclerView.Adapter<ParaleloAsigadoAdapter.ParaleloAsignadoViewHolder> {

    private Context context;
    private List<String> listaParalelosAsignados;

    public ParaleloAsigadoAdapter(){}

    public ParaleloAsigadoAdapter(Context context, List<String> lista){
        this.context = context;
        this.listaParalelosAsignados = lista;
    }

    @NonNull
    @Override
    public ParaleloAsigadoAdapter.ParaleloAsignadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paralelo_asignado,parent,false);
        return new ParaleloAsignadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParaleloAsigadoAdapter.ParaleloAsignadoViewHolder holder, int position) {
        final int itemPosicion = position;

        holder.nomPA.setText(listaParalelosAsignados.get(position));
        holder.eliminarPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarItem(itemPosicion);
            }
        });
    }

    private void eliminarItem(int posicion){
        listaParalelosAsignados.remove(posicion);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaParalelosAsignados.size();
    }

    public class ParaleloAsignadoViewHolder extends RecyclerView.ViewHolder{
        TextView nomPA;
        ImageView eliminarPA;
        public ParaleloAsignadoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomPA = itemView.findViewById(R.id.NomParAsig);
            eliminarPA = itemView.findViewById(R.id.eliminarPA);
        }
    }
}
