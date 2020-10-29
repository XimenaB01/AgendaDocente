package com.utpl.agendadocente.Features.Tarea.CrearTarea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TareaCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener {

    private static TareaCrearListener tareaCrearListener;
    private TareaCrearListener listener;
    private Button btnFechaEn;
    private TextInputEditText nomTarea, descTarea, obsTarea;
    private Spinner parTarea;

    private String nomTar = "";
    private String desTar = "";
    private String obsTar = "";
    private String fecEntTar = "";
    private String estadoTar = "";
    private String paraleloTar = "";

    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private List<Asignatura> ListaAsignaturas = operacionesAsignatura.ListarAsig();
    private List<Paralelo> ListaParalelos = operacionesParalelo.ListarPar();
    private List<String> Paralelos = new ArrayList<>();

    public TareaCrearActivity(){}

    public static TareaCrearActivity newInstance(String Title, TareaCrearListener listener){
        tareaCrearListener = listener;
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
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements DocenteCreateListener");
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
        parTarea = view.findViewById(R.id.paraleloTarea);
        final Button estados = view.findViewById(R.id.estadosTarea);

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
                if (getFragmentManager() != null) {
                    dialogDatePicker.show(getFragmentManager(),utilidades.CREAR);
                }
            }
        });

        estados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuEstados = new PopupMenu(getContext(),estados);
                popupMenuEstados.getMenuInflater().inflate(R.menu.estados,popupMenuEstados.getMenu());
                //popupMenuEstados.getMenu().add(Menu.NONE, 1, 1, "Share");
                //popupMenuEstados.getMenu().add(Menu.NONE, 2, 2, "Comment");

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

        ObetnerParalelos();

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

                paraleloTar = parTarea.getSelectedItem().toString();
                String nomParalelo = "";
                Integer IdAsignatura = -1;
                for (int i = 0; i<ListaParalelos.size();i++){
                    for (int j = 0; j<ListaAsignaturas.size(); j++){
                        if (paraleloTar.equals( ListaAsignaturas.get(j).getNombreAsignatura()+ " - " + ListaParalelos.get(i).getNombreParalelo() )){
                            nomParalelo = ListaParalelos.get(i).getNombreParalelo();
                            IdAsignatura = ListaAsignaturas.get(j).getId_asignatura();
                        }
                    }
                }

                Tarea tarea = new Tarea();
                if (!nomTar.isEmpty()){
                    tarea.setNombreTarea(nomTar);
                    tarea.setDescripcionTarea(desTar);
                    tarea.setFechaTarea(fecEntTar);
                    tarea.setObservacionTarea(obsTar);
                    tarea.setEstadoTarea(estadoTar);

                    long insercion = operacionesTarea.InsertarTar(tarea, nomParalelo,IdAsignatura);
                    if (insercion > 0){
                        int inser = (int)insercion;
                        tarea.setId_tarea(inser);
                        if (tareaCrearListener != null){
                            tareaCrearListener.onCrearTarea(tarea);
                        }else {
                            listener.onCrearTarea(tarea);
                        }
                        dismiss();
                    }

                }else{
                    Toast.makeText(getContext(), "Agregar un nombre",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        return view;
    }

    private void ObetnerParalelos() {

        Paralelos.add("Seleccionar Paralelo");
        for (int i = 0; i<ListaParalelos.size();i++){
            for (int j = 0; j<ListaAsignaturas.size(); j++){
                if (ListaParalelos.get(i).getAsignaturaID().equals( ListaAsignaturas.get(j).getId_asignatura() )){
                    Paralelos.add(ListaAsignaturas.get(j).getNombreAsignatura()+ " - " + ListaParalelos.get(i).getNombreParalelo());
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.spinner_item_style_pesonal,Paralelos);
        parTarea.setAdapter(adapter);
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
        btnFechaEn.setText(String.format("%s/%s/%s",day,month,year));
    }
}
