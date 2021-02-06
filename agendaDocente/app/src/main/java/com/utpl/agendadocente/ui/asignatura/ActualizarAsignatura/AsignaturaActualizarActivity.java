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
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesComponente;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.decorador.DescripcionDecorador;
import com.utpl.agendadocente.decorador.DuracionDecorador;
import com.utpl.agendadocente.decorador.NivelDecorador;
import com.utpl.agendadocente.decorador.TemasDecorador;
import com.utpl.agendadocente.intef.IAsignatura;
import com.utpl.agendadocente.intef.impl.AsignaturaNormal;

import java.util.List;
import java.util.Objects;

import static com.utpl.agendadocente.decorador.DescripcionDecorador.recibirDescripcion;
import static com.utpl.agendadocente.decorador.DuracionDecorador.recibirDuracion;
import static com.utpl.agendadocente.decorador.NivelDecorador.recibirNivel;
import static com.utpl.agendadocente.decorador.TemasDecorador.recibirTemas;

public class AsignaturaActualizarActivity extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private static Integer idAsignatura;
    private static int asignaturaItemPosition;
    private static IAsignatura.ActualizarAsignaturaListener actualizarAsignaturaListener;

    private Asignatura asignatura;

    private TextInputEditText nomAct, creAct, horAct , desAct, nilAct, temAct;
    private Spinner area, carrerra;
    private MaterialCheckBox CH1, CH2, CH3, CH4;
    private TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    private Button button;

    private String nombreAsig = "";
    private String descrpcionAsig = "";
    private String nivelAsig = "";
    private String carreraAsig = "";
    private String creditoAsig = "";
    private String areaAsig = "";
    private String horaAsig = "";
    private String duracion = "";
    private String temas = "";

    private OperacionesAsignatura operacionesAsignatura;
    private OperacionesComponente operacionesComponente = new OperacionesComponente(getContext());
    private List<Componente> list = operacionesComponente.obtenerComponentes(idAsignatura);

    public AsignaturaActualizarActivity(){

    }

    public static AsignaturaActualizarActivity newInstance(Integer id, int position, IAsignatura.ActualizarAsignaturaListener listener){
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
        temAct = view.findViewById(R.id.temAsigAct);
        button = view.findViewById(R.id.durAsigAct);
        textInputLayout1 = view.findViewById(R.id.outlinedTextField2);
        textInputLayout2 = view.findViewById(R.id.outlinedTextField7);
        textInputLayout3 = view.findViewById(R.id.outlinedTextField3);
        CH1 = view.findViewById(R.id.descCH);
        CH2 = view.findViewById(R.id.nivCH);
        CH3 = view.findViewById(R.id.temCH);
        CH4 = view.findViewById(R.id.durCH);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        area.setAdapter(areas);
        area.setOnItemSelectedListener(this);

        CH1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CH1.isChecked()){
                    CH1.setChecked(false);
                    textInputLayout1.setVisibility(View.GONE);
                    desAct.setText("");
                }else {
                    CH1.setChecked(true);
                    textInputLayout1.setVisibility(View.VISIBLE);
                }
            }
        });

        CH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CH2.isChecked()){
                    CH2.setChecked(false);
                    textInputLayout3.setVisibility(View.GONE);
                    nilAct.setText("");
                }else {
                    CH2.setChecked(true);
                    textInputLayout3.setVisibility(View.VISIBLE);
                }
            }
        });

        CH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CH3.isChecked()){
                    CH3.setChecked(false);
                    textInputLayout2.setVisibility(View.GONE);
                    temAct.setText("");
                }else {
                    CH3.setChecked(true);
                    textInputLayout2.setVisibility(View.VISIBLE);
                }
            }
        });

        CH4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CH4.isChecked()){
                    CH4.setChecked(false);
                    button.setVisibility(View.GONE);
                    String text = "Duración";
                    button.setText(text);
                }else {
                    CH4.setChecked(true);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        asignatura = operacionesAsignatura.obtenerAsignatura(idAsignatura);
        if (asignatura!=null){

            llenarFormulario();

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    nombreAsig = Objects.requireNonNull(nomAct.getText()).toString();
                    carreraAsig = carrerra.getSelectedItem().toString();
                    creditoAsig = Objects.requireNonNull(creAct.getText()).toString();
                    areaAsig = area.getSelectedItem().toString();
                    horaAsig = Objects.requireNonNull(horAct.getText()).toString();
                    descrpcionAsig = Objects.requireNonNull(desAct.getText()).toString();
                    nivelAsig = Objects.requireNonNull(nilAct.getText()).toString();
                    temas = Objects.requireNonNull(temAct.getText()).toString();
                    duracion = button.getText().toString();

                    EnviarDatos(validarCampos());

                    return true;
                }
            });

        }
        return view;
    }

    private Asignatura validarCampos() {

        if (!nombreAsig.isEmpty()){

            asignatura.setNombreAsignatura(nombreAsig);
            asignatura.setArea(areaAsig);
            asignatura.setCarrera(carreraAsig);

            if (!creditoAsig.isEmpty()){
                int cred = Integer.parseInt(creditoAsig);
                if (cred > 0 && cred <= 6){
                    asignatura.setCreditos(creditoAsig);
                }else {
                    Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
                }
            }

            if (!horaAsig.isEmpty()){
                int hor = Integer.parseInt(horaAsig);
                if (hor > 0 && hor <= 5){
                    asignatura.setHorario(horaAsig);
                }else {
                    Toast.makeText(getContext(),"El número de horas no debe ser mayor a 5",Toast.LENGTH_LONG).show();
                }
            }

            if (CH1.isChecked()){
                if (!descrpcionAsig.isEmpty()){
                    recibirDescripcion(descrpcionAsig);
                }else {
                    eliminarComponente("Descripcion");
                }
            }else {
                eliminarComponente("Descripcion");
            }

            if (CH2.isChecked()){
                if (!nivelAsig.isEmpty()){
                    recibirNivel(nivelAsig);
                }else {
                    eliminarComponente("Nivel");
                }
            }else {
                eliminarComponente("Nivel");
            }

            if (CH3.isChecked()){
                if (!temas.isEmpty()){
                    recibirTemas(temas);
                }else {
                    eliminarComponente("Temas");
                }
            }else {
                eliminarComponente("Temas");
            }

            if (CH4.isChecked()){
                if (!duracion.isEmpty()){
                    recibirDuracion(duracion);
                }else {
                    eliminarComponente("Duracion");
                }
            }else {
                eliminarComponente("Duracion");
            }

        }else {
            Toast.makeText(getContext(),"Ingrese el nombre de la Asignatura",Toast.LENGTH_LONG).show();
        }

        return asignatura;
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

    private void llenarFormulario(){
        nomAct.setText(asignatura.getNombreAsignatura());
        creAct.setText(asignatura.getCreditos());
        horAct.setText(asignatura.getHorario());
        final String areaspiner = asignatura.getArea();
        area.setSelection(obtenerPositionItem(area,areaspiner));

        for (int i = 0; i < list.size(); i++){
            switch(list.get(i).getComponente()){
                case "Descripcion":
                    CH1.setChecked(true);
                    textInputLayout1.setVisibility(View.VISIBLE);
                    desAct.setText(list.get(i).getValor());
                    break;
                case "Nivel":
                    CH2.setChecked(true);
                    textInputLayout3.setVisibility(View.VISIBLE);
                    nilAct.setText(list.get(i).getValor());
                    break;
                case "Temas":
                    CH3.setChecked(true);
                    textInputLayout2.setVisibility(View.VISIBLE);
                    temAct.setText(list.get(i).getValor());
                    break;
                case "Duracion":
                    CH4.setChecked(true);
                    button.setVisibility(View.VISIBLE);

                    final PopupMenu popupMenuEstados = new PopupMenu(getContext(),button);
                    popupMenuEstados.getMenuInflater().inflate(R.menu.menu_duracion,popupMenuEstados.getMenu());

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupMenuEstados.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    button.setText(menuItem.getTitle());
                                    return true;
                                }
                            });
                            popupMenuEstados.show();
                        }
                    });

                    for (int j = 0; j < popupMenuEstados.getMenu().size(); j++){
                        if (list.get(i).getValor().equals(popupMenuEstados.getMenu().getItem(j).getTitle().toString())) {
                            button.setText(popupMenuEstados.getMenu().getItem(j).getTitle());
                        }
                    }

                    break;
            }
        }
    }

    private void EnviarDatos(Asignatura asignatura){

        Asignatura asignaturaNueva = new Asignatura();

        IAsignatura.asignatura Iasignatura = new AsignaturaNormal();

        if (CH1.isChecked()) {
            IAsignatura.asignatura asignaturaDescripcion = new DescripcionDecorador(Iasignatura);

            if (CH2.isChecked()){
                IAsignatura.asignatura asignaturaNivel = new NivelDecorador(asignaturaDescripcion);

                if (CH3.isChecked()){
                    IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaNivel);

                    if (CH4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                    }else {
                        asignaturaNueva = asignaturaTemas.agregarAsignatura(asignatura,getContext());//
                    }
                }else {
                    if (CH4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaNivel);
                        asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                    }else {
                        asignaturaNueva = asignaturaNivel.agregarAsignatura(asignatura,getContext());//
                    }
                }

            }else {

                if (CH3.isChecked()){
                    IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaDescripcion);

                    if (CH4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                    }else {
                        asignaturaNueva = asignaturaTemas.agregarAsignatura(asignatura,getContext());//
                    }
                }else {
                    if (CH4.isChecked()){
                        IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaDescripcion);
                        asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                    }else {
                        asignaturaNueva = asignaturaDescripcion.agregarAsignatura(asignatura,getContext());//
                    }
                }
            }

        } else if (CH2.isChecked()){
            //agregamos Nivel
            IAsignatura.asignatura asignaturaNivel = new NivelDecorador(Iasignatura);

            if (CH3.isChecked()){
                IAsignatura.asignatura asignaturaTemas = new TemasDecorador(asignaturaNivel);

                if (CH4.isChecked()){
                    IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                    asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                }else {
                    asignaturaNueva = asignaturaTemas.agregarAsignatura(asignatura,getContext());//
                }
            }else {
                if (CH4.isChecked()){
                    IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaNivel);
                    asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
                }else {
                    asignaturaNueva = asignaturaNivel.agregarAsignatura(asignatura,getContext());//
                }
            }

        }else if (CH3.isChecked()){

            //agregamos Temas
            IAsignatura.asignatura asignaturaTemas = new TemasDecorador(Iasignatura);

            if (CH4.isChecked()){
                IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(asignaturaTemas);
                asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
            }else {
                asignaturaNueva = asignaturaTemas.agregarAsignatura(asignatura,getContext());//
            }

        }else if (CH4.isChecked()){
            //agregamos Duracion
            IAsignatura.asignatura asignaturaDuracion = new DuracionDecorador(Iasignatura);
            asignaturaNueva = asignaturaDuracion.agregarAsignatura(asignatura,getContext());//
        }else {
            asignaturaNueva = Iasignatura.agregarAsignatura(asignatura, getContext());
        }

        actualizarAsignaturaListener.onActualizarAsignatura(asignaturaNueva,asignaturaItemPosition);
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void eliminarComponente(String Componente){
        Componente componente = new Componente();
        componente.setIdAsig(idAsignatura);
        componente.setComponente(Componente);
        operacionesComponente.EliminarComponente(componente);
    }
}
