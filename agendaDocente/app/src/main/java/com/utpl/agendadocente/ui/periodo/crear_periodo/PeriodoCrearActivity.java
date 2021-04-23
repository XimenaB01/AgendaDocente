package com.utpl.agendadocente.ui.periodo.crear_periodo;

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

import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.periodo.IPeriodo;

public class PeriodoCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener{

    private static IPeriodo.PeriodoCreateListener periodoCreateListener;
    private IPeriodo.PeriodoCreateListener listener;
    private Button btnFechaIn;
    private Button btnFechaFin;
    private OperacionesPeriodo operacionesPeriodo;

    public PeriodoCrearActivity(){
        //Required constructor
    }

    public static PeriodoCrearActivity  newInstance(String title, IPeriodo.PeriodoCreateListener listener){
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
                listener = (IPeriodo.PeriodoCreateListener) getTargetFragment();
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

        operacionesPeriodo = (OperacionesPeriodo) OperacionesFactory.getOperacionPeriodo(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarP);
        btnFechaIn = view.findViewById(R.id.btnFInicio);
        btnFechaFin = view.findViewById(R.id.btnFFin);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.guardar);

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                guardarPeriodo();
                return true;
            }
        });

        return view;
    }

    private void guardarPeriodo() {
        String fechaInicio = btnFechaIn.getText().toString();
        String fechaFin = btnFechaFin.getText().toString();

        PeriodoAcademico periodoAcademico = new PeriodoAcademico();
        if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()){
            periodoAcademico.setFechaInicio(fechaInicio);
            periodoAcademico.setFechaFin(fechaFin);

            if (operacionesPeriodo.periodoRepetido(fechaInicio, fechaFin)){
                long insercion = operacionesPeriodo.insertarPeriodo(periodoAcademico);
                if (insercion > 0){
                    int inser = (int)insercion;
                    periodoAcademico.setIdPeriodo(inser);
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

    }

    private void llamarDialogDatePicker(String tipoPeriodo){
        DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance(tipoPeriodo);
        dialogDatePicker.setTargetFragment(PeriodoCrearActivity.this,22);
        dialogDatePicker.setCancelable(false);
        dialogDatePicker.show(getParentFragmentManager(), "tag");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }

    @Override
    public void onDateSet(DatePicker datePicker, String fecha, String tipo) {
        if (tipo.equals("Inicio")){
            btnFechaIn.setText(fecha);
        }else {
            btnFechaFin.setText(fecha);
        }
    }
}