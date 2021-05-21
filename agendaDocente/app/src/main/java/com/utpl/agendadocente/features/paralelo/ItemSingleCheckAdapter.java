package com.utpl.agendadocente.features.paralelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.R;

import java.util.List;

public class ItemSingleCheckAdapter extends RecyclerView.Adapter<ItemSingleCheckAdapter.ItemAsignaturaViewHolder> {

    private Context context;
    private List<String> listItemSingleCheck;
    private int lastSelectedPosition = -1;
    private int posicion;

    public ItemSingleCheckAdapter (Context context, List<String> listaItemSingleCkecks, int posicion){
        this.context = context;
        this.listItemSingleCheck = listaItemSingleCkecks;
        this.posicion = posicion;
    }

    public ItemSingleCheckAdapter(){}

    @NonNull
    @Override
    public ItemAsignaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_radio_button,parent,false);
        return new ItemAsignaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAsignaturaViewHolder holder, final int position) {

        holder.radioButtonCkeck.setText(listItemSingleCheck.get(position));
        if (posicion == position){
            holder.radioButtonCkeck.setChecked(true);
            lastSelectedPosition = posicion;
        }else {
            holder.radioButtonCkeck.setChecked(position == lastSelectedPosition);
        }

        holder.radioButtonCkeck.setTag(position);
        holder.radioButtonCkeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = (Integer) view.getTag();
                posicion = -1;
                notifyDataSetChanged();
            }
        });
    }

    public String getSelectedItem(){
        if (lastSelectedPosition != -1){
            return listItemSingleCheck.get(lastSelectedPosition);
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return listItemSingleCheck.size();
    }

    public class ItemAsignaturaViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButtonCkeck;

        public ItemAsignaturaViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButtonCkeck = itemView.findViewById(R.id.radioButtonCkeck);

        }
    }
}
