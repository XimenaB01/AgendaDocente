package com.utpl.agendadocente.Features.Periodo.ActualizarPeriodo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Features.Periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

public class PeriodoActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener{

    private static long idPeriodo;
    private static int periodoItemPosition;
    private static ActualizarPeriodoListener actualizarPeriodoListener;

    private PeriodoAcademico periodoAcademico;

    private TextView FechaInicioAct, FechaFinAct;

    private String FechaInicio = "";
    private String FechaFin = "";

    private OperacionesPeriodo operacionesPeriodo;

    public PeriodoActualizarActivity(){}

    public static PeriodoActualizarActivity newInstance(Integer id, int position, ActualizarPeriodoListener listener){
        idPeriodo = id;
        periodoItemPosition = position;
        actualizarPeriodoListener = listener;
        PeriodoActualizarActivity periodoActualizarActivity = new PeriodoActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Periodo");
        periodoActualizarActivity.setArguments(bundle);

        periodoActualizarActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return periodoActualizarActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_periodo,container,false);

        operacionesPeriodo = new OperacionesPeriodo(getContext());
        Toolbar toolbar = view.findViewById(R.id.toolbarPer);
        FechaInicioAct = view.findViewById(R.id.textInicioA);
        FechaFinAct = view.findViewById(R.id.textFinA);
        Button btnPerIn = view.findViewById(R.id.btnFInicioA);
        Button btnPerFin = view.findViewById(R.id.btnFFinA);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        periodoAcademico = operacionesPeriodo.obtenerPer(idPeriodo);
        if (periodoAcademico!=null){
                FechaInicioAct.setText(periodoAcademico.getFechaInicio());
                FechaFinAct.setText(periodoAcademico.getFechaFin());

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
                    FechaInicio = FechaInicioAct.getText().toString();
                    FechaFin = FechaFinAct.getText().toString();

                    if (!FechaInicio.isEmpty()){
                        periodoAcademico.setFechaInicio(FechaInicio);
                        periodoAcademico.setFechaFin(FechaFin);

                        if (!operacionesPeriodo.PeriodoRepetido(FechaInicio,FechaFin)){

                            long insercion = operacionesPeriodo.ModificarPer(periodoAcademico);

                            if (insercion > 0){
                                actualizarPeriodoListener.onActualizarPeriodo(periodoAcademico,periodoItemPosition);
                                dismiss();
                            }

                        }else {
                            Toast.makeText(getContext(),"Ya existe este Periodo Académico",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getContext(),"Agregue la Fecha de Inicio",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });

            btnPerIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogDatePicker("Inicio");
                }
            });

            btnPerFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogDatePicker("Fin");
                }
            });
        }
        return view;

    }

    private void llamarDialogDatePicker(String tipoPeriodo){
        DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance(tipoPeriodo);
        dialogDatePicker.setTargetFragment(PeriodoActualizarActivity.this,22);
        dialogDatePicker.setCancelable(false);
        if (getFragmentManager() != null) {
            dialogDatePicker.show(getFragmentManager(), "tag");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day, String tipo) {

    }
}
