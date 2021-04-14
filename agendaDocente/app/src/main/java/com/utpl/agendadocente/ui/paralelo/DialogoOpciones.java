package com.utpl.agendadocente.ui.paralelo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.ui.paralelo.actualizar_paralelo.ParaleloActualizarActivity;
import com.utpl.agendadocente.ui.paralelo.presentar_paralelo.DetalleParaleloActivity;
import com.utpl.agendadocente.ui.paralelo.presentar_paralelo.DialogoOpcionesListener;
import com.utpl.agendadocente.ui.paralelo.replicar_paralelo.ReplicarActivity;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;

import java.util.List;

public class DialogoOpciones extends BottomSheetDialogFragment implements IParalelo.ReplicarParaleloListener{

    private static long idParalelo;
    private static int paraleloItemPosition;
    private static DialogoOpcionesListener dialogoOpcionesListener;
    private static List<Paralelo> paraleloList;

    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    public DialogoOpciones(){
        //required constructor
    }

    public static DialogoOpciones newInstance(Integer id, int posicion, List<Paralelo> list, DialogoOpcionesListener listener){
        idParalelo = id;
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
                Paralelo paralelo = operacionesParalelo.obtenerParalelo(idParalelo);
                ParaleloActualizarActivity paraleloActualizarActivity = ParaleloActualizarActivity.newInstance(paralelo, paraleloItemPosition, new IParalelo.ActualizarParaleloListener() {
                    @Override
                    public void onActualizarParalelo(Paralelo paralelo, int position) {
                        dialogoOpcionesListener.onDialogoOpcionesParalelo(paralelo,position,"Editar");
                    }
                });
                paraleloActualizarActivity.setCancelable(false);
                paraleloActualizarActivity.show(((MainActivity) requireContext()).getSupportFragmentManager(), Utilidades.ACTUALIZAR);
            }
        });

        replicarLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = (int) idParalelo;
                ReplicarActivity replicarActivity = ReplicarActivity.newInstance(id);
                replicarActivity.setCancelable(false);
                replicarActivity.setTargetFragment(DialogoOpciones.this,22);
                replicarActivity.show(getParentFragmentManager(), Utilidades.REPLICAR);
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
        long count = operacionesParalelo.eliminarParalelo(idParalelo);
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

