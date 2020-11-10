package com.utpl.agendadocente.ui.periodo.CrearPeriodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Objects;

public class PeriodoCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener{

    private static PeriodoCreateListener periodoCreateListener;
    private PeriodoCreateListener listener;

    private Button btnFechaIn, btnFechaFin;

    private String FechaInicio = "";
    private String FechaFin = "";

    private OperacionesPeriodo operacionesPeriodo;

    public PeriodoCrearActivity(){}

    public static PeriodoCrearActivity  newInstance(String title, PeriodoCreateListener listener){
        periodoCreateListener = listener;
        PeriodoCrearActivity perCreAc = new PeriodoCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        perCreAc.setArguments(bundle);

        perCreAc.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);

        return perCreAc;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (periodoCreateListener != context){
                listener = (PeriodoCreateListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crear_periodo,container,false);

        operacionesPeriodo = new OperacionesPeriodo(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarP);
        btnFechaIn = view.findViewById(R.id.btnFInicio);
        btnFechaFin = view.findViewById(R.id.btnFFin);

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
                FechaInicio = btnFechaIn.getText().toString();
                FechaFin = btnFechaFin.getText().toString();

                PeriodoAcademico periodoAcademico = new PeriodoAcademico();
                if (!FechaInicio.isEmpty() && !FechaFin.isEmpty()){
                    periodoAcademico.setFechaInicio(FechaInicio);
                    periodoAcademico.setFechaFin(FechaFin);

                    if (!operacionesPeriodo.PeriodoRepetido(FechaInicio,FechaFin)){
                        long insercion = operacionesPeriodo.InsertarPer(periodoAcademico);
                        if (insercion > 0){
                            int inser = (int)insercion;
                            periodoAcademico.setId_periodo(inser);
                            if (periodoCreateListener != null) {
                                periodoCreateListener.onCrearPeriodo(periodoAcademico);
                            }else {
                                listener.onCrearPeriodo(periodoAcademico);
                            }
                            dismiss();
                        }
                    }else {
                        Toast.makeText(getContext(),"Ya existe este Periodo Académico",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getContext(),"Agregue las fechas del Perdiodo Académico",Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        btnFechaIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogDatePicker("Inicio");
            }
        });
        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogDatePicker("Fin");
            }
        });

        return view;
    }

    private void llamarDialogDatePicker(String tipoPeriodo){
        DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance(tipoPeriodo);
        dialogDatePicker.setTargetFragment(PeriodoCrearActivity.this,22);
        dialogDatePicker.setCancelable(false);
        if (getFragmentManager() != null) {
            dialogDatePicker.show(getFragmentManager(), "tag");
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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day, String tipo) {
        switch (tipo){
            case "Inicio":
                btnFechaIn.setText(String.format("%s/%s/%s",day,month,year));
                break;
            case "Fin":
                btnFechaFin.setText(String.format("%s/%s/%s",day,month,year));
                break;
        }
    }
}