package com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion;

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

public class paraleloAsigadoAdapter extends RecyclerView.Adapter<paraleloAsigadoAdapter.ParaleloAsignadoViewHolder> {

    private Context context;
    private List<String> ListaParalelosAsignados;

    public paraleloAsigadoAdapter(){}

    public paraleloAsigadoAdapter(Context context, List<String> Lista){
        this.context = context;
        this.ListaParalelosAsignados = Lista;
    }

    @NonNull
    @Override
    public paraleloAsigadoAdapter.ParaleloAsignadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paralelo_asignado,parent,false);
        return new ParaleloAsignadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull paraleloAsigadoAdapter.ParaleloAsignadoViewHolder holder, int position) {
        final int itemPosicion = position;

        holder.NomPA.setText(ListaParalelosAsignados.get(position));
        holder.eliminarPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarItem(itemPosicion);
            }
        });
    }

    private void eliminarItem(int posicion){
        ListaParalelosAsignados.remove(posicion);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ListaParalelosAsignados.size();
    }

    public class ParaleloAsignadoViewHolder extends RecyclerView.ViewHolder{
        TextView NomPA;
        ImageView eliminarPA;
        public ParaleloAsignadoViewHolder(@NonNull View itemView) {
            super(itemView);
            NomPA = itemView.findViewById(R.id.NomParAsig);
            eliminarPA = itemView.findViewById(R.id.eliminarPA);
        }
    }
}
