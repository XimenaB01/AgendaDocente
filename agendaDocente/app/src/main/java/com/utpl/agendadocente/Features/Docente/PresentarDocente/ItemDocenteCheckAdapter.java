package com.utpl.agendadocente.Features.Docente.PresentarDocente;

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

public class ItemDocenteCheckAdapter extends RecyclerView.Adapter<ItemDocenteCheckAdapter.ItemDocenteViewHolder> {

    private Context context;
    private List<Boolean> listEstados = new ArrayList<>();
    private List<String> itemsListNew = new ArrayList<>();
    private List<String> ListaItemMultiCkecks = new ArrayList<>();

    public ItemDocenteCheckAdapter(){}

    public ItemDocenteCheckAdapter(Context context,  List<String> listaItemMultiCkecks, List<Boolean> estados){
        this.context = context;
        this.listEstados = estados;
        this.ListaItemMultiCkecks = listaItemMultiCkecks;
    }

    @NonNull
    @Override
    public ItemDocenteCheckAdapter.ItemDocenteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check_docente,parent,false);
        return new ItemDocenteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemDocenteCheckAdapter.ItemDocenteViewHolder holder, final int position) {

        holder.docenteItem.setText(ListaItemMultiCkecks.get(position));

        holder.checkDocente.setChecked(listEstados.get(position));
        if (holder.checkDocente.isChecked()){
            itemsListNew.add(ListaItemMultiCkecks.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkDocente.isChecked()){
                    holder.checkDocente.setChecked(false);
                    itemsListNew.remove(ListaItemMultiCkecks.get(position));
                }else {
                    holder.checkDocente.setChecked(true);
                    itemsListNew.add(ListaItemMultiCkecks.get(position));
                }
            }
        });

        holder.checkDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkDocente.isChecked()){
                    itemsListNew.add(ListaItemMultiCkecks.get(position));
                }else {
                    itemsListNew.remove(ListaItemMultiCkecks.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListaItemMultiCkecks.size();
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
