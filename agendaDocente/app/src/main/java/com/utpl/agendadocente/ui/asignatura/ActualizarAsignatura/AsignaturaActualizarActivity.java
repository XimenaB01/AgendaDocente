package com.utpl.agendadocente.ui.asignatura.ActualizarAsignatura;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
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

public class AsignaturaActualizarActivity extends DialogFragment implements AdapterView.OnItemSelectedListener{
    private static Integer idAsignatura;
    private static int asignaturaItemPosition;
    private static ActualizarAsignaturaListener actualizarAsignaturaListener;

    private Asignatura asignatura;

    private TextInputEditText nomAct, creAct, horAct , desAct, nilAct;
    private Spinner area, carrerra;

    private String nombreAsig = "";
    private String descrpcionAsig = "";
    private String nivelAsig = "";
    private String carreraAsig = "";
    private String creditoAsig = "";
    private String areaAsig = "";
    private String horaAsig = "";

    private OperacionesAsignatura operacionesAsignatura;

    public AsignaturaActualizarActivity(){

    }

    public static AsignaturaActualizarActivity newInstance(Integer id, int position, ActualizarAsignaturaListener listener){
        idAsignatura = id;
        asignaturaItemPosition = position;
        actualizarAsignaturaListener = listener;
        AsignaturaActualizarActivity actualizarAsignatura = new AsignaturaActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Actualizar Asignatura");
        actualizarAsignatura.setArguments(bundle);

        actualizarAsignatura.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        return actualizarAsignatura;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_actualizar_asignatura,container,false);

        operacionesAsignatura = new OperacionesAsignatura(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarAsA);
        nomAct = view.findViewById(R.id.nomAsigAct);
        creAct = view.findViewById(R.id.credAsgiAct);
        horAct = view.findViewById(R.id.horAsigAct);
        desAct = view.findViewById(R.id.desAsigAct);
        nilAct = view.findViewById(R.id.nivAsigAct);
        area = view.findViewById(R.id.areaAsgiAct);
        carrerra = view.findViewById(R.id.carrAsigAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        area.setAdapter(areas);
        area.setOnItemSelectedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        asignatura = operacionesAsignatura.obtenerAsignatura(idAsignatura);
        if (asignatura!=null){

            nomAct.setText(asignatura.getNombreAsignatura());
            creAct.setText(asignatura.getCreditos());
            horAct.setText(asignatura.getHorario());
            desAct.setText(asignatura.getDescripcionAsigantura());
            nilAct.setText(asignatura.getNivel());

            final String areaspiner = asignatura.getArea();
            area.setSelection(obtenerPositionItem(area,areaspiner));

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    nombreAsig = Objects.requireNonNull(nomAct.getText()).toString();
                    descrpcionAsig = Objects.requireNonNull(desAct.getText()).toString();
                    nivelAsig = Objects.requireNonNull(nilAct.getText()).toString();
                    carreraAsig = carrerra.getSelectedItem().toString();
                    creditoAsig = Objects.requireNonNull(creAct.getText()).toString();
                    areaAsig = area.getSelectedItem().toString();
                    horaAsig = Objects.requireNonNull(horAct.getText()).toString();

                    if (!nombreAsig.isEmpty()){
                        if (!nivelAsig.isEmpty()){
                            int nivel = Integer.parseInt(nivelAsig);
                            if (nivel > 0 && nivel <= 10){
                                if (!creditoAsig.isEmpty()){
                                    int cred = Integer.parseInt(creditoAsig);
                                    if (cred > 0 && cred <= 6){
                                        ActualizarAsignatura();
                                    }else {
                                        Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    ActualizarAsignatura();
                                }
                            }else {
                                Toast.makeText(getContext(),"El nivel debe estar entre 1-10",Toast.LENGTH_LONG).show();
                            }
                        }else if (!creditoAsig.isEmpty()){
                            int cred = Integer.parseInt(creditoAsig);
                            if (cred > 0 && cred <= 6){
                                ActualizarAsignatura();
                            }else {
                                Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            ActualizarAsignatura();
                        }
                    }else {
                        Toast.makeText(getContext(),"Ingrese el nombre de la asignatura",Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
            });

        }
        return view;
    }

    private int obtenerPositionItem(Spinner spinner, String cadena) {
        int posicion = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(cadena)){
                posicion = i;
            }
        }
        return posicion;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        int [] carreras = {R.array.tecnica,R.array.administrativa,R.array.sociohumanistica,R.array.biologica};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),carreras[position],R.layout.spinner_item_style_pesonal);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        carrerra.setAdapter(adapter);

        final String carreraspiner = asignatura.getCarrera();
        carrerra.setSelection(obtenerPositionItem(carrerra,carreraspiner));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void ActualizarAsignatura (){
        asignatura.setNombreAsignatura(nombreAsig);
        asignatura.setArea(areaAsig);
        asignatura.setDescripcionAsigantura(descrpcionAsig);
        asignatura.setNivel(nivelAsig);
        asignatura.setCarrera(carreraAsig);
        asignatura.setCreditos(creditoAsig);
        asignatura.setArea(areaAsig);
        asignatura.setHorario(horaAsig);

        long insercion = operacionesAsignatura.ModificarAsig(asignatura);

        if (insercion>0){
            actualizarAsignaturaListener.onActualizarAsignatura(asignatura,asignaturaItemPosition);
            Objects.requireNonNull(getDialog()).dismiss();
        }
    }
}
