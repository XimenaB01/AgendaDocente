package com.utpl.agendadocente.Features.Horario.CrearHorario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Features.Horario.DialogTimePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Objects;

public class HorarioCrearActivity extends DialogFragment implements DialogTimePicker.TimePickerListener {

    private Button TimeEntradaAdd, TimeSalidaAdd;
    private TextInputEditText txtAula;

    private String Aula ="";
    private String HoraEntrada = "";
    private String HoraSalida = "";

    private static HorarioCrearListener horarioCrearListener;
    private HorarioCrearListener listener;
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());

    public HorarioCrearActivity(){}

    public static HorarioCrearActivity newInstance(String title, HorarioCrearListener listener){
        horarioCrearListener = listener;
        HorarioCrearActivity horarioCrearActivity = new HorarioCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        horarioCrearActivity.setArguments(bundle);

        horarioCrearActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);

        return horarioCrearActivity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (horarioCrearListener != context){
                listener = (HorarioCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements DocenteCreateListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_horario,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarH);
        txtAula = view.findViewById(R.id.textAula);
        TimeEntradaAdd = view.findViewById(R.id.in_time1);
        TimeSalidaAdd = view.findViewById(R.id.in_time2);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Aula = Objects.requireNonNull(txtAula.getText()).toString();
                HoraEntrada = TimeEntradaAdd.getText().toString();
                HoraSalida = TimeSalidaAdd.getText().toString();

                if (!Aula.isEmpty() && !HoraEntrada.isEmpty() && !HoraSalida.isEmpty()){
                    if (Aula.length() == 3){
                        Horario horario = new Horario();
                        horario.setAula(Aula);
                        horario.setHora_entrada(HoraEntrada);
                        horario.setHora_salida(HoraSalida);
                        if (!operacionesHorario.HorarioRepetido(Aula,HoraEntrada,HoraSalida)){
                            long insercion = operacionesHorario.InsertarHor(horario);
                            if (insercion > 0){
                                int inser = (int)insercion;
                                horario.setId_horario(inser);
                                if (horarioCrearListener != null){
                                    horarioCrearListener.onCrearHorario(horario);
                                }else {
                                    listener.onCrearHorario(horario);
                                }
                                dismiss();
                            }
                        }else {
                            Toast.makeText(getContext(),"Horario ya existe",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"El numero de aula debe ser de 3 digitos",Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(getContext(),"Agregue el aula",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        TimeEntradaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogTimePicker("Entrada");
            }
        });

        TimeSalidaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogTimePicker("Salida");
            }
        });

        return view;
    }

    private void llamarDialogTimePicker(String TipoHora){
        DialogTimePicker dialogTimePicker = DialogTimePicker.newInstance(TipoHora);
        dialogTimePicker.setTargetFragment(HorarioCrearActivity.this,22);
        dialogTimePicker.setCancelable(false);
        if (getFragmentManager() != null) {
            dialogTimePicker.show(getFragmentManager(),"timePicker");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute, String tipoHora) {
        switch (tipoHora) {
            case "Entrada":
                TimeEntradaAdd.setText(String.format("%s : %s",hour,minute));
            break;
            case "Salida":
                TimeSalidaAdd.setText(String.format("%s : %s",hour,minute));
        }
    }
}
