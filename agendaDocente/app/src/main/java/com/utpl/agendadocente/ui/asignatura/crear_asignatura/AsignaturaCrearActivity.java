package com.utpl.agendadocente.ui.asignatura.crear_asignatura;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.utpl.agendadocente.decorador.imple.AsignaturaListenerNormal;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.decorador.DescripcionDecorador;
import com.utpl.agendadocente.decorador.DuracionDecorador;
import com.utpl.agendadocente.decorador.NivelDecorador;
import com.utpl.agendadocente.decorador.TemasDecorador;
import com.utpl.agendadocente.ui.asignatura.IAsignatura;

import java.util.Objects;

import static com.utpl.agendadocente.decorador.DescripcionDecorador.recibirDescripcion;
import static com.utpl.agendadocente.decorador.DuracionDecorador.recibirDuracion;
import static com.utpl.agendadocente.decorador.NivelDecorador.recibirNivel;
import static com.utpl.agendadocente.decorador.TemasDecorador.recibirTemas;


public class AsignaturaCrearActivity extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static IAsignatura.AsignaturaCreateListener asignaturaCreateListener;
    private IAsignatura.AsignaturaCreateListener listener;
    private Asignatura asig = new Asignatura();

    private TextInputEditText nombre;
    private TextInputEditText creditos;
    private TextInputEditText horario;
    private TextInputEditText nivel;
    private TextInputEditText descripcion;
    private TextInputEditText temas;
    private Spinner spinnerAreas;
    private Spinner spinnerCarrera;
    private CheckBox checkBoxDescripcion;
    private CheckBox checkBoxNivel;
    private CheckBox checkBoxTemas;
    private CheckBox checkBoxDuracion;
    private TextInputLayout textInputLayoutDesc;
    private TextInputLayout textInputLayoutNiv;
    private TextInputLayout textInputLayoutTem;
    private Button buttonDuracion;

    private String nombreA = "";
    private String creditosA = "";
    private String horasA = "";
    private String nivelA = "";
    private String descripcionA = "";
    private String areaA = "";
    private String carreraA = "";
    private String temasA = "";
    private String duracion = "";

    public AsignaturaCrearActivity(){
        //Required construtor
    }

    public static AsignaturaCrearActivity newInstance(String title, IAsignatura.AsignaturaCreateListener listener){
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
                listener = (IAsignatura.AsignaturaCreateListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements AsignaturaCreateListener");
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
        temas = view.findViewById(R.id.temAsig);
        buttonDuracion = view.findViewById(R.id.durAsig);
        checkBoxDescripcion = view.findViewById(R.id.agregarDescripcion);
        checkBoxNivel = view.findViewById(R.id.agregarNivel);
        checkBoxTemas = view.findViewById(R.id.agregarTemas);
        checkBoxDuracion = view.findViewById(R.id.agregarDuracion);
        textInputLayoutDesc = view.findViewById(R.id.outlinedTextField2);
        textInputLayoutNiv = view.findViewById(R.id.outlinedTextField3);
        textInputLayoutTem = view.findViewById(R.id.outlinedTextField6);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerAreas.setAdapter(areas);
        spinnerAreas.setOnItemSelectedListener(this);

        buttonDuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuEstados = new PopupMenu(getContext(), buttonDuracion);
                popupMenuEstados.getMenuInflater().inflate(R.menu.menu_duracion,popupMenuEstados.getMenu());
                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        buttonDuracion.setText(menuItem.getTitle());
                        return true;
                    }
                });
                popupMenuEstados.show();
            }
        });

        agregarComponete();

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
                carreraA = spinnerCarrera.getSelectedItem().toString();

                if (checkBoxDescripcion.isChecked()){
                    descripcionA = Objects.requireNonNull(descripcion.getText()).toString();
                }
                if (checkBoxNivel.isChecked()){
                    nivelA = Objects.requireNonNull(nivel.getText()).toString();
                }
                if (checkBoxTemas.isChecked()){
                    temasA = Objects.requireNonNull(temas.getText()).toString();
                }
                if (checkBoxDuracion.isChecked()){
                    duracion = buttonDuracion.getText().toString();
                }

                guardarAsignatura(validarCampos());

                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
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
        //Ignore method
    }

    private void agregarComponete(){

        checkBoxDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxDescripcion.isChecked()){
                    textInputLayoutDesc.setVisibility(View.VISIBLE);
                }else {
                    textInputLayoutDesc.setVisibility(View.GONE);
                }
            }
        });

        checkBoxNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxNivel.isChecked()){
                    textInputLayoutNiv.setVisibility(View.VISIBLE);
                }else {
                    textInputLayoutNiv.setVisibility(View.GONE);
                }
            }
        });

        checkBoxTemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxTemas.isChecked()){
                    textInputLayoutTem.setVisibility(View.VISIBLE);
                }else {
                    textInputLayoutTem.setVisibility(View.GONE);
                }
            }
        });

        checkBoxDuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxDuracion.isChecked()){
                    buttonDuracion.setVisibility(View.VISIBLE);
                }else {
                    buttonDuracion.setVisibility(View.GONE);
                }
            }
        });
    }

    private void guardarAsignatura(Asignatura asig){

        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener iasignatura = new AsignaturaListenerNormal();

        if (checkBoxDescripcion.isChecked()) {
            asignaturaNueva = getDecoradorDescripcionForAsignatura(iasignatura, asig);
        } else if (checkBoxNivel.isChecked()){
            asignaturaNueva = getDecoradorNivelForAsignatura(iasignatura, asig);
        }else if (checkBoxTemas.isChecked()){
            asignaturaNueva = getDecoradorTemasForAsignatura(iasignatura,asig);
        }else if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDecoradorDuracionForAsignatura(iasignatura, asig);
        }else {
            asignaturaNueva = iasignatura.agregarAsignatura(asig, getContext());
        }

        if (asignaturaCreateListener != null){
            asignaturaCreateListener.onCrearAsignatura(asignaturaNueva);
        }else {
            listener.onCrearAsignatura(asignaturaNueva);
        }
    }

    private Asignatura validarCampos(){

        if(!nombreA.isEmpty()){

            asig.setNombreAsignatura(nombreA);
            asig.setArea(areaA);
            asig.setCarrera(carreraA);

            validarCreditos();
            validarHoras();
            validarNivel();
            validarDescripcion();
            validarDuracion();
            validarTemas();

            Objects.requireNonNull(getDialog()).dismiss();

        }else {
            Toast.makeText(getContext(),"Agregar el nombre de la Asignatura",Toast.LENGTH_LONG).show();
        }

        return asig;
    }

    private void validarTemas() {
        if (!temasA.isEmpty()){
            recibirTemas(temasA);
        }
    }

    private void validarDuracion() {
        if (!duracion.isEmpty()){
            recibirDuracion(duracion);
        }
    }

    private void validarDescripcion() {
        if (!descripcionA.isEmpty()){
            recibirDescripcion(descripcionA);
        }
    }

    private void validarNivel() {
        if (!nivelA.isEmpty()){
            recibirNivel(nivelA);
        }
    }

    private void validarHoras() {
        if (!horasA.isEmpty()){
            int hor = Integer.parseInt(horasA);
            if (hor > 0 && hor <= 5){
                asig.setHorario(horasA);
            }else {
                Toast.makeText(getContext(),"El número de horas no debe ser mayor a 5",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarCreditos() {

        if (!creditosA.isEmpty()){
            int cred = Integer.parseInt(creditosA);
            if (cred > 0 && cred <= 6){
                asig.setCreditos(creditosA);
            }else {
                Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
            }
        }
    }

    private Asignatura getDecoradorDescripcionForAsignatura(IAsignatura.AsignaturaListener iasignatura, Asignatura asig){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerDescripcion = new DescripcionDecorador(iasignatura);
        if (checkBoxNivel.isChecked()){
            asignaturaNueva = getDecoradorNivelForAsignatura(asignaturaListenerDescripcion, asig);
        }else if (checkBoxTemas.isChecked()){
            asignaturaNueva = getDecoradorTemasForAsignatura(asignaturaListenerDescripcion, asig);
        }else if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDecoradorDuracionForAsignatura(asignaturaListenerDescripcion, asig);
        }else {
            asignaturaNueva = asignaturaListenerDescripcion.agregarAsignatura(asig, getContext());
        }
        return asignaturaNueva;
    }

    private Asignatura getDecoradorNivelForAsignatura(IAsignatura.AsignaturaListener iasignatura, Asignatura asig){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerNivel = new NivelDecorador(iasignatura);
        if (checkBoxTemas.isChecked()){
            asignaturaNueva = getDecoradorTemasForAsignatura(asignaturaListenerNivel,asig);
        }else if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDecoradorDuracionForAsignatura(asignaturaListenerNivel, asig);
        }else {
            asignaturaNueva = asignaturaListenerNivel.agregarAsignatura(asig,getContext());
        }
        return asignaturaNueva;
    }

    private Asignatura getDecoradorTemasForAsignatura(IAsignatura.AsignaturaListener iasignatura, Asignatura asig){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerTemas = new TemasDecorador(iasignatura);
        if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDecoradorDuracionForAsignatura(asignaturaListenerTemas, asig);
        }else {
            asignaturaNueva = asignaturaListenerTemas.agregarAsignatura(asig,getContext());
        }
        return asignaturaNueva;
    }

    private Asignatura getDecoradorDuracionForAsignatura(IAsignatura.AsignaturaListener iasignatura, Asignatura asig){
        IAsignatura.AsignaturaListener asignaturaListenerDuracion = new DuracionDecorador(iasignatura);
        return asignaturaListenerDuracion.agregarAsignatura(asig,getContext());
    }
}
