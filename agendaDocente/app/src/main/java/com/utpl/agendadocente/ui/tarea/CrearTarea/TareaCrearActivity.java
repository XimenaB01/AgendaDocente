package com.utpl.agendadocente.ui.tarea.CrearTarea;

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
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Paralelo;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TareaCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static TareaCrearListener tareaCrearListener;
    private static Integer IdParalelo;
    private TareaCrearListener listener;
    private Button btnFechaEn, btnParaleloA;
    private TextInputEditText nomTarea, descTarea, obsTarea;
    private RecyclerView recyclerViewTar;

    private String nomTar = "";
    private String desTar = "";
    private String obsTar = "";
    private String fecEntTar = "";
    private String estadoTar = "";

    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private List<Asignatura> ListaAsignaturas = operacionesAsignatura.ListarAsig();
    private List<Paralelo> ListaParalelos = operacionesParalelo.ListarPar();
    private List<String> paralalosAsignados = new ArrayList<>();

    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();

    public TareaCrearActivity(){}

    public static TareaCrearActivity newInstance(String Title, TareaCrearListener listener, Integer Id){
        tareaCrearListener = listener;
        IdParalelo = Id;
        TareaCrearActivity tarCreAct = new TareaCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",Title);
        tarCreAct.setArguments(bundle);

        tarCreAct.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return tarCreAct;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            if (tareaCrearListener != context){
                listener = (TareaCrearListener) getTargetFragment();
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
        final Button estados = view.findViewById(R.id.estadosTarea);
        btnParaleloA = view.findViewById(R.id.paraleloAsigTar);
        recyclerViewTar = view.findViewById(R.id.paralelosAsignadosTar);

        evaluacionCrearActivity.llenarRecycleView(recyclerViewTar, getContext(), paralalosAsignados);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        btnFechaEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(TareaCrearActivity.this,22);
                dialogDatePicker.setCancelable(false);
                dialogDatePicker.show(getParentFragmentManager(),utilidades.CREAR);
            }
        });

        estados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuEstados = new PopupMenu(getContext(),estados);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados,popupMenuEstados.getMenu());

                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        estados.setText(menuItem.getTitle());
                        return true;
                    }
                });

                popupMenuEstados.show();
            }
        });

        btnParaleloA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
                paralalosAsignados = evaluacionCrearActivity.obtenerParalelos(paralalosAsignados, ListaParalelos, ListaAsignaturas, getContext(), recyclerViewTar);
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
                if (!estados.getText().toString().equals("Estado")){
                    estadoTar = estados.getText().toString();
                }else {
                    estadoTar = "Sin Estado";
                }

                List<Integer> Ids = new ArrayList<>();
                if (btnParaleloA.getVisibility()==View.GONE && recyclerViewTar.getVisibility()==View.GONE){
                    Ids.add(IdParalelo);
                } else {
                    Ids = evaluacionCrearActivity.obtenerIdsParalelos(paralalosAsignados, ListaParalelos, ListaAsignaturas);
                }

                if (!nomTar.isEmpty()){
                    for (int i = 0; i<Ids.size(); i++){
                        Tarea tarea = new Tarea();
                        tarea.setNombreTarea(nomTar);
                        tarea.setDescripcionTarea(desTar);
                        tarea.setFechaTarea(fecEntTar);
                        tarea.setObservacionTarea(obsTar);
                        tarea.setEstadoTarea(estadoTar);
                        tarea.setParaleloId(Ids.get(i));

                        long insercion = operacionesTarea.InsertarTar(tarea);
                        if (insercion > 0){
                            int inser = (int)insercion;
                            tarea.setId_tarea(inser);
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
                return true;
            }
        });
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
        btnFechaEn.setText(fecha);
    }

    private void visible(){
        if (IdParalelo != null) {
            btnParaleloA.setVisibility(View.GONE);
            recyclerViewTar.setVisibility(View.GONE);
        }else {
            btnParaleloA.setVisibility(View.VISIBLE);
            recyclerViewTar.setVisibility(View.VISIBLE);
        }
    }
}
