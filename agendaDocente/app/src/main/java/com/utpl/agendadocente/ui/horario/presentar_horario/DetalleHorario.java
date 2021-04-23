package com.utpl.agendadocente.ui.horario.presentar_horario;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.util.Utilidades;

public class DetalleHorario extends DialogFragment {

    private static Horario horario;

    public DetalleHorario(){
        //required constructor
    }

    public static DetalleHorario newInstance(Horario hor){
        DetalleHorario detalleHorario = new DetalleHorario();
        horario = hor;
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Horario");
        detalleHorario.setArguments(bundle);

        return detalleHorario;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detalle_horario, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarDH);
        TextView aula = view.findViewById(R.id.horAula);
        TextView dia = view.findViewById(R.id.horDia);
        TextView hEntrada = view.findViewById(R.id.horEntrada);
        TextView hSalida = view.findViewById(R.id.horSalida);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        if (horario != null){
            aula.setText(horario.getAula());
            dia.setText(horario.getDia());
            hEntrada.setText(horario.getHoraEntrada());
            hSalida.setText(horario.getHoraSalida());

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }else {
            Toast.makeText(getContext(),"No se encontro el horario",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }
}
