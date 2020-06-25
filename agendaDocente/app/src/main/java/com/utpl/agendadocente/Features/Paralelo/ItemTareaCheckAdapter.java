package com.utpl.agendadocente.Features.Paralelo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTareaCheckAdapter extends RecyclerView.Adapter<ItemTareaCheckAdapter.ItemTareaViewHolder> {
    @NonNull
    @Override
    public ItemTareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTareaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemTareaViewHolder extends RecyclerView.ViewHolder {
        public ItemTareaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
