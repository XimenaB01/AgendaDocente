package com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Features.Cuestionario.CrearCuestionario.CuestionarioCrearActivity;
import com.utpl.agendadocente.Features.Cuestionario.CrearCuestionario.CuestionarioCrearListener;
import com.utpl.agendadocente.Features.Periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener, CuestionarioCrearListener{

    private static EvaluacionCrearListener evaluacionCrearListener;
    private EvaluacionCrearListener listener;
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private TextInputEditText nomE, obsE;
    private Button btnFechaEva;
    private Spinner tipoE, cuest;
    private RadioButton rb1BimE, rb2BimE;

    private String nombEva = "";
    private String tipoEva = "";
    private int idCuestEva = -1;
    private String bimEva = "";
    private String fechEva = "";
    private String obserEva = "";
    private List<Cuestionario> cuestList = operacionesCuestionario.ListarCuest();
    private ArrayList<String> listCuet = new ArrayList<>();
    private ArrayAdapter<String> adapter ;



    public EvaluacionCrearActivity (){}

    public static EvaluacionCrearActivity newInstance(String Title, EvaluacionCrearListener listener){
        evaluacionCrearListener = listener;
        EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",Title);
        evaluacionCrearActivity.setArguments(bundle);

        evaluacionCrearActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return evaluacionCrearActivity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (evaluacionCrearListener != context){
                listener = (EvaluacionCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements DocenteCreateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crear_evaluacion,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarE);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        nomE = view.findViewById(R.id.textNomE);
        tipoE = view.findViewById(R.id.spinnerTipo);
        cuest = view.findViewById(R.id.spinnerEva);
        rb1BimE = view.findViewById(R.id.rb1B);
        rb2BimE = view.findViewById(R.id.rb2B);
        obsE = view.findViewById(R.id.textObsEva);
        btnFechaEva = view.findViewById(R.id.btnfecE);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.FABQNew);

        spinners();
        spinnercuestio();

        btnFechaEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(EvaluacionCrearActivity.this,22);
                dialogDatePicker.setCancelable(false);
                if (getFragmentManager() != null) {
                    dialogDatePicker.show(getFragmentManager(),utilidades.CREAR);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearNuevoCuestionario();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                nombEva = Objects.requireNonNull(nomE.getText()).toString();
                tipoEva = tipoE.getSelectedItem().toString();
                String cuet = cuest.getSelectedItem().toString();
                for (int i = 0; i < cuestList.size(); i++){
                    if (cuestList.get(i).getNombreCuestionario().equals(cuet)){
                        idCuestEva = cuestList.get(i).getId_cuestionario();
                    }
                }

                if (rb1BimE.isChecked()){
                    bimEva = rb1BimE.getText().toString();
                }else if (rb2BimE.isChecked()){
                    bimEva = rb2BimE.getText().toString();
                }
                fechEva = btnFechaEva.getText().toString();
                obserEva = Objects.requireNonNull(obsE.getText()).toString();

                Evaluacion eva = new Evaluacion();
                if (!nombEva.isEmpty()){
                    eva.setNombreEvaluacion(nombEva);
                    eva.setTipo(tipoEva);
                    eva.setBimestre(bimEva);
                    eva.setFechaEvaluacion(fechEva);
                    eva.setObservacion(obserEva);
                    eva.setCuestionarioID(idCuestEva);

                    long insercion = operacionesEvaluacion.InsertarEva(eva);
                    if (insercion > 0 ){
                        int inser = (int)insercion;
                        eva.setId_evaluacion(inser);
                        if (evaluacionCrearListener != null){
                            evaluacionCrearListener.onCrearEvaluacion(eva);
                        }else  {
                            listener.onCrearEvaluacion(eva);
                        }

                        dismiss();
                    }
                }else{
                    Toast.makeText(getContext(),"Agregar un nombre",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        return view;
    }

    private void crearNuevoCuestionario() {
        CuestionarioCrearActivity crearCuestionario = CuestionarioCrearActivity.newInstance("Nuevo Cuestionario", null);
        crearCuestionario.setTargetFragment(EvaluacionCrearActivity.this,22);
        crearCuestionario.setCancelable(false);
        if (getFragmentManager() != null) {
            crearCuestionario.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void spinners(){
        String [] tipo = {"Presencial", "Online"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.spinner_item_style_pesonal,tipo);
        tipoE.setAdapter(adapter);
    }

    private void spinnercuestio(){
        llenarListaAdapter();
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.spinner_item_style_pesonal, listCuet);
        cuest.setAdapter(adapter);
    }

    private void llenarListaAdapter(){
        listCuet.add("Seleccione Cuestionario");
        for (int i = 0; i< cuestList.size(); i++){
            listCuet.add(cuestList.get(i).getNombreCuestionario());
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
        btnFechaEva.setText(String.format("%s/%s/%s",day,month,year));
    }

    @Override
    public void onCrearCuestionario(Cuestionario cuestionario) {
        adapter.clear();
        cuestList.add(cuestionario);
        llenarListaAdapter();
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.spinner_item_style_pesonal, listCuet);
        cuest.setAdapter(adapter);
    }
}
