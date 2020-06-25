package com.utpl.agendadocente.Features.Paralelo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemPeriodoCheckAdapter extends RecyclerView.Adapter<ItemPeriodoCheckAdapter.ItemPeriodoViewHolder> {
    @NonNull
    @Override
    public ItemPeriodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPeriodoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemPeriodoViewHolder extends RecyclerView.ViewHolder {
        public ItemPeriodoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
