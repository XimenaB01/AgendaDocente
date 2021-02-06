package com.utpl.agendadocente.ui.tarea.ActualizarTarea;

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
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Paralelo;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TareaActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idTarea;
    private static int tareaItemPosition;
    private static ActualizarTareaListener actualizarTareaListener;

    private Button btnFechAct, estadoTareaAct;
    private TextInputEditText nomTareaAct, descTareaAct, obsTareaAct;
    private Spinner ParTareaAct;

    private String nomTarAct = "";
    private String desTarAct = "";
    private String obsTarAct = "";
    private String fecEntTarAct = "";
    private String estadoTarAct = "";
    private Integer IdParalelo = null;
    private List<String> Paralelos = new ArrayList<>();

    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

    private Tarea tarea  = operacionesTarea.obtenerTar(idTarea);

    private List<Asignatura> ListaAsignaturas = operacionesAsignatura.ListarAsig();
    private List<Paralelo> ListaParalelos = operacionesParalelo.ListarPar();

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
        ParTareaAct = view.findViewById(R.id.paraleloTareaAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        obtenerParalelosSpinner();

        if (tarea!=null){
            nomTareaAct.setText(tarea.getNombreTarea());
            descTareaAct.setText(tarea.getDescripcionTarea());
            btnFechAct.setText(tarea.getFechaTarea());
            obsTareaAct.setText(tarea.getObservacionTarea());
            estadoTareaAct.setText(tarea.getEstadoTarea());

            obtenerParaleloAsignadoSpinner();

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
                    obtenerIdParaleloAsignado(ParTareaAct.getSelectedItem().toString());

                    if (!nomTarAct.isEmpty()){
                        tarea.setNombreTarea(nomTarAct);
                        tarea.setDescripcionTarea(desTarAct);
                        tarea.setFechaTarea(fecEntTarAct);
                        tarea.setObservacionTarea(obsTarAct);
                        tarea.setEstadoTarea(estadoTarAct);
                        tarea.setParaleloId(IdParalelo);

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
                    dialogDatePicker.show(getChildFragmentManager(),utilidades.CREAR);
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
    public void onDateSet(DatePicker datePicker, String fecha, String tipo) {
        btnFechAct.setText(fecha);
    }

    private void obtenerParalelosSpinner(){

        //rellenado del spinner con los Paralelos
        Paralelos.add("Selecionar Paralelo");
        for (int i = 0; i < ListaParalelos.size(); i++){
            for (int j = 0; j < ListaAsignaturas.size(); j++){
                if (ListaParalelos.get(i).getAsignaturaID().equals(ListaAsignaturas.get(j).getId_asignatura())){
                    Paralelos.add(ListaAsignaturas.get(j).getNombreAsignatura() + " - " + ListaParalelos.get(i).getNombreParalelo());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal,Paralelos);
        ParTareaAct.setAdapter(adapter);
    }

    private void obtenerParaleloAsignadoSpinner(){

        //Obtenemos el ParaleoAsignado de la Tarea como la opción seleccionada del Spinner
        String ParAsig = "";
        if (tarea.getParaleloId()!=null){
            for (int i = 0; i < ListaParalelos.size(); i++){
                if (tarea.getParaleloId().equals(ListaParalelos.get(i).getId_paralelo())){
                    for (int j = 0; j < ListaAsignaturas.size(); j++){
                        if (ListaParalelos.get(i).getAsignaturaID().equals(ListaAsignaturas.get(j).getId_asignatura())){
                            ParAsig = ListaAsignaturas.get(j).getNombreAsignatura() + " - " + ListaParalelos.get(i).getNombreParalelo();
                        }
                    }
                }
            }
        }

        ParTareaAct.setSelection(obtenerPositionItem(ParTareaAct,ParAsig));
    }

    private void obtenerIdParaleloAsignado( String ParAsigSelecionado){

        //Obtenemos el Id de ParaleoAsignado para Tarea
        for (int i = 0; i<ListaParalelos.size();i++){
            for (int j = 0; j<ListaAsignaturas.size(); j++){
                if (ParAsigSelecionado.equals( ListaAsignaturas.get(j).getNombreAsignatura()+ " - " + ListaParalelos.get(i).getNombreParalelo() )){
                    IdParalelo = ListaParalelos.get(i).getId_paralelo();
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