package com.utpl.agendadocente.ui.paralelo.PresentarParalelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.ui.paralelo.DialogoOpciones;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class ParaleloListaRecycleViewAdapter extends RecyclerView.Adapter<ParaleloListaRecycleViewAdapter.ParaleloViewHolder> {
    private Context context;
    private List<Paralelo> listaParalelo;
    private OperacionesAsignatura operacionesAsignatura= new OperacionesAsignatura(context);

    public ParaleloListaRecycleViewAdapter(){}

    public ParaleloListaRecycleViewAdapter(Context context, List<Paralelo> listaParalelo){
        this.context = context;
        this.listaParalelo = listaParalelo;
    }

    @NonNull
    @Override
    public ParaleloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paralelo, parent, false);
        return new ParaleloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ParaleloViewHolder holder, final int position) {
        final Paralelo paralelo = listaParalelo.get(position);
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());

        holder.paralelo.setText(paralelo.getNombreParalelo());
        holder.numAlumnos.setText(String.valueOf(paralelo.getNum_estudiantes()));
        holder.asignatura.setText(asignatura.getNombreAsignatura());

        holder.opcionesPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoOpciones dialogoOpciones = DialogoOpciones.newInstance(paralelo, position, listaParalelo, new DialogoOpcionesListener(){
                    @Override
                    public void onDialogoOpcionesParalelo(Paralelo paralelo, int position, String option) {
                        switch (option){
                            case "Replicar":
                                listaParalelo.add(paralelo);
                                break;
                            case "Editar":
                                listaParalelo.set(position,paralelo);
                        }
                        notifyDataSetChanged();
                    }
                });
                dialogoOpciones.show(((MainActivity)context).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaParalelo.size();
    }

    public class ParaleloViewHolder extends RecyclerView.ViewHolder{
        TextView paralelo, numAlumnos, asignatura;
        ImageView opcionesPar;

        public ParaleloViewHolder(View itemView){
            super(itemView);
            paralelo = itemView.findViewById(R.id.textParalelo);
            numAlumnos = itemView.findViewById(R.id.textNumAlumnos);
            asignatura = itemView.findViewById(R.id.textAsignatura);
            opcionesPar = itemView.findViewById(R.id.opcionesPar);
        }
    }

}
