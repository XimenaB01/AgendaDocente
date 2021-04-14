package com.utpl.agendadocente.ui.tarea.presentar_tarea;

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

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;

public class TareaDetalle extends DialogFragment {

    private TextView paraleloTarea;
    private TextView asignarutaTarea;

    private static long idTarea;
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private Tarea tarea = operacionesTarea.obtenerTarea(idTarea);

    public TareaDetalle(){
        //Required constructor
    }

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

        Toolbar toolbar = view.findViewById(R.id.toolbarDT);
        TextView nombreTarea = view.findViewById(R.id.nomTarDet);
        TextView fechaTarea = view.findViewById(R.id.fechTarDet);
        TextView descripcionTarea = view.findViewById(R.id.DescTarDet);
        TextView observacionTarea = view.findViewById(R.id.ObsTarDet);
        TextView estadoTarea = view.findViewById(R.id.estadoTarea);
        paraleloTarea = view.findViewById(R.id.parTar);
        asignarutaTarea = view.findViewById(R.id.asigTar);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
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

            estadoTarea.setText(tarea.getEstadoTarea());
            obtenerParaleloAsignado();

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

    private void obtenerParaleloAsignado() {
        String paraleloNull = "Sin Asignar";

        if (tarea.getParaleloId() != 0) {
            OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
            Paralelo paralelo = operacionesParalelo.obtenerParalelo(tarea.getParaleloId());
            paraleloTarea.setText(paralelo.getNombreParalelo());

            OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
            Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());
            asignarutaTarea.setText(asignatura.getNombreAsignatura());
        }else {
            paraleloTarea.setText(" -- ");
            asignarutaTarea.setText(paraleloNull);
        }

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
