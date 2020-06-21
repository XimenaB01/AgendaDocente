package com.utpl.agendadocente.Features.Tarea.PresentarTarea;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

public class TareaDetalle extends DialogFragment {

    private static long idTarea;

    public TareaDetalle(){}

    public static TareaDetalle newInstance(int id){
        idTarea = id;
        TareaDetalle tareaDetalle = new TareaDetalle();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Tarea");
        tareaDetalle.setArguments(bundle);
        return tareaDetalle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_detalle_tarea, container, false);

        OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDT);
        TextView nombreTarea = view.findViewById(R.id.nomTarDet);
        TextView fechaTarea = view.findViewById(R.id.fechTarDet);
        TextView descripcionTarea = view.findViewById(R.id.DescTarDet);
        TextView observacionTarea = view.findViewById(R.id.ObsTarDet);

        Tarea tarea = operacionesTarea.obtenerTar(idTarea);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        if (tarea != null){
            nombreTarea.setText(tarea.getNombreTarea());
            fechaTarea.setText(tarea.getFechaTarea());
            descripcionTarea.setText(tarea.getDescripcionTarea());
            if (!tarea.getObservacionTarea().isEmpty()){
                observacionTarea.setText(tarea.getObservacionTarea());
            }else {
                String mensaje = "No agrego Observaciones";
                observacionTarea.setText(mensaje);
            }

        }else {
            Toast.makeText(getContext(),"No se encontro la Tarea",Toast.LENGTH_LONG).show();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
}
