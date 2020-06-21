package com.utpl.agendadocente.Features.Asignatura.PresentarAsignatura;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleAsignatura extends DialogFragment {

    private static Integer idAsignatura;

    public DetalleAsignatura() {
        // Required empty public constructor
    }

    public static DetalleAsignatura newInstance(int id){
        idAsignatura = id;
        DetalleAsignatura detalleAsignatura = new DetalleAsignatura();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Asignatura");
        detalleAsignatura.setArguments(bundle);

        return detalleAsignatura;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_detalle_asignatura, container, false);

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDA);
        TextView asignatura = view.findViewById(R.id.nombreAsig);
        TextView area = view.findViewById(R.id.areaAsig);
        TextView carrera = view.findViewById(R.id.carreraAsig);
        TextView nivel = view.findViewById(R.id.nivelAsig);
        TextView descripcion = view.findViewById(R.id.descripcionAsig);
        TextView creditos = view.findViewById(R.id.creditosAsig);
        TextView horas = view.findViewById(R.id.horasAsig);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        Asignatura asig = operacionesAsignatura.obtenerAsignatura(idAsignatura);

        if (asig != null){
            asignatura.setText(asig.getNombreAsignatura());
            area.setText(asig.getArea());
            carrera.setText(asig.getCarrera());
            nivel.setText(asig.getNivel());
            descripcion.setText(asig.getDescripcionAsigantura());
            creditos.setText(asig.getCreditos());
            horas.setText(asig.getHorario());

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else {
            Toast.makeText(getContext(),"No se encontro la asignatura",Toast.LENGTH_LONG).show();
        }

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
