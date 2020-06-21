package com.utpl.agendadocente.Features.Paralelo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DialogMultiCheck extends DialogFragment {
    private static String Componente;
    private static String [] ItemList;
    private static boolean [] EstadosList;

    public static DialogMultiCheck newInstance(String [] itemList, boolean [] estadosList, String componente){
        ItemList = itemList;
        EstadosList = estadosList;
        Componente = componente;
        return new DialogMultiCheck();
    }

    public interface DialogMultiCheckListener{
        void resutadoDialogMultiCheck(StringBuilder item, List<String> listaItems ,String componente, boolean [] est);
    }

    private DialogMultiCheckListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogMultiCheckListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(" onMultiChoiceListener must implemented");

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final List<String> listItemsSelected = new ArrayList<>();
        String title = "Seleccionar "+Componente;

        for (int y = 0; y < EstadosList.length; y++){
            if (EstadosList[y]){
                listItemsSelected.add(ItemList[y]);
            }
        }

        builder.setTitle(title)
                .setMultiChoiceItems(ItemList, EstadosList, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked){
                            listItemsSelected.add(ItemList[position]);
                        }else {
                            listItemsSelected.remove(ItemList[position]);
                        }
                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder listaItems = new StringBuilder();
                        if (listItemsSelected.size() != 0){
                            for (int x = 0; x < listItemsSelected.size(); x++){
                                listaItems.append(listItemsSelected.get(x));
                                if (x != listItemsSelected.size()-1){
                                    listaItems.append("\n");
                                }
                            }
                        }else {
                            listaItems.append(String.format("Agregar %s",Componente));
                        }

                        listener.resutadoDialogMultiCheck(listaItems,listItemsSelected, Componente, EstadosList);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }
}