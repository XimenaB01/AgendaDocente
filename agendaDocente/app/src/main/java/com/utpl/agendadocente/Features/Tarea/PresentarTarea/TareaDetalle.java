package com.utpl.agendadocente.Features.Tarea.PresentarTarea;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Entidades.TareaAsignada;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.List;

public class TareaDetalle extends DialogFragment {

    private static long idTarea;
    private String NombreTarea = "";
    private StringBuilder Asignaturas = new StringBuilder();
    private StringBuilder Paralelos = new StringBuilder();
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private List<TareaAsignada> tareaAsignada = operacionesTarea.ListarTareasPorParalelo(idTarea);
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

        Toolbar toolbar = view.findViewById(R.id.toolbarDT);
        TextView nombreTarea = view.findViewById(R.id.nomTarDet);
        TextView fechaTarea = view.findViewById(R.id.fechTarDet);
        TextView descripcionTarea = view.findViewById(R.id.DescTarDet);
        TextView observacionTarea = view.findViewById(R.id.ObsTarDet);
        TextView estadoTarea = view.findViewById(R.id.estadoTarea);
        TextView paraleloTarea = view.findViewById(R.id.parTar);
        TextView asignarutaTarea = view.findViewById(R.id.asigTar);

        Tarea tarea = operacionesTarea.obtenerTar(idTarea);

        listatareas();

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        if (tarea != null){
            nombreTarea.setText(NombreTarea);
            fechaTarea.setText(tarea.getFechaTarea());
            descripcionTarea.setText(tarea.getDescripcionTarea());
            if (!tarea.getObservacionTarea().isEmpty()){
                observacionTarea.setText(tarea.getObservacionTarea());
            }else {
                String mensaje = "No agrego Observaciones";
                observacionTarea.setText(mensaje);
            }

            estadoTarea.setText(tarea.getEstadoTarea());
            paraleloTarea.setText(Paralelos);
            asignarutaTarea.setText(Asignaturas);

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

    private void listatareas(){
        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

        for (int i = 0; i < tareaAsignada.size(); i++){
            if (!NombreTarea.contains(tareaAsignada.get(i).getNombreTarea())){
                NombreTarea = tareaAsignada.get(i).getNombreTarea();
            }
            if (tareaAsignada.get(i).getNomPar() != null){
                if (!Paralelos.toString().contains(tareaAsignada.get(i).getNomPar())){
                    Paralelos.append(tareaAsignada.get(i).getNomPar());
                    if (i < tareaAsignada.size() -1){
                        Paralelos.append(" - ");
                    }
                }
            }else {
                Paralelos.append("--");
            }

            if (tareaAsignada.get(i).getId_asig() != 0){
                Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(tareaAsignada.get(i).getId_asig());
                Asignaturas.append(asignatura.getNombreAsignatura());
                if (i < tareaAsignada.size() -1){
                    Asignaturas.append(" - ");
                }
            }else {
                Asignaturas.append("Sin Asignar");
            }
        }
    }
}
