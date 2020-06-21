package com.utpl.agendadocente.Features.Paralelo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class DialogSingleCheck extends DialogFragment {
    private static String [] listaItems;
    private static String Componente;
    private static int position;
    private DialogSingleCheckListener listener;

    public static DialogSingleCheck newInstance(String [] lista, String componente, int pos){
        Componente = componente;
        listaItems = lista;
        position = pos;
        return new DialogSingleCheck();
    }

    public interface DialogSingleCheckListener{
       void resutadoDialogSingle(String item, String componente);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogSingleCheckListener)getTargetFragment();
        }catch (ClassCastException  e){
            throw new ClassCastException(" must implement DialogSingleCheckListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        String title = "Seleccionar "+Componente;
        builder.setTitle(title)
                .setSingleChoiceItems(listaItems, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                    }
                })
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (position != -1) {
                            String item = listaItems[position];
                            if (!item.isEmpty()){
                                listener.resutadoDialogSingle(item, Componente);
                            }else {
                                dialogInterface.dismiss();
                            }
                        }
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
