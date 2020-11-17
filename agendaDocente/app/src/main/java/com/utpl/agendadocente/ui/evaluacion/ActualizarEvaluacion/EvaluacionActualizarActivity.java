package com.utpl.agendadocente.ui.evaluacion.ActualizarEvaluacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.tarea.ActualizarTarea.TareaActualizarActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionActualizarActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static long idEvaluacion;
    private static int evaluacionItemPosition;
    private static ActualizarEvaluacionListener actualizarEvaluacionListener;

    private Evaluacion evaluacion;

    private Button fechEvaAct1;
    private Button tipoEvaActButton;
    private TextInputEditText txtnomEvaAct, txtObsEvaAct;
    private Spinner cuesEvaActSp;
    private RadioButton rb1BimEAct, rb2BimEAct;
    private TextView parAsignado;

    private String nomEvaAct = "";
    private String tipoEvaAct = "";
    private String fechEvaAct = "";
    private int cuesEvaAct = 0;
    private String bimEvaAct = "";
    private String obsEvaAct = "";
    private Integer IdParalelo = null;

    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private List<Cuestionario> cuestListAct = operacionesCuestionario.ListarCuest();
    private ArrayList<String> listCuetAct = new ArrayList<>();
    private List<String> paralalosAsignados = new ArrayList<>();

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
        cuesEvaActSp = view.findViewById(R.id.spinnerEvaAct);
        fechEvaAct1 = view.findViewById(R.id.btnfecEvAct);
        rb1BimEAct = view.findViewById(R.id.rb1BAct);
        rb2BimEAct = view.findViewById(R.id.rb2BAct);
        txtObsEvaAct = view.findViewById(R.id.textObsEvaAct);
        tipoEvaActButton = view.findViewById(R.id.tipoEvAct);
        parAsignado = view.findViewById(R.id.paraleloAsignadoAct);
        Button btnParalelo = view.findViewById(R.id.paraleloAsigEvaAct);

        evaluacion = operacionesEvaluacion.obtenerEva(idEvaluacion);

        if (evaluacion != null) {

            final String Tipo = evaluacion.getTipo();
            tipoEvaActButton.setText(Tipo);
            tipoEvaActButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
                    evaluacionCrearActivity.obtenerTipoEvaluacion(Tipo, getContext());
                }
            });

            obtenerspinnercuestio();

            for (int i = 0; i < cuestListAct.size(); i++){
                if (evaluacion.getCuestionarioID().equals(cuestListAct.get(i).getId_cuestionario())){
                    cuesEvaActSp.setSelection(TareaActualizarActivity.obtenerPositionItem(cuesEvaActSp,cuestListAct.get(i).getNombreCuestionario()));
                }
            }

            txtnomEvaAct.setText(evaluacion.getNombreEvaluacion());
            fechEvaAct1.setText(evaluacion.getFechaEvaluacion());
            txtObsEvaAct.setText(evaluacion.getObservacion());

            if (evaluacion.getParaleloID() != null){
                obtenerParaleloAsignado(evaluacion.getParaleloID());
            }

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
                    dialogDatePicker.show(getParentFragmentManager(),utilidades.CREAR);
                }
            });

            btnParalelo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (paralalosAsignados.size() == 0){
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
                    nomEvaAct = Objects.requireNonNull(txtnomEvaAct.getText()).toString();
                    tipoEvaAct = tipoEvaActButton.getText().toString();
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

                    obtenerParalelos(parAsignado.getText().toString());

                    obsEvaAct = Objects.requireNonNull(txtObsEvaAct.getText()).toString();

                    if (!nomEvaAct.isEmpty()){
                        evaluacion.setNombreEvaluacion(nomEvaAct);
                        evaluacion.setTipo(tipoEvaAct);
                        evaluacion.setBimestre(bimEvaAct);
                        evaluacion.setFechaEvaluacion(fechEvaAct);
                        evaluacion.setObservacion(obsEvaAct);
                        evaluacion.setCuestionarioID(cuesEvaAct);
                        evaluacion.setParaleloID(IdParalelo);

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

    private void obtenerParalelos(String itemAsignado) {
        List<Paralelo> paraleloList = operacionesParalelo.ListarPar();
        List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();

        for (int i = 0; i < paraleloList.size(); i++){
            for (int j = 0; j < asignaturaList.size(); j++){
                if (paraleloList.get(i).getAsignaturaID().equals(asignaturaList.get(j).getId_asignatura())){
                    paralalosAsignados.add(asignaturaList.get(j).getNombreAsignatura()+" - "+paraleloList.get(i).getNombreParalelo());
                    if (!itemAsignado.isEmpty()){
                        if (itemAsignado.equals(asignaturaList.get(j).getNombreAsignatura()+" - "+paraleloList.get(i).getNombreParalelo())){
                            IdParalelo = paraleloList.get(i).getId_paralelo();
                        }
                    }
                }
            }
        }
    }

    private void obtenerParaleloAsignado(Integer paraleloID) {

        Paralelo paralelo = operacionesParalelo.obtenerPar(paraleloID);
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());

        String paraleloAsignado = asignatura.getNombreAsignatura() + " - " + paralelo.getNombreParalelo();

        parAsignado.setText(paraleloAsignado);
    }

    private void asignarParalelo(List<String> Lista, String PA){

        final String [] par = new String[Lista.size()];
        int posicion = -1;

        for (int i = 0; i < Lista.size(); i++){
            par[i] = Lista.get(i);
            if (PA.equals(par[i])){
                posicion = i;
            }
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Tipo de Evaluación");
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