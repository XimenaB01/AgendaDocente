package com.utpl.agendadocente.ui.evaluacion.ActualizarEvaluacion;

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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idEvaluacion;
    private static int evaluacionItemPosition;
    private static ActualizarEvaluacionListener actualizarEvaluacionListener;

    private Evaluacion evaluacion;

    private Button fechEvaAct1;
    private TextInputEditText txtnomEvaAct, txtObsEvaAct;
    private Spinner tipoEvaActSp, cuesEvaActSp;
    private RadioButton rb1BimEAct, rb2BimEAct;

    private String nomEvaAct = "";
    private String tipoEvaAct = "";
    private String fechEvaAct = "";
    private int cuesEvaAct = 0;
    private String bimEvaAct = "";
    private String obsEvaAct = "";

    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private List<Cuestionario> cuestListAct = operacionesCuestionario.ListarCuest();
    private ArrayList<String> listCuetAct = new ArrayList<>();

    private OperacionesEvaluacion operacionesEvaluacion;

    public EvaluacionActualizarActivity() {
    }

    public static EvaluacionActualizarActivity newInstance(Integer id, int position, ActualizarEvaluacionListener listener) {
        idEvaluacion = id;
        evaluacionItemPosition = position;
        actualizarEvaluacionListener = listener;
        EvaluacionActualizarActivity EvaActActi = new EvaluacionActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Editar Tarea");
        EvaActActi.setArguments(bundle);

        EvaActActi.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return EvaActActi;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_evaluacion, container,false);

        operacionesEvaluacion = new OperacionesEvaluacion(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarEv);
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);


        txtnomEvaAct = view.findViewById(R.id.textNomEvaAct);
        tipoEvaActSp = view.findViewById(R.id.spinnerTipoAct);
        cuesEvaActSp = view.findViewById(R.id.spinnerEvaAct);
        fechEvaAct1 = view.findViewById(R.id.btnfecEvAct);
        rb1BimEAct = view.findViewById(R.id.rb1BAct);
        rb2BimEAct = view.findViewById(R.id.rb2BAct);
        txtObsEvaAct = view.findViewById(R.id.textObsEvaAct);

        evaluacion = operacionesEvaluacion.obtenerEva(idEvaluacion);

        if (evaluacion != null) {
            spinnersTipoEvaluacion();
            spinnercuestio();
            txtnomEvaAct.setText(evaluacion.getNombreEvaluacion());
            fechEvaAct1.setText(evaluacion.getFechaEvaluacion());
            txtObsEvaAct.setText(evaluacion.getObservacion());


                for (int i = 0; i < cuestListAct.size(); i++){
                    if (evaluacion.getCuestionarioID().equals(cuestListAct.get(i).getId_cuestionario())){
                        cuesEvaActSp.setSelection(i);
                    }
                }


            final String Tipo = evaluacion.getTipo();
            tipoEvaActSp.setSelection(obtenerPositionItem(tipoEvaActSp,Tipo));

            String bim = evaluacion.getBimestre();
            if (bim.equals("1er Bimestre")){
                rb1BimEAct.setChecked(true);
            }else if (bim.equals("2do Bimestre")){
                rb2BimEAct.setChecked(true);
            }
            fechEvaAct1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                    dialogDatePicker.setTargetFragment(EvaluacionActualizarActivity.this,22);
                    dialogDatePicker.setCancelable(false);
                    if (getFragmentManager() != null) {
                        dialogDatePicker.show(getFragmentManager(),utilidades.CREAR);
                    }
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
                    nomEvaAct = Objects.requireNonNull(txtnomEvaAct.getText()).toString();
                    tipoEvaAct = tipoEvaActSp.getSelectedItem().toString();
                    fechEvaAct = fechEvaAct1.getText().toString();

                    String cuest =cuesEvaActSp.getSelectedItem().toString();

                    for (int i = 0; i < cuestListAct.size(); i++){
                        if (cuestListAct.get(i).getNombreCuestionario().equals(cuest)){
                            cuesEvaAct = cuestListAct.get(i).getId_cuestionario();
                        }
                    }

                    if (rb1BimEAct.isChecked()){
                        bimEvaAct = rb1BimEAct.getText().toString();
                    } else if(rb2BimEAct.isChecked()){
                        bimEvaAct = rb2BimEAct.getText().toString();
                    }

                    obsEvaAct = Objects.requireNonNull(txtObsEvaAct.getText()).toString();

                    if (!nomEvaAct.isEmpty()){
                        evaluacion.setNombreEvaluacion(nomEvaAct);
                        evaluacion.setTipo(tipoEvaAct);
                        evaluacion.setBimestre(bimEvaAct);
                        evaluacion.setFechaEvaluacion(fechEvaAct);
                        evaluacion.setObservacion(obsEvaAct);
                        evaluacion.setCuestionarioID(cuesEvaAct);

                        long insercion = operacionesEvaluacion.ModificarEva(evaluacion);

                        if (insercion > 0) {
                            actualizarEvaluacionListener.onActualizarEvaluacion(evaluacion, evaluacionItemPosition);
                            dismiss();
                        }
                    }else {
                        Toast.makeText(getContext(),"Agregar el nombre",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        }
        return view;
    }

    private static int obtenerPositionItem(Spinner spinnerTipo, String tipo){
        int pos = 0;
        for (int i = 0; i <spinnerTipo.getCount(); i++){
            if (spinnerTipo.getItemAtPosition(i).toString().equalsIgnoreCase(tipo)){
                pos=i;
            }
        }
        return pos;
    }

    private void spinnersTipoEvaluacion(){
        String [] tipo = {"Presencial", "Online"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal,tipo);
        tipoEvaActSp.setAdapter(adapter);
    }

    private void spinnercuestio(){
        listCuetAct.add("Seleccione Cuestionario");
        for (int i = 0; i< cuestListAct.size(); i++){
            listCuetAct.add(cuestListAct.get(i).getNombreCuestionario());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal, listCuetAct);
        cuesEvaActSp.setAdapter(adapter);
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
        fechEvaAct1.setText(String.format("%s/%s/%s",day,month,year));
    }
}