package com.utpl.agendadocente.ui.paralelo;

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
    private List<String> ListItemSingleCheck;
    private int lastSelectedPosition = -1;
    private int Posicion;

    public ItemSingleCheckAdapter (Context context, List<String> listaItemSingleCkecks, int posicion){
        this.context = context;
        this.ListItemSingleCheck = listaItemSingleCkecks;
        this.Posicion = posicion;
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

        holder.RadioButtonCkeck.setText(ListItemSingleCheck.get(position));
        if (Posicion == position){
            holder.RadioButtonCkeck.setChecked(true);
            lastSelectedPosition = Posicion;
        }else {
            holder.RadioButtonCkeck.setChecked(position == lastSelectedPosition);
        }

        holder.RadioButtonCkeck.setTag(position);
        holder.RadioButtonCkeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = (Integer) view.getTag();
                Posicion = -1;
                notifyDataSetChanged();
            }
        });
    }

    public String getSelectedItem(){
        if (lastSelectedPosition != -1){
            return ListItemSingleCheck.get(lastSelectedPosition);
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return ListItemSingleCheck.size();
    }

    public class ItemAsignaturaViewHolder extends RecyclerView.ViewHolder {

        RadioButton RadioButtonCkeck;

        public ItemAsignaturaViewHolder(@NonNull View itemView) {
            super(itemView);
            RadioButtonCkeck = itemView.findViewById(R.id.radioButtonCkeck);

        }
    }
}
