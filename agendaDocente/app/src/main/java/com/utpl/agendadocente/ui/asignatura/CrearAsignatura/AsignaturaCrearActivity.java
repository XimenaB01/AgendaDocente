package com.utpl.agendadocente.ui.asignatura.CrearAsignatura;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Objects;


public class AsignaturaCrearActivity extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static AsignaturaCreateListener asignaturaCreateListener;
    private AsignaturaCreateListener listener;

    private TextInputEditText nombre, creditos, horario, nivel, descripcion;
    private Spinner spinnerAreas, spinnerCarrera;

    private String nombreA = "";
    private String creditosA = "0";
    private String horasA = "0";
    private String nivelA = "0";
    private String descripcionA = "";
    private String areaA = "";
    private String carreraA = "";


    public AsignaturaCrearActivity(){}

    public static AsignaturaCrearActivity newInstance(String title, AsignaturaCreateListener listener){
        if (listener != null){
            asignaturaCreateListener = listener;
        }
        AsignaturaCrearActivity crearAsig = new AsignaturaCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        crearAsig.setArguments(bundle);

        crearAsig.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        return crearAsig;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (asignaturaCreateListener != context){
                listener = (AsignaturaCreateListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_asignatura, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarAs);
        nombre = view.findViewById(R.id.nomAsig);
        spinnerAreas = view.findViewById(R.id.sparea);
        spinnerCarrera = view.findViewById(R.id.spcarrera);
        creditos =  view.findViewById(R.id.credAsig);
        horario =  view.findViewById(R.id.horAsig);
        nivel =  view.findViewById(R.id.nivAsig);
        descripcion = view.findViewById(R.id.desAsig);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerAreas.setAdapter(areas);
        spinnerAreas.setOnItemSelectedListener(this);

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
                nombreA =  Objects.requireNonNull(nombre.getText()).toString();
                areaA = spinnerAreas.getSelectedItem().toString();
                creditosA = Objects.requireNonNull(creditos.getText()).toString();
                horasA = Objects.requireNonNull(horario.getText()).toString();
                nivelA = Objects.requireNonNull(nivel.getText()).toString();
                descripcionA = Objects.requireNonNull(descripcion.getText()).toString();
                carreraA = spinnerCarrera.getSelectedItem().toString();

                if(!nombreA.isEmpty()){
                    if (!nivelA.isEmpty()){
                        int nivel = Integer.parseInt(nivelA);
                        if (nivel > 0 && nivel <= 10){
                            if (!creditosA.isEmpty()){
                                int cred = Integer.parseInt(creditosA);
                                if (cred > 0 && cred <= 6){
                                    guardarAsignatura();
                                }else {
                                    Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                guardarAsignatura();
                            }
                        }else {
                            Toast.makeText(getContext(),"El nivel debe estar entre 1-10",Toast.LENGTH_LONG).show();
                        }
                    }else if (!creditosA.isEmpty()){
                        int cred = Integer.parseInt(creditosA);
                        if (cred > 0 && cred <= 6){
                            guardarAsignatura();
                        }else {
                            Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        guardarAsignatura();
                    }
                }else {
                    Toast.makeText(getContext(),"Agregar el nombre de la Asignatura",Toast.LENGTH_LONG).show();
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
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int [] carreras = {R.array.tecnica,R.array.administrativa,R.array.sociohumanistica,R.array.biologica};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),carreras[position],R.layout.spinner_item_style_pesonal);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerCarrera.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void guardarAsignatura(){

        Asignatura asig = new Asignatura();

        asig.setNombreAsignatura(nombreA);
        asig.setArea(areaA);
        asig.setCarrera(carreraA);
        asig.setCreditos(creditosA);
        asig.setNivel(nivelA);
        asig.setDescripcionAsigantura(descripcionA);
        asig.setHorario(horasA);

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

        long insercion = operacionesAsignatura.InsertarAsignatura(asig);

        if (insercion > 0){
            int inser = (int)insercion;
            asig.setId_asignatura(inser);
            if (asignaturaCreateListener != null){
                asignaturaCreateListener.onCrearAsignatura(asig);
            }else {
                listener.onCrearAsignatura(asig);
            }
            Objects.requireNonNull(getDialog()).dismiss();
        }
    }
}
