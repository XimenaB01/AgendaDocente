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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.decorador.DescripcionDecorador;
import com.utpl.agendadocente.decorador.DuracionDecorador;
import com.utpl.agendadocente.decorador.NivelDecorador;
import com.utpl.agendadocente.decorador.TemasDecorador;
import com.utpl.agendadocente.intef.IAsignatura;
import com.utpl.agendadocente.intef.impl.AsignaturaNormal;

import java.util.Objects;

import static com.utpl.agendadocente.decorador.DescripcionDecorador.recibirDescripcion;
import static com.utpl.agendadocente.decorador.DuracionDecorador.recibirDuracion;
import static com.utpl.agendadocente.decorador.NivelDecorador.recibirNivel;
import static com.utpl.agendadocente.decorador.TemasDecorador.recibirTemas;


public class AsignaturaCrearActivity extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private static IAsignatura.AsignaturaCreateListener asignaturaCreateListener;
    private IAsignatura.AsignaturaCreateListener listener;

    private TextInputEditText nombre, creditos, horario, nivel, descripcion, ETtemas;
    private Spinner spinnerAreas, spinnerCarrera;
    private CheckBox CBD1, CBD2, CBD3, CBD4;
    private TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    private Button BTduracion;

    private String nombreA = "";
    private String creditosA = "";
    private String horasA = "";
    private String nivelA = "";
    private String descripcionA = "";
    private String areaA = "";
    private String carreraA = "";
    private String temas = "";
    private String duracion = "";

    public AsignaturaCrearActivity(){}

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
        ETtemas = view.findViewById(R.id.temAsig);
        BTduracion = view.findViewById(R.id.durAsig);
        CBD1 = view.findViewById(R.id.agregarDescripcion);
        CBD2 = view.findViewById(R.id.agregarNivel);
        CBD3 = view.findViewById(R.id.agregarTemas);
        CBD4 = view.findViewById(R.id.agregarDuracion);
        textInputLayout1 = view.findViewById(R.id.outlinedTextField2);
        textInputLayout2 = view.findViewById(R.id.outlinedTextField3);
        textInputLayout3 = view.findViewById(R.id.outlinedTextField6);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerAreas.setAdapter(areas);
        spinnerAreas.setOnItemSelectedListener(this);

        BTduracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenuEstados = new PopupMenu(getContext(),BTduracion);
                popupMenuEstados.getMenuInflater().inflate(R.menu.menu_duracion,popupMenuEstados.getMenu());
                popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        BTduracion.setText(menuItem.getTitle());
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

                if (CBD1.isChecked()){
                    descripcionA = Objects.requireNonNull(descripcion.getText()).toString();
                }
                if (CBD2.isChecked()){
                    nivelA = Objects.requireNonNull(nivel.getText()).toString();
                }
                if (CBD3.isChecked()){
                    temas = Objects.requireNonNull(ETtemas.getText()).toString();
                }
                if (CBD4.isChecked()){
                    duracion = BTduracion.getText().toString();
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

    private void agregarComponete(){

        CBD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CBD1.isChecked()){
                    textInputLayout1.setVisibility(View.VISIBLE);
                }else {
                    textInputLayout1.setVisibility(View.GONE);
                }
            }
        });

        CBD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CBD2.isChecked()){
                    textInputLayout2.setVisibility(View.VISIBLE);
                }else {
                    textInputLayout2.setVisibility(View.GONE);
                }
            }
        });

        CBD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CBD3.isChecked()){
                    textInputLayout3.setVisibility(View.VISIBLE);
                }else {
                    textInputLayout3.setVisibility(View.GONE);
                }
            }
        });

        CBD4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CBD4.isChecked()){
                    BTduracion.setVisibility(View.VISIBLE);
                }else {
                    BTduracion.setVisibility(View.GONE);
                }
            }
        });
    }

    private void guardarAsignatura(Asignatura asig){

        Asignatura asignaturaNueva = new Asignatura();
        IAsignatura.asignatura Iasignatura = new AsignaturaNormal();

        if (CBD1.isChecked()) {
            IAsignatura.asignatura asignaturaDescripcion = new DescripcionDecorador(Iasignatura);

            if (CBD2.isChecked()){
                IAsignatura.asignatura asignaturaNivel = new NivelDecorador(asignaturaDescripcion);

                if (CBD3.isChecked()){
                    IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaNivel);

                    if (CBD4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
                    }else {
                        asignaturaNueva = asignaturaTemas.agregarAsignatura(asig,getContext());//
                    }
                }else {
                    if (CBD4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaNivel);
                        asignaturaDuracion.agregarAsignatura(asig,getContext());//
                    }else {
                        asignaturaNueva = asignaturaNivel.agregarAsignatura(asig,getContext());//
                    }
                }

            }else {

                if (CBD3.isChecked()){
                    IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaDescripcion);

                    if (CBD4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
                    }else {
                        asignaturaNueva = asignaturaTemas.agregarAsignatura(asig,getContext());//
                    }
                }else {
                    if (CBD4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaDescripcion);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
                    }else {
                        asignaturaNueva = asignaturaDescripcion.agregarAsignatura(asig,getContext());//
                    }
                }
            }

        } else if (CBD2.isChecked()){
            //agregamos Nivel
            IAsignatura.asignatura asignaturaNivel = new NivelDecorador(Iasignatura);

            if (CBD3.isChecked()){
                IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaNivel);

                if (CBD4.isChecked()){
                    IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                    asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
                }else {
                    asignaturaNueva = asignaturaTemas.agregarAsignatura(asig,getContext());//
                }
            }else {
                if (CBD4.isChecked()){
                    IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaNivel);
                    asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
                }else {
                    asignaturaNueva = asignaturaNivel.agregarAsignatura(asig,getContext());//
                }
            }

        }else if (CBD3.isChecked()){

            //agregamos Temas
            IAsignatura.asignatura asignaturaTemas = new TemasDecorador(Iasignatura);

            if (CBD4.isChecked()){
                IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
            }else {
                asignaturaNueva = asignaturaTemas.agregarAsignatura(asig,getContext());//
            }

        }else if (CBD4.isChecked()){
            //agregamos Duracion
            IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(Iasignatura);
            asignaturaNueva = asignaturaDuracion.agregarAsignatura(asig,getContext());//
        }else {
            asignaturaNueva = Iasignatura.agregarAsignatura(asig, getContext());
        }

        if (asignaturaCreateListener != null){
            asignaturaCreateListener.onCrearAsignatura(asignaturaNueva);
        }else {
            listener.onCrearAsignatura(asignaturaNueva);
        }
    }

    private Asignatura validarCampos(){

        Asignatura asig = new Asignatura();

        if(!nombreA.isEmpty()){

            asig.setNombreAsignatura(nombreA);
            asig.setArea(areaA);
            asig.setCarrera(carreraA);

            if (!creditosA.isEmpty()){
                int cred = Integer.parseInt(creditosA);
                if (cred > 0 && cred <= 6){
                    asig.setCreditos(creditosA);
                }else {
                    Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                }
            }

            if (!horasA.isEmpty()){
                int hor = Integer.parseInt(horasA);
                if (hor > 0 && hor <= 5){
                    asig.setHorario(horasA);
                }else {
                    Toast.makeText(getContext(),"El número de horas no debe ser mayor a 5",Toast.LENGTH_LONG).show();
                }
            }

            if (!descripcionA.isEmpty()){
                recibirDescripcion(descripcionA);
            }

            if (!nivelA.isEmpty()){
                recibirNivel(nivelA);
            }

            if (!temas.isEmpty()){
                recibirTemas(temas);
            }

            if (!duracion.isEmpty()){
                recibirDuracion(duracion);
            }

            Objects.requireNonNull(getDialog()).dismiss();

        }else {
            Toast.makeText(getContext(),"Agregar el nombre de la Asignatura",Toast.LENGTH_LONG).show();
        }

        return asig;
    }
}
