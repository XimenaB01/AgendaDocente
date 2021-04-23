package com.utpl.agendadocente.ui.horario.actualizar_horario;

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
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.ui.horario.DialogTimePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.Objects;

public class HorarioActualizarActivity extends DialogFragment implements DialogTimePicker.TimePickerListener{

    private static long idHorario;
    private static int horarioItemPosition;
    private static IHorario.ActualizarHorarioListener actualizarHorarioListener;

    private Horario horario;

    private Button timeEntradaAddAct;
    private Button timeSalidaAddAct;
    private Button btnDia;
    private TextInputEditText txtAula;

    private OperacionesHorario operacionesHorario;

    public HorarioActualizarActivity(){
        //Required constructor
    }

    public static HorarioActualizarActivity newInstance(Integer id, int position, IHorario.ActualizarHorarioListener listener){
        idHorario = id;
        horarioItemPosition = position;
        actualizarHorarioListener = listener;
        HorarioActualizarActivity horarioActualizarActivity = new HorarioActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Horario");
        horarioActualizarActivity.setArguments(bundle);

        horarioActualizarActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return horarioActualizarActivity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_horario, container, false);

        operacionesHorario = (OperacionesHorario) OperacionesFactory.getOperacionHorario(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarAH);
        timeEntradaAddAct = view.findViewById(R.id.in_time1Act);
        txtAula = view.findViewById(R.id.textAulaAct);
        timeSalidaAddAct = view.findViewById(R.id.in_time2Act);
        btnDia = view.findViewById(R.id.diaAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        horario = operacionesHorario.obtenerHorario(idHorario);

        if (horario!=null){
            txtAula.setText(horario.getAula());
            timeEntradaAddAct.setText(horario.getHoraEntrada());
            timeSalidaAddAct.setText(horario.getHoraSalida());

            btnDia.setText(horario.getDia());
            btnDia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenuDia = new PopupMenu(getContext(), btnDia);
                    popupMenuDia.getMenuInflater().inflate(R.menu.dias, popupMenuDia.getMenu());

                    popupMenuDia.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            btnDia.setText(menuItem.getTitle());
                            return true;
                        }
                    });

                    popupMenuDia.show();
                }
            });

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    enviarHorario();
                    return true;
                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            timeEntradaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogTimePicker("Entrada");
                }
            });

            timeSalidaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogTimePicker("Salida");
                }
            });
        }
        return view;
    }

    private void enviarHorario() {
        String aula = Objects.requireNonNull(txtAula.getText()).toString();
        String horaEntrada = timeEntradaAddAct.getText().toString();
        String dia = btnDia.getText().toString();
        String horaSalida = timeSalidaAddAct.getText().toString();

        if (!aula.isEmpty()){
            if (aula.length() == 3){
                horario.setAula(aula);
                horario.setDia(dia);
                horario.setHoraEntrada(horaEntrada);
                horario.setHoraSalida(horaSalida);

                if(operacionesHorario.horarioRepetido(horario)){
                    long insercion = operacionesHorario.modificarHorario(horario);

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
            Toast.makeText(getContext(),"Ingresar el aula",Toast.LENGTH_LONG).show();
        }
    }

    private void llamarDialogTimePicker(String tipoHora){
        DialogTimePicker dialogTimePicker = DialogTimePicker.newInstance(tipoHora);
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
    public void onTimeSet(TimePicker timePicker, String hora, String tipo) {
        if (tipo.equals("Entrada")){
            timeEntradaAddAct.setText(hora);
        }else {
            timeSalidaAddAct.setText(hora);
        }
    }
}
