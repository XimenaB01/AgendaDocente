package com.utpl.agendadocente.ui.tarea.crear_tarea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TareaCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static ITarea.TareaCrearListener tareaCrearListener;
    private static Integer idParalelo;
    private ITarea.TareaCrearListener listener;
    private Button btnFechaEn;
    private Button btnParaleloA;
    private TextInputEditText nomTarea;
    private TextInputEditText descTarea;
    private TextInputEditText obsTarea;
    private RecyclerView recyclerViewTar;

    private String nomTar = "";
    private String desTar = "";
    private String obsTar = "";
    private String fecEntTar = "";
    private String estadoTar = "";

    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private List<Asignatura> listaAsignaturas = operacionesAsignatura.listarAsignatura();
    private List<Paralelo> listaParalelos = operacionesParalelo.listarParalelo();
    private List<String> paralalosAsignados = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();

    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();

    public TareaCrearActivity(){
        //required constructor
    }

    public static TareaCrearActivity newInstance(String title, ITarea.TareaCrearListener listener, Integer id){
        tareaCrearListener = listener;
        idParalelo = id;
        TareaCrearActivity tarCreAct = new TareaCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        tarCreAct.setArguments(bundle);

        tarCreAct.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return tarCreAct;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            if (tareaCrearListener != context){
                listener = (ITarea.TareaCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_tarea,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarT);
        nomTarea = view.findViewById(R.id.nomTar);
        descTarea = view.findViewById(R.id.desTar);
        obsTarea = view.findViewById(R.id.obsTar);
        btnFechaEn = view.findViewById(R.id.btnFechEn);
        btnParaleloA = view.findViewById(R.id.paraleloAsigTar);
        recyclerViewTar = view.findViewById(R.id.paralelosAsignadosTar);

        evaluacionCrearActivity.llenarRecycleView(recyclerViewTar, getContext(), paralalosAsignados);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        btnFechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(TareaCrearActivity.this,22);
                dialogDatePicker.setCancelable(false);
                dialogDatePicker.show(getParentFragmentManager(), Utilidades.CREAR);
            }
        });

        btnParaleloA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paralalosAsignados = evaluacionCrearActivity.obtenerParalelos(paralalosAsignados, listaParalelos, listaAsignaturas, getContext(), recyclerViewTar);
            }
        });

        visible();

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
                nomTar = Objects.requireNonNull(nomTarea.getText()).toString();
                desTar = Objects.requireNonNull(descTarea.getText()).toString();
                obsTar = Objects.requireNonNull(obsTarea.getText()).toString();
                fecEntTar = btnFechaEn.getText().toString();
                estadoTar = "Sin Enviar";

                if (btnParaleloA.getVisibility()==View.GONE && recyclerViewTar.getVisibility()==View.GONE){
                    ids.add(idParalelo);
                } else {
                    ids = evaluacionCrearActivity.obtenerIdsParalelos(paralalosAsignados, listaParalelos, listaAsignaturas);
                }

                guardarTarea();

                return true;
            }
        });
        return view;
    }

    private void guardarTarea() {
        if (!nomTar.isEmpty()){
            for (int i = 0; i<ids.size(); i++){
                Tarea tarea = new Tarea();
                tarea.setNombreTarea(nomTar);
                tarea.setDescripcionTarea(desTar);
                tarea.setFechaTarea(fecEntTar);
                tarea.setObservacionTarea(obsTar);
                tarea.setEstadoTarea(estadoTar);
                tarea.setParaleloId(ids.get(i));

                long insercion = operacionesTarea.insertarTarea(tarea);
                if (insercion > 0){
                    int inser = (int)insercion;
                    tarea.setIdTarea(inser);
                    if (tareaCrearListener != null){
                        tareaCrearListener.onCrearTarea(tarea);
                    }
                    else {
                        listener.onCrearTarea(tarea);
                    }
                    dismiss();
                }

            }

        }else{
            Toast.makeText(getContext(), "Agregar un nombre",Toast.LENGTH_LONG).show();
        }
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
        btnFechaEn.setText(fecha);
    }

    private void visible(){
        if (idParalelo != null) {
            btnParaleloA.setVisibility(View.GONE);
            recyclerViewTar.setVisibility(View.GONE);
        }else {
            btnParaleloA.setVisibility(View.VISIBLE);
            recyclerViewTar.setVisibility(View.VISIBLE);
        }
    }
}
