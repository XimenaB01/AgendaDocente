package com.utpl.agendadocente.ui.horario.ActualizarHorario;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.ui.horario.DialogTimePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Objects;

public class HorarioActualizarActivity extends DialogFragment implements DialogTimePicker.TimePickerListener{

    private static long idHorario;
    private static int horarioItemPosition;
    private static ActualizarHorarioListener actualizarHorarioListener;

    private Horario horario;

    private Button TimeEntradaAddAct, TimeSalidaAddAct;
    private TextInputEditText txtAula;

    private String Aula = "";
    private String HoraEntrada = "";
    private String HoraSalida = "";

    private OperacionesHorario operacionesHorario;

    public HorarioActualizarActivity(){}

    public static HorarioActualizarActivity newInstance(Integer id, int position, ActualizarHorarioListener listener){
        idHorario = id;
        horarioItemPosition = position;
        actualizarHorarioListener = listener;
        HorarioActualizarActivity HorActActi = new HorarioActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Horario");
        HorActActi.setArguments(bundle);

        HorActActi.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return HorActActi;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_horario, container, false);

        operacionesHorario = new OperacionesHorario(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarAH);
        TimeEntradaAddAct = view.findViewById(R.id.in_time1Act);
        txtAula = view.findViewById(R.id.textAulaAct);
        TimeSalidaAddAct = view.findViewById(R.id.in_time2Act);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        horario = operacionesHorario.obtenerHor(idHorario);

        if (horario!=null){
            txtAula.setText(horario.getAula());
            TimeEntradaAddAct.setText(horario.getHora_entrada());
            TimeSalidaAddAct.setText(horario.getHora_salida());

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Aula = Objects.requireNonNull(txtAula.getText()).toString();
                    HoraEntrada = TimeEntradaAddAct.getText().toString();
                    HoraSalida = TimeSalidaAddAct.getText().toString();

                    if (!Aula.isEmpty()){
                        if (Aula.length() == 3){
                            horario.setAula(Aula);
                            horario.setHora_entrada(HoraEntrada);
                            horario.setHora_salida(HoraSalida);

                            if(!operacionesHorario.HorarioRepetido(Aula,HoraEntrada,HoraSalida)){
                                long insercion = operacionesHorario.ModificarHor(horario);

                                if (insercion > 0){
                                    actualizarHorarioListener.onActualizarHorario(horario,horarioItemPosition);
                                    dismiss();
                                }
                            }else {
                                Toast.makeText(getContext(),"No ha hecho ningún cambio",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getContext(),"El numero de aula debe ser de 3 digitos",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"Ingresar el Aula",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });

            TimeEntradaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogTimePicker("Entrada");
                }
            });

            TimeSalidaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogTimePicker("Salida");
                }
            });
        }
        return view;
    }

    private void llamarDialogTimePicker(String TipoHora){
        DialogTimePicker dialogTimePicker = DialogTimePicker.newInstance(TipoHora);
        dialogTimePicker.setTargetFragment(HorarioActualizarActivity.this,22);
        dialogTimePicker.setCancelable(false);
        dialogTimePicker.show(getParentFragmentManager(),"timePicker");
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
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute, String tipo) {
        switch (tipo) {
            case "Entrada":
                TimeEntradaAddAct.setText(String.format("%s : %s",hour,minute));
                break;
            case "Salida":
                TimeSalidaAddAct.setText(String.format("%s : %s",hour,minute));
        }
    }
}
