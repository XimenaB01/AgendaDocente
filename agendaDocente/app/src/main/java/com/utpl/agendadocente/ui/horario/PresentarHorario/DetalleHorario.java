package com.utpl.agendadocente.ui.horario.PresentarHorario;

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

import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

public class DetalleHorario extends DialogFragment {

    private static Horario horario = new Horario();

    public DetalleHorario(){}

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
        TextView Aula = view.findViewById(R.id.horAula);
        TextView Dia = view.findViewById(R.id.horDia);
        TextView HEntrada = view.findViewById(R.id.horEntrada);
        TextView Hsalida = view.findViewById(R.id.horSalida);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        if (horario != null){
            Aula.setText(horario.getAula());
            Dia.setText(horario.getDia());
            HEntrada.setText(horario.getHora_entrada());
            Hsalida.setText(horario.getHora_salida());

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
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
}
