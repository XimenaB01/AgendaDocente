package com.utpl.agendadocente.Features.Paralelo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemHorarioCheckAdapter extends RecyclerView.Adapter<ItemHorarioCheckAdapter.ItemHorarioViewHolder> {
    @NonNull
    @Override
    public ItemHorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHorarioViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemHorarioViewHolder extends RecyclerView.ViewHolder {
        public ItemHorarioViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
