package com.utpl.agendadocente.ui.paralelo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.ui.paralelo.ActualizarParalelo.ActualizarParaleloListener;
import com.utpl.agendadocente.ui.paralelo.ActualizarParalelo.ParaleloActualizarActivity;
import com.utpl.agendadocente.ui.paralelo.PresentarParalelo.DetalleParaleloActivity;
import com.utpl.agendadocente.ui.paralelo.PresentarParalelo.DialogoOpcionesListener;
import com.utpl.agendadocente.ui.paralelo.Replicar.ReplicarActivity;
import com.utpl.agendadocente.ui.paralelo.Replicar.ReplicarParaleloListener;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;
import java.util.Objects;

public class DialogoOpciones extends BottomSheetDialogFragment implements ReplicarParaleloListener{

    private static long IdParalelo;
    private static int paraleloItemPosition;
    private static DialogoOpcionesListener dialogoOpcionesListener;
    private static List<Paralelo> paraleloList;

    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    public DialogoOpciones(){}

    public static DialogoOpciones newInstance(Integer Id, int posicion, List<Paralelo> list, DialogoOpcionesListener listener){
        IdParalelo = Id;
        paraleloItemPosition = posicion;
        dialogoOpcionesListener = listener;
        paraleloList = list;
        return new DialogoOpciones();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.menu_opciones,container, false);

        LinearLayout eliminarLL = view.findViewById(R.id.eliminar);
        LinearLayout editarLL = view.findViewById(R.id.editar);
        LinearLayout detalleLL = view.findViewById(R.id.detalle);
        LinearLayout replicarLL = view.findViewById(R.id.replicar);

        editarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Paralelo paralelo = operacionesParalelo.obtenerPar(IdParalelo);
                ParaleloActualizarActivity paraleloActualizarActivity = ParaleloActualizarActivity.newInstance(paralelo, paraleloItemPosition, new ActualizarParaleloListener() {
                    @Override
                    public void onActualizarParalelo(Paralelo paralelo, int position) {
                        dialogoOpcionesListener.onDialogoOpcionesParalelo(paralelo,position,"Editar");
                    }
                });
                paraleloActualizarActivity.setCancelable(false);
                paraleloActualizarActivity.show(((MainActivity) requireContext()).getSupportFragmentManager(), utilidades.ACTUALIZAR);
            }
        });

        replicarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paralelo paralelo = operacionesParalelo.obtenerPar(IdParalelo);
                ReplicarActivity replicarActivity = ReplicarActivity.newInstance(paralelo.getId_paralelo());
                replicarActivity.setCancelable(false);
                replicarActivity.setTargetFragment(DialogoOpciones.this,22);
                replicarActivity.show(getChildFragmentManager(),utilidades.REPLICAR);
                dismiss();
            }
        });

        eliminarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("¿Estás seguro de que querías eliminar esté Paralelo ?");
                alertDialogBuilder.setPositiveButton("Eliminar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarParalelo(paraleloItemPosition);
                            }
                        }
                );
                alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        detalleLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Paralelo paralelo = paraleloList.get(paraleloItemPosition);
                Intent intent = new Intent(getActivity(), DetalleParaleloActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("paralelo",paralelo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private void eliminarParalelo(int position){
        Paralelo paralelo = paraleloList.get(position);
        long count = operacionesParalelo.eliminarPar(IdParalelo);
        if (count > 0){
            paraleloList.remove(position);
            dialogoOpcionesListener.onDialogoOpcionesParalelo(paralelo,position, "Eliminar");
        }
    }

    @Override
    public void onReplicarParalelo(Paralelo paralelo) {
        dialogoOpcionesListener.onDialogoOpcionesParalelo(paralelo,paraleloList.size()+1,"Replicar");
    }
}

