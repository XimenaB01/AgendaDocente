package com.utpl.agendadocente.ui.horario.crear_horario;

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
import android.widget.PopupMenu;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.ui.horario.DialogTimePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.horario.IHorario;

import java.util.Objects;

public class HorarioCrearActivity extends DialogFragment implements DialogTimePicker.TimePickerListener {

    private Button timeEntradaAdd;
    private Button timeSalidaAdd;
    private Button buttonDia;
    private TextInputEditText txtAula;
    private Toolbar toolbar;

    private String aula ="";
    private String dia ="";
    private String horaEntrada = "";
    private String horaSalida = "";

    private static IHorario.HorarioCrearListener horarioCrearListener;
    private IHorario.HorarioCrearListener listener;
    private OperacionesHorario operacionesHorario = (OperacionesHorario) OperacionesFactory.getOperacionHorario(getContext());

    public HorarioCrearActivity(){
        //required constructor
    }

    public static HorarioCrearActivity newInstance(String title, IHorario.HorarioCrearListener listener){
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
                listener = (IHorario.HorarioCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
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

        toolbar = view.findViewById(R.id.toolbarH);
        txtAula = view.findViewById(R.id.textAula);
        buttonDia = view.findViewById(R.id.dia);
        timeEntradaAdd = view.findViewById(R.id.in_time1);
        timeSalidaAdd = view.findViewById(R.id.in_time2);

        buttonDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuDias = new PopupMenu(getContext(), buttonDia);
                popupMenuDias.getMenuInflater().inflate(R.menu.dias,popupMenuDias.getMenu());

                popupMenuDias.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        buttonDia.setText(menuItem.getTitle());
                        return true;
                    }
                });
                popupMenuDias.show();
            }
        });

        crearToolbar();

        timeEntradaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogTimePicker("Entrada");
            }
        });

        timeSalidaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogTimePicker("Salida");
            }
        });

        return view;
    }

    private void crearToolbar() {
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                obtenerDatosFormulario();
                return true;
            }
        });
    }

    private void llamarDialogTimePicker(String tipoHora){
        DialogTimePicker dialogTimePicker = DialogTimePicker.newInstance(tipoHora);
        dialogTimePicker.setTargetFragment(HorarioCrearActivity.this,22);
        dialogTimePicker.setCancelable(false);
        dialogTimePicker.show(getParentFragmentManager(),"timePicker");
    }

    private void obtenerDatosFormulario(){
        aula = Objects.requireNonNull(txtAula.getText()).toString();
        horaEntrada = timeEntradaAdd.getText().toString();
        horaSalida = timeSalidaAdd.getText().toString();
        dia = buttonDia.getText().toString();

        validarDatos();

    }

    private void validarDatos() {
        if (!aula.isEmpty() && !horaEntrada.isEmpty() && !horaSalida.isEmpty()){
            if (aula.length() == 3){
                guardarHorario();
            }else {
                Toast.makeText(getContext(),"El numero de aula debe ser de 3 digitos",Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getContext(),"Agregue el aula",Toast.LENGTH_LONG).show();
        }
    }

    private void guardarHorario() {
        Horario horario = new Horario();
        horario.setAula(aula);
        horario.setDia(dia);
        horario.setHoraEntrada(horaEntrada);
        horario.setHoraSalida(horaSalida);
        if (operacionesHorario.horarioRepetido(horario)){
            long insercion = operacionesHorario.insertarHorario(horario);
            if (insercion > 0){
                int inser = (int)insercion;
                horario.setIdHorario(inser);
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
    }

    @Override
    public void onTimeSet(TimePicker timePicker, String hora, String tipoHora) {
        if (tipoHora.equals("Entrada")){
            timeEntradaAdd.setText(hora);
        }else {
            timeSalidaAdd.setText(hora);
        }
    }
}
