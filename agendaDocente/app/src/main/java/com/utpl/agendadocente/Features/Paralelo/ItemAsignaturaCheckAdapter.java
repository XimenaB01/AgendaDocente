package com.utpl.agendadocente.Features.Paralelo;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAsignaturaCheckAdapter extends RecyclerView.Adapter<ItemAsignaturaCheckAdapter.ItemAsignaturaViewHolder> {
    @NonNull
    @Override
    public ItemAsignaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAsignaturaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ItemAsignaturaViewHolder extends RecyclerView.ViewHolder {
        public ItemAsignaturaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
