package com.utpl.agendadocente.Features.Paralelo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemEvaluacionCheckAdapter extends RecyclerView.Adapter<ItemEvaluacionCheckAdapter.ItemEvaluacionViewHolder> {
    @NonNull
    @Override
    public ItemEvaluacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemEvaluacionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemEvaluacionViewHolder extends RecyclerView.ViewHolder {
        public ItemEvaluacionViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
