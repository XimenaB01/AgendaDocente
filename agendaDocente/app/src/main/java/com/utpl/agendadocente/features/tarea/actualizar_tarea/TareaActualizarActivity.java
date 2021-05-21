package com.utpl.agendadocente.features.tarea.actualizar_tarea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.features.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.features.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TareaActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idTarea;
    private static int tareaItemPosition;
    private static ITarea.ActualizarTareaListener actualizarTareaListener;

    private Button btnFechAct;
    private TextInputEditText nomTareaAct;
    private TextInputEditText descTareaAct;
    private TextInputEditText obsTareaAct;
    private Spinner parTareaAct;

    private String nomTarAct = "";
    private String desTarAct = "";
    private String obsTarAct = "";
    private String fecEntTarAct = "";
    private Integer idParalelo = null;
    private List<String> paralelos = new ArrayList<>();

    private OperacionesTarea operacionesTarea = (OperacionesTarea) OperacionesFactory.getOperacionTarea(getContext());
    private OperacionesParalelo operacionesParalelo = (OperacionesParalelo) OperacionesFactory.getOperacionParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = (OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(getContext());


    private Tarea tarea  = operacionesTarea.obtenerTarea(idTarea);

    private List<Asignatura> listaAsignaturas = operacionesAsignatura.listarAsignatura();
    private List<Paralelo> listaParalelos = operacionesParalelo.listarParalelo();

    public TareaActualizarActivity(){
        // Required empty public constructor
    }

    public static TareaActualizarActivity newInstance(Integer id, int position, ITarea.ActualizarTareaListener listener){
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
        parTareaAct = view.findViewById(R.id.paraleloTareaAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        obtenerParalelosSpinner();

        if (tarea!=null){
            nomTareaAct.setText(tarea.getNombreTarea());
            descTareaAct.setText(tarea.getDescripcionTarea());
            btnFechAct.setText(tarea.getFechaTarea());
            obsTareaAct.setText(tarea.getObservacionTarea());

            obtenerParaleloAsignadoSpinner();

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
                    obtenerIdParaleloAsignado(parTareaAct.getSelectedItem().toString());

                    if (!nomTarAct.isEmpty()){
                        tarea.setNombreTarea(nomTarAct);
                        tarea.setDescripcionTarea(desTarAct);
                        tarea.setFechaTarea(fecEntTarAct);
                        tarea.setObservacionTarea(obsTarAct);
                        tarea.setParaleloId(idParalelo);

                        long insercion = operacionesTarea.modificarTarea(tarea);
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
                    dialogDatePicker.show(getParentFragmentManager(), Utilidades.CREAR);
                }
            });
        }
        return view;
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
        btnFechAct.setText(fecha);
    }

    private void obtenerParalelosSpinner(){

        //rellenado del spinner con los paralelos
        paralelos.add("Selecionar Paralelo");
        for (int i = 0; i < listaParalelos.size(); i++){
            for (int j = 0; j < listaAsignaturas.size(); j++){
                if (listaParalelos.get(i).getAsignaturaID().equals(listaAsignaturas.get(j).getIdAsignatura())){
                    paralelos.add(listaAsignaturas.get(j).getNombreAsignatura() + " - " + listaParalelos.get(i).getNombreParalelo());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal, paralelos);
        parTareaAct.setAdapter(adapter);
    }

    private void obtenerParaleloAsignadoSpinner(){

        //Obtenemos el ParaleoAsignado de la Tarea como la opción seleccionada del Spinner
        String parAsig = "";
        if (tarea.getParaleloId()!=null){
            for (int i = 0; i < listaParalelos.size(); i++){
                if (tarea.getParaleloId().equals(listaParalelos.get(i).getIdParalelo())){
                    for (int j = 0; j < listaAsignaturas.size(); j++){
                        if (listaParalelos.get(i).getAsignaturaID().equals(listaAsignaturas.get(j).getIdAsignatura())){
                            parAsig = listaAsignaturas.get(j).getNombreAsignatura() + " - " + listaParalelos.get(i).getNombreParalelo();
                        }
                    }
                }
            }
        }

        parTareaAct.setSelection(obtenerPositionItem(parTareaAct,parAsig));
    }

    private void obtenerIdParaleloAsignado( String parAsigSelecionado){

        //Obtenemos el Id de ParaleoAsignado para Tarea
        for (int i = 0; i< listaParalelos.size(); i++){
            for (int j = 0; j< listaAsignaturas.size(); j++){
                if (parAsigSelecionado.equals( listaAsignaturas.get(j).getNombreAsignatura()+ " - " + listaParalelos.get(i).getNombreParalelo() )){
                    idParalelo = listaParalelos.get(i).getIdParalelo();
                }
            }
        }

    }

    public static int obtenerPositionItem(Spinner spinnerPA, String tipo){

        //Buscamos de todas las opciones del Spinner en que posicion se encuentra el Paralelo Asignado
        int pos = 0;
        for (int i = 0; i <spinnerPA.getCount(); i++){
            if (spinnerPA.getItemAtPosition(i).toString().equalsIgnoreCase(tipo)){
                pos=i;
            }
        }
        return pos;
    }

}
