package com.utpl.agendadocente.ui.asignatura.actualizar_asignatura;

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
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.decorador.DescripcionDecorador;
import com.utpl.agendadocente.decorador.DuracionDecorador;
import com.utpl.agendadocente.decorador.NivelDecorador;
import com.utpl.agendadocente.decorador.TemasDecorador;
import com.utpl.agendadocente.decorador.intef.IAsignatura;
import com.utpl.agendadocente.decorador.intef.impl.AsignaturaListenerNormal;

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

    private TextInputEditText nomAct;
    private TextInputEditText creAct;
    private TextInputEditText horAct;
    private TextInputEditText desAct;
    private TextInputEditText nilAct;
    private TextInputEditText temAct;
    private Spinner area;
    private Spinner carrerra;
    private MaterialCheckBox checkBoxDescripcion;
    private MaterialCheckBox checkBoxNivel;
    private MaterialCheckBox checkBoxTema;
    private MaterialCheckBox checkBoxDuracion;
    private TextInputLayout textInputLayoutDesc;
    private TextInputLayout textInputLayoutTem;
    private TextInputLayout textInputLayoutNiv;
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
    private String textDuracion = "Duración";

    private OperacionesComponente operacionesComponente = new OperacionesComponente(getContext());
    private List<Componente> list = operacionesComponente.obtenerComponentes(idAsignatura);

    public AsignaturaActualizarActivity(){
        //Ignore this method
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

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

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
        textInputLayoutDesc = view.findViewById(R.id.outlinedTextField2);
        textInputLayoutTem = view.findViewById(R.id.outlinedTextField7);
        textInputLayoutNiv = view.findViewById(R.id.outlinedTextField3);
        checkBoxDescripcion = view.findViewById(R.id.descCH);
        checkBoxNivel = view.findViewById(R.id.nivCH);
        checkBoxTema = view.findViewById(R.id.temCH);
        checkBoxDuracion = view.findViewById(R.id.durCH);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        ArrayAdapter<CharSequence> areas = ArrayAdapter.createFromResource(requireContext(),R.array.areas,R.layout.spinner_item_style_pesonal);
        areas.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        area.setAdapter(areas);
        area.setOnItemSelectedListener(this);

        checkBoxDescripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxDescripcion.isChecked()){
                    checkBoxDescripcion.setChecked(false);
                    textInputLayoutDesc.setVisibility(View.GONE);
                    desAct.setText("");
                }else {
                    checkBoxDescripcion.setChecked(true);
                    textInputLayoutDesc.setVisibility(View.VISIBLE);
                }
            }
        });

        checkBoxNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxNivel.isChecked()){
                    checkBoxNivel.setChecked(false);
                    textInputLayoutNiv.setVisibility(View.GONE);
                    nilAct.setText("");
                }else {
                    checkBoxNivel.setChecked(true);
                    textInputLayoutNiv.setVisibility(View.VISIBLE);
                }
            }
        });

        checkBoxTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxTema.isChecked()){
                    checkBoxTema.setChecked(false);
                    textInputLayoutTem.setVisibility(View.GONE);
                    temAct.setText("");
                }else {
                    checkBoxTema.setChecked(true);
                    textInputLayoutTem.setVisibility(View.VISIBLE);
                }
            }
        });

        checkBoxDuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxDuracion.isChecked()){
                    checkBoxDuracion.setChecked(false);
                    button.setVisibility(View.GONE);
                    button.setText(textDuracion);
                }else {
                    checkBoxDuracion.setChecked(true);
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

                    enviarDatos(validarCampos());

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

            validarCampoCreditos();
            validarCampoHorario();
            validarCampoDescripcion();
            validarCampoNivel();
            validarCampoTemas();
            validarCampoDuracion();

        }else {
            Toast.makeText(getContext(),"Ingrese el nombre de la Asignatura",Toast.LENGTH_LONG).show();
        }

        return asignatura;
    }

    private void validarCampoCreditos(){
        if (!creditoAsig.isEmpty()){
            int cred = Integer.parseInt(creditoAsig);
            if (cred > 0 && cred <= 6){
                asignatura.setCreditos(creditoAsig);
            }else {
                Toast.makeText(getContext(),"El número de creditos debe ser entre 1-6",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarCampoHorario(){
        if (!horaAsig.isEmpty()){
            int hor = Integer.parseInt(horaAsig);
            if (hor > 0 && hor <= 5){
                asignatura.setHorario(horaAsig);
            }else {
                Toast.makeText(getContext(),"El número de horas no debe ser mayor a 5",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarCampoDescripcion(){
        String textDescripcion = "Descripcion";
        if (checkBoxDescripcion.isChecked()){
            if (!descrpcionAsig.isEmpty()){
                recibirDescripcion(descrpcionAsig);
            }else {
                eliminarComponente(textDescripcion);
            }
        }else {
            eliminarComponente(textDescripcion);
        }
    }

    private void validarCampoNivel(){
        String textNivel = "Nivel";
        if (checkBoxNivel.isChecked()){
            if (!nivelAsig.isEmpty()){
                recibirNivel(nivelAsig);
            }else {
                eliminarComponente(textNivel);
            }
        }else {
            eliminarComponente(textNivel);
        }
    }

    private void validarCampoTemas(){
        String textTema = "Temas";
        if (checkBoxTema.isChecked()){
            if (!temas.isEmpty()){
                recibirTemas(temas);
            }else {
                eliminarComponente(textTema);
            }
        }else {
            eliminarComponente(textTema);
        }
    }

    private void validarCampoDuracion(){
        if (checkBoxDuracion.isChecked()){
            if (!duracion.isEmpty()){
                recibirDuracion(duracion);
            }else {
                eliminarComponente(textDuracion);
            }
        }else {
            eliminarComponente(textDuracion);
        }
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
        //Ignore method
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
                    checkBoxDescripcion.setChecked(true);
                    textInputLayoutDesc.setVisibility(View.VISIBLE);
                    desAct.setText(list.get(i).getValor());
                    break;
                case "Nivel":
                    checkBoxNivel.setChecked(true);
                    textInputLayoutNiv.setVisibility(View.VISIBLE);
                    nilAct.setText(list.get(i).getValor());
                    break;
                case "Temas":
                    checkBoxTema.setChecked(true);
                    textInputLayoutTem.setVisibility(View.VISIBLE);
                    temAct.setText(list.get(i).getValor());
                    break;
                case "Duracion":
                    checkBoxDuracion.setChecked(true);
                    button.setVisibility(View.VISIBLE);
                    llamarPoputMenu(i);
                    break;
                default:
                    //Ignore this part
                    break;
            }
        }
    }

    private void enviarDatos(Asignatura asignatura){

        IAsignatura.AsignaturaListener iasignatura = new AsignaturaListenerNormal();

        if (checkBoxDescripcion.isChecked()) {
            actualizarAsignaturaListener.onActualizarAsignatura(getDescripcionDecoradorForAsignaturaNormal(iasignatura),asignaturaItemPosition);
        } else if (checkBoxNivel.isChecked()){
            actualizarAsignaturaListener.onActualizarAsignatura(getNivelDecoradorForAsignaturaNormal(iasignatura),asignaturaItemPosition);
        }else if (checkBoxTema.isChecked()){
            actualizarAsignaturaListener.onActualizarAsignatura(getTemasDecoradotForAsignaturaNormal(iasignatura),asignaturaItemPosition);
        }else if (checkBoxDuracion.isChecked()){
            actualizarAsignaturaListener.onActualizarAsignatura(getDuracionDecoradorForAsignaturaNormal(iasignatura),asignaturaItemPosition);
        }else {
            actualizarAsignaturaListener.onActualizarAsignatura(iasignatura.agregarAsignatura(asignatura, getContext()),asignaturaItemPosition);
        }
        Objects.requireNonNull(getDialog()).dismiss();
    }

    private void eliminarComponente(String componente1){
        Componente componente = new Componente();
        componente.setIdAsig(idAsignatura);
        componente.setComponente(componente1);
        operacionesComponente.eliminarComponente(componente);
    }

    private void llamarPoputMenu(int i){
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
    }

    private Asignatura getDescripcionDecoradorForAsignaturaNormal( IAsignatura.AsignaturaListener iasignatura){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerDescripcion = new DescripcionDecorador(iasignatura);
        if (checkBoxNivel.isChecked()){
            asignaturaNueva = getNivelDecoradorForAsignaturaNormal(asignaturaListenerDescripcion);
        }else if (checkBoxTema.isChecked()){
            asignaturaNueva = getTemasDecoradotForAsignaturaNormal(asignaturaListenerDescripcion);
        }else if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDuracionDecoradorForAsignaturaNormal(asignaturaListenerDescripcion);
        }else {
            asignaturaNueva = asignaturaListenerDescripcion.agregarAsignatura(asignatura,getContext());
        }

        return asignaturaNueva;
    }

    private Asignatura getNivelDecoradorForAsignaturaNormal(IAsignatura.AsignaturaListener iasignatura){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerNivel = new NivelDecorador(iasignatura);
        if (checkBoxTema.isChecked()){
            asignaturaNueva = getTemasDecoradotForAsignaturaNormal(asignaturaListenerNivel);
        }else if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDuracionDecoradorForAsignaturaNormal(asignaturaListenerNivel);
        }else {
            asignaturaNueva = asignaturaListenerNivel.agregarAsignatura(asignatura,getContext());
        }
        return asignaturaNueva;
    }

    private Asignatura getTemasDecoradotForAsignaturaNormal(IAsignatura.AsignaturaListener iasignatura){
        Asignatura asignaturaNueva;
        IAsignatura.AsignaturaListener asignaturaListenerTemas = new TemasDecorador(iasignatura);

        if (checkBoxDuracion.isChecked()){
            asignaturaNueva = getDuracionDecoradorForAsignaturaNormal(asignaturaListenerTemas);
        }else {
            asignaturaNueva = asignaturaListenerTemas.agregarAsignatura(asignatura,getContext());//
        }
        return asignaturaNueva;
    }

    private Asignatura getDuracionDecoradorForAsignaturaNormal(IAsignatura.AsignaturaListener iasignatura){
        IAsignatura.AsignaturaListener asignaturaListenerDuracion = new DuracionDecorador(iasignatura);
        return asignaturaListenerDuracion.agregarAsignatura(asignatura,getContext());
    }

}
