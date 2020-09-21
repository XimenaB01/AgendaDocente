package com.utpl.agendadocente.Features.Tarea.ActualizarTarea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Evaluacion.ActualizarEvaluacion.EvaluacionActualizarActivity;
import com.utpl.agendadocente.Features.Periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Calendar;
import java.util.Objects;

public class TareaActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idTarea;
    private static int tareaItemPosition;
    private static ActualizarTareaListener actualizarTareaListener;

    private Tarea tarea;

    private Button btnFechAct, estadoTareaAct;
    private TextInputEditText nomTareaAct, descTareaAct, obsTareaAct;

    private String nomTarAct = "";
    private String desTarAct = "";
    private String obsTarAct = "";
    private String fecEntTarAct = "";
    private String estadoTarAct = "";

    private OperacionesTarea operacionesTarea;

    public TareaActualizarActivity(){}

    public static TareaActualizarActivity newInstance(Integer id, int position, ActualizarTareaListener listener){
        idTarea = id;
        tareaItemPosition = position;
        actualizarTareaListener = listener;
        TareaActualizarActivity tareaActualizarActivity = new TareaActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Tarea");
        tareaActualizarActivity.setArguments(bundle);

        tareaActualizarActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return tareaActualizarActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_actualizar_tarea, container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarAT);

        nomTareaAct = view.findViewById(R.id.nomTarAct);
        descTareaAct = view.findViewById(R.id.desTarAct);
        obsTareaAct = view.findViewById(R.id.obsTarAct);
        btnFechAct = view.findViewById(R.id.btnFechEAct);
        estadoTareaAct = view.findViewById(R.id.estadosTareaAct);

        operacionesTarea = new OperacionesTarea(getContext());

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        tarea = operacionesTarea.obtenerTar(idTarea);

        if (tarea!=null){
            nomTareaAct.setText(tarea.getNombreTarea());
            descTareaAct.setText(tarea.getDescripcionTarea());
            btnFechAct.setText(tarea.getFechaTarea());
            obsTareaAct.setText(tarea.getObservacionTarea());
            estadoTareaAct.setText(tarea.getEstadoTarea());

            estadoTareaAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenuEstados = new PopupMenu(getContext(),estadoTareaAct);
                    popupMenuEstados.getMenuInflater().inflate(R.menu.estados,popupMenuEstados.getMenu());

                    popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            estadoTareaAct.setText(menuItem.getTitle());
                            return true;
                        }
                    });

                    popupMenuEstados.show();
                }
            });

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
                    nomTarAct = Objects.requireNonNull(nomTareaAct.getText()).toString();
                    desTarAct = Objects.requireNonNull(descTareaAct.getText()).toString();
                    fecEntTarAct = btnFechAct.getText().toString();
                    obsTarAct = Objects.requireNonNull(obsTareaAct.getText()).toString();
                    estadoTarAct = estadoTareaAct.getText().toString();

                    if (!nomTarAct.isEmpty() && !fecEntTarAct.isEmpty()){
                        tarea.setNombreTarea(nomTarAct);
                        tarea.setDescripcionTarea(desTarAct);
                        tarea.setFechaTarea(fecEntTarAct);
                        tarea.setObservacionTarea(obsTarAct);
                        tarea.setEstadoTarea(estadoTarAct);

                        long insercion = operacionesTarea.ModificarTar(tarea);
                        if (insercion > 0){
                            actualizarTareaListener.onActualizarTarea(tarea,tareaItemPosition);
                            dismiss();
                        }
                    }else{
                        Toast.makeText(getContext(), "Agregar un nombre a la tarea", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });

            btnFechAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                    dialogDatePicker.setTargetFragment(TareaActualizarActivity.this,22);
                    dialogDatePicker.setCancelable(false);
                    if (getFragmentManager() != null) {
                        dialogDatePicker.show(getFragmentManager(),utilidades.CREAR);
                    }
                }
            });
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
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day, String tipo) {
        btnFechAct.setText(String.format("%s/%s/%s",day,month,year));
    }
}
