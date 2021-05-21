package com.utpl.agendadocente.features.periodo.actualizar_periodo;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.features.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.periodo.IPeriodo;

public class PeriodoActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener{

    private static long idPeriodo;
    private static int periodoItemPosition;
    private static IPeriodo.ActualizarPeriodoListener actualizarPeriodoListener;

    private PeriodoAcademico periodoAcademico;

    private Button btnPerIn;
    private Button btnPerFin;

    private OperacionesPeriodo operacionesPeriodo = (OperacionesPeriodo) OperacionesFactory.getOperacionPeriodo(getContext());

    public PeriodoActualizarActivity(){
        //Required constructor
    }

    public static PeriodoActualizarActivity newInstance(Integer id, int position, IPeriodo.ActualizarPeriodoListener listener){
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

        Toolbar toolbar = view.findViewById(R.id.toolbarPer);
        btnPerIn = view.findViewById(R.id.btnFInicioA);
        btnPerFin = view.findViewById(R.id.btnFFinA);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        periodoAcademico = operacionesPeriodo.obtenerPeriodo(idPeriodo);
        if (periodoAcademico!=null){
            btnPerIn.setText(periodoAcademico.getFechaInicio());
            btnPerFin.setText(periodoAcademico.getFechaFin());

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
                    guardarPeriodo();
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

    private void guardarPeriodo() {
        String fechaInicio = btnPerIn.getText().toString();
        String fechaFin = btnPerFin.getText().toString();

        if (!fechaInicio.isEmpty()){
            periodoAcademico.setFechaInicio(fechaInicio);
            periodoAcademico.setFechaFin(fechaFin);

            if (operacionesPeriodo.periodoRepetido(fechaInicio, fechaFin)){

                long insercion = operacionesPeriodo.modificarPeriodo(periodoAcademico);

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
    }


    private void llamarDialogDatePicker(String tipoPeriodo){
        DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance(tipoPeriodo);
        dialogDatePicker.setTargetFragment(PeriodoActualizarActivity.this,22);
        dialogDatePicker.setCancelable(false);
        dialogDatePicker.show(getParentFragmentManager(), "tag");
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
    public void onDateSet(DatePicker datePicker, String fecha, String tipo) {
        if (tipo.equals("Inicio")){
            btnPerIn.setText(fecha);
        }else {
            btnPerFin.setText(fecha);
        }
    }
}
