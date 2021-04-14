package com.utpl.agendadocente.ui.paralelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;
import java.util.ArrayList;
import java.util.List;

public class ItemMultiCheckAdapter extends RecyclerView.Adapter<ItemMultiCheckAdapter.ItemDocenteViewHolder> {

    private Context context;
    private List<Boolean> listEstados = new ArrayList<>();
    private List<String> itemsListNew = new ArrayList<>();
    private List<String> listaItemMultiCkecks = new ArrayList<>();

    public ItemMultiCheckAdapter(){}

    public ItemMultiCheckAdapter(Context context, List<String> listaItemMultiCkecks, List<Boolean> estados){
        this.context = context;
        this.listEstados = estados;
        this.listaItemMultiCkecks = listaItemMultiCkecks;
    }

    @NonNull
    @Override
    public ItemMultiCheckAdapter.ItemDocenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check_box,parent,false);
        return new ItemDocenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemMultiCheckAdapter.ItemDocenteViewHolder holder, final int position) {

        holder.docenteItem.setText(listaItemMultiCkecks.get(position));

        if (listEstados.get(position)){
            holder.checkDocente.setChecked(true);
            if (holder.checkDocente.isChecked()){
                itemsListNew.add(listaItemMultiCkecks.get(position));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkDocente.isChecked()){
                    holder.checkDocente.setChecked(false);
                    itemsListNew.remove(listaItemMultiCkecks.get(position));
                }else {
                    holder.checkDocente.setChecked(true);
                    itemsListNew.add(listaItemMultiCkecks.get(position));
                }
            }
        });

        holder.checkDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkDocente.isChecked()){
                    itemsListNew.add(listaItemMultiCkecks.get(position));
                }else {
                    itemsListNew.remove(listaItemMultiCkecks.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaItemMultiCkecks.size();
    }

    public class ItemDocenteViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkDocente;
        private TextView docenteItem;
        public ItemDocenteViewHolder(@NonNull View itemView) {
            super(itemView);
            checkDocente = itemView.findViewById(R.id.checkboxDocente);
            docenteItem = itemView.findViewById(R.id.docenteItem);
        }
    }

    public List<String> getSelectedItems(){
        return itemsListNew;
    }
}
