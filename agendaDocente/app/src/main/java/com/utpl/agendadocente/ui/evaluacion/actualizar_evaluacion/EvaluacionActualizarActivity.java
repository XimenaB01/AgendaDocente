package com.utpl.agendadocente.ui.evaluacion.actualizar_evaluacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.IEvaluacion;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.tarea.actualizar_tarea.TareaActualizarActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idEvaluacion;
    private static int evaluacionItemPosition;
    private static IEvaluacion.ActualizarEvaluacionListener actualizarEvaluacionListener;

    private Evaluacion evaluacion;

    private Button fechEvaAct1;
    private Button tipoEvaActButton;
    private TextInputEditText txtnomEvaAct;
    private TextInputEditText txtObsEvaAct;
    private Spinner cuesEvaActSp;
    private RadioButton rb1BimEAct;
    private RadioButton rb2BimEAct;
    private TextView parAsignado;
    private Toolbar toolbar;
    private Button btnParalelo;

    private int cuesEvaAct = 0;
    private String bimEvaAct = "";
    private String texto = "Sin Asignar";
    private Integer idParalelo = null;

    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private List<Cuestionario> cuestListAct = operacionesCuestionario.listarCuestionario();
    private ArrayList<String> listCuetAct = new ArrayList<>();
    private List<String> paralalosAsignados = new ArrayList<>();

    private OperacionesEvaluacion operacionesEvaluacion;

    public EvaluacionActualizarActivity() {
        //Required constructor
    }

    public static EvaluacionActualizarActivity newInstance(Integer id, int position, IEvaluacion.ActualizarEvaluacionListener listener) {
        idEvaluacion = id;
        evaluacionItemPosition = position;
        actualizarEvaluacionListener = listener;
        EvaluacionActualizarActivity evaluacionActualizarActivity = new EvaluacionActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Evaluación");
        evaluacionActualizarActivity.setArguments(bundle);

        evaluacionActualizarActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return evaluacionActualizarActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_evaluacion, container,false);

        operacionesEvaluacion = new OperacionesEvaluacion(getContext());

        toolbar = view.findViewById(R.id.toolbarEv);
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);


        txtnomEvaAct = view.findViewById(R.id.textNomEvaAct);
        cuesEvaActSp = view.findViewById(R.id.spinnerEvaAct);
        fechEvaAct1 = view.findViewById(R.id.btnfecEvAct);
        rb1BimEAct = view.findViewById(R.id.rb1BAct);
        rb2BimEAct = view.findViewById(R.id.rb2BAct);
        txtObsEvaAct = view.findViewById(R.id.textObsEvaAct);
        tipoEvaActButton = view.findViewById(R.id.tipoEvAct);
        parAsignado = view.findViewById(R.id.paraleloAsignadoAct);
        btnParalelo = view.findViewById(R.id.paraleloAsigEvaAct);

        evaluacion = operacionesEvaluacion.obtenerEvaluacion(idEvaluacion);

        llenarFormulario();
        return view;
    }

    private void obtenerParalelos(String itemAsignado) {
        List<Paralelo> paraleloList = operacionesParalelo.listarParalelo();
        List<Asignatura> asignaturaList = operacionesAsignatura.listarAsignatura();

        for (int i = 0; i < paraleloList.size(); i++){
            for (int j = 0; j < asignaturaList.size(); j++){
                if (paraleloList.get(i).getAsignaturaID().equals(asignaturaList.get(j).getIdAsignatura())){
                    paralalosAsignados.add(asignaturaList.get(j).getNombreAsignatura()+" - "+paraleloList.get(i).getNombreParalelo());
                    if (!itemAsignado.isEmpty() && itemAsignado.equals(asignaturaList.get(j).getNombreAsignatura()+" - "+paraleloList.get(i).getNombreParalelo())){
                        idParalelo = paraleloList.get(i).getIdParalelo();
                    }
                }
            }
        }
    }

    private void obtenerParaleloAsignado(long paraleloID) {
        if (paraleloID != 0){
            Paralelo paralelo = operacionesParalelo.obtenerParalelo(paraleloID);
            Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());

            String paraleloAsignado = asignatura.getNombreAsignatura() + " - " + paralelo.getNombreParalelo();

            parAsignado.setText(paraleloAsignado);
        }else {
            parAsignado.setText(texto);
        }
    }

    private void asignarParalelo(List<String> lista, String paraleloAsignado){

        final String [] par = new String[lista.size()];
        int posicion = -1;

        for (int i = 0; i < lista.size(); i++){
            par[i] = lista.get(i);
            if (paraleloAsignado.equals(par[i])){
                posicion = i;
            }
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Paralelos");
        dialog.setSingleChoiceItems(par, posicion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                parAsignado.setText(par[i]);
                dialogInterface.dismiss();
            }
        });
        dialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private void obtenerspinnercuestio(){
        listCuetAct.add("Seleccione Cuestionario");
        for (int i = 0; i< cuestListAct.size(); i++){
            listCuetAct.add(cuestListAct.get(i).getNombreCuestionario());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal, listCuetAct);
        cuesEvaActSp.setAdapter(adapter);
    }

    private void llenarFormulario(){
        evaluacion = operacionesEvaluacion.obtenerEvaluacion(idEvaluacion);

        if (evaluacion != null) {
            obtenerTipoEvaluacion();
            obtenerFechaEvaluacion();
            obtenerspinnercuestio();
            obtenerBimestre();

            for (int i = 0; i < cuestListAct.size(); i++){
                if (evaluacion.getCuestionarioID().equals(cuestListAct.get(i).getIdCuestionario())){
                    cuesEvaActSp.setSelection(TareaActualizarActivity.obtenerPositionItem(cuesEvaActSp,cuestListAct.get(i).getNombreCuestionario()));
                }
            }

            txtnomEvaAct.setText(evaluacion.getNombreEvaluacion());

            txtObsEvaAct.setText(evaluacion.getObservacion());

            if (evaluacion.getParaleloID() != null){
                obtenerParaleloAsignado(evaluacion.getParaleloID());
            }

            btnParalelo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paralalosAsignados.isEmpty()){
                        obtenerParalelos("");
                    }
                    asignarParalelo(paralalosAsignados, parAsignado.getText().toString());
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
                    guardarEvaluacion();
                    return true;
                }
            });
        }
    }

    private void obtenerBimestre() {
        String bim = evaluacion.getBimestre();
        if (bim.equals("1er Bimestre")){
            rb1BimEAct.setChecked(true);
        }else if (bim.equals("2do Bimestre")){
            rb2BimEAct.setChecked(true);
        }
    }

    private void obtenerFechaEvaluacion() {
        if (!evaluacion.getFechaEvaluacion().equals(texto) && !evaluacion.getFechaEvaluacion().equals("Fecha de Evaluación")){
            fechEvaAct1.setText(evaluacion.getFechaEvaluacion());
        }else {
            String texto2 = "Fecha de Evaluación";
            fechEvaAct1.setText(texto2);
        }

        fechEvaAct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(EvaluacionActualizarActivity.this,22);
                dialogDatePicker.setCancelable(false);
                dialogDatePicker.show(getParentFragmentManager(), Utilidades.CREAR);
            }
        });
    }

    private void obtenerTipoEvaluacion() {
        final String tipoTexto = evaluacion.getTipo();
        if (!tipoTexto.equals(texto) && !tipoTexto.equals("Tipo de Evaluación")){
            tipoEvaActButton.setText(tipoTexto);

        }else {
            String texto1 = "Tipo de Evaluación";
            tipoEvaActButton.setText(texto1);
        }

        tipoEvaActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
                evaluacionCrearActivity.obtenerTipoEvaluacion(tipoTexto, getContext(), new EvaluacionCrearActivity.RetornoDeValor() {
                    @Override
                    public void retornarvalor(String valor) {
                        tipoEvaActButton.setText(valor);
                    }
                });
            }
        });
    }


    private void guardarEvaluacion(){

        String nomEvaAct = Objects.requireNonNull(txtnomEvaAct.getText()).toString();


        String tipoEvaAct = tipoEvaActButton.getText().toString();
        String fechEvaAct = fechEvaAct1.getText().toString();

        String cuest =cuesEvaActSp.getSelectedItem().toString();

        for (int i = 0; i < cuestListAct.size(); i++){
            if (cuestListAct.get(i).getNombreCuestionario().equals(cuest)){
                cuesEvaAct = cuestListAct.get(i).getIdCuestionario();
            }
        }

        if (rb1BimEAct.isChecked()){
            bimEvaAct = rb1BimEAct.getText().toString();
        } else if(rb2BimEAct.isChecked()){
            bimEvaAct = rb2BimEAct.getText().toString();
        }

        obtenerParalelos(parAsignado.getText().toString());
        String obsEvaAct = Objects.requireNonNull(txtObsEvaAct.getText()).toString();

        if (!nomEvaAct.isEmpty()){
            evaluacion.setNombreEvaluacion(nomEvaAct);
            evaluacion.setTipo(tipoEvaAct);
            evaluacion.setBimestre(bimEvaAct);
            evaluacion.setFechaEvaluacion(fechEvaAct);
            evaluacion.setObservacion(obsEvaAct);
            evaluacion.setCuestionarioID(cuesEvaAct);
            evaluacion.setParaleloID(idParalelo);

            long insercion = operacionesEvaluacion.modificarEvaluacion(evaluacion);

            if (insercion > 0) {
                actualizarEvaluacionListener.onActualizarEvaluacion(evaluacion, evaluacionItemPosition);
                dismiss();
            }
        }else {
            Toast.makeText(getContext(),"Agregar el nombre",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, String fecha, String tipo) {
        fechEvaAct1.setText(fecha);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }
}