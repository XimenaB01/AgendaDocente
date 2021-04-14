package com.utpl.agendadocente.ui.paralelo.crear_paralelo;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarSingleItem;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.paralelo.IParalelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CrearParaleloActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener {

    private static IParalelo.ParaleloCrearListener paraleloCrearListener;

    private TextInputEditText nombre;
    private TextInputEditText alunmos;
    private TextView asignaturaAdd;
    private TextView periodoAdd;
    private TextView horarioAdd;
    private RecyclerView recyclerView;

    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());

    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
    private Paralelo paralelo = new Paralelo();
    private String tipoComponente;
    private String [] itemsAgregados = new String[0];
    private String itemAgregado = "";
    private int asigIdPar = -1;
    private int hoIdPar = -1;
    private int perIdPar = -1;
    private int alumPar = 0;
    private String nomPar;
    private List<Integer> idsDoc = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private List<String> listaDocentesAsignados = new ArrayList<>();

    private List<Docente> docenteList = operacionesDocente.listarDocente();
    private List<Asignatura> asignaturaList = new ArrayList<>();
    private List<Horario> horarioList = new ArrayList<>();
    private List<PeriodoAcademico> periodoAcademicoList = new ArrayList<>();

    public CrearParaleloActivity(){
        //required constuctor
    }

    public static CrearParaleloActivity newInstance(String title, IParalelo.ParaleloCrearListener listener){
        paraleloCrearListener = listener;
        CrearParaleloActivity crearPar = new CrearParaleloActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        crearPar.setArguments(bundle);

        crearPar.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        return crearPar;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_paralelo, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarPar);
        nombre = view.findViewById(R.id.nomPar);
        alunmos = view.findViewById(R.id.numAlu);
        Button docenteAdd = view.findViewById(R.id.agregarDocente);
        asignaturaAdd = view.findViewById(R.id.agregarAsignatura);
        horarioAdd = view.findViewById(R.id.agregarHorario);
        periodoAdd = view.findViewById(R.id.agregarPeriodo);
        recyclerView = view.findViewById(R.id.recyclerDoc);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        evaluacionCrearActivity.llenarRecycleView(recyclerView,getContext(), listaDocentesAsignados);

        docenteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Docente";

                //llena la lista con los nombres y apellidos de cada docente
                for (int i = 0; i < docenteList.size(); i++){
                    String docente = (String.format("%s %s",docenteList.get(i).getNombreDocente(),docenteList.get(i).getApellidoDocente()));
                    //verificar si no existe el docente, para agregarlo
                    if (!listItemMultiCkeck.contains(docente)){
                        listItemMultiCkeck.add(docente);
                    }
                }

                llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
            }
        });


        asignaturaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Asignatura";
                obtenerListaAsignatura();

                String asignaturaTexto = asignaturaAdd.getText().toString();
                if (!asignaturaTexto.equals("Agregar Asignatura")){
                    itemAgregado = asignaturaTexto;
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }else {
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }
            }
        });

        horarioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Horario";
                obtenerListaHorario();

                String horarioTexto = horarioAdd.getText().toString();
                if (!horarioTexto.equals("Agregar Horario")){
                    itemAgregado = horarioTexto;
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }else {
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }
            }
        });

        periodoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Periodo";
                obtenerListaPeriodo();

                String periodoTexto = periodoAdd.getText().toString();
                if (!periodoTexto.equals("Agregar Periodo")){
                    itemAgregado = periodoTexto;
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }else {
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }
            }
        });

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                obtenerDatosFormulario();
                return true;
            }
        });

      return view;
    }

    private void obtenerDatosFormulario(){
        nomPar = Objects.requireNonNull(nombre.getText()).toString();

        if (!Objects.requireNonNull(alunmos.getText()).toString().isEmpty()){
            alumPar = Integer.parseInt(alunmos.getText().toString());
        }

        for (int i = 0; i< asignaturaList.size(); i++){
            if (asignaturaAdd.getText().equals(asignaturaList.get(i).getNombreAsignatura())){
                asigIdPar = asignaturaList.get(i).getIdAsignatura();
            }
        }

        for (int i = 0; i < horarioList.size(); i++){
            String horario = String.format("%s Aula:%s De:%s A:%s", horarioList.get(i).getDia(), horarioList.get(i).getAula(), horarioList.get(i).getHoraEntrada(), horarioList.get(i).getHoraSalida());
            if (horarioAdd.getText().toString().equals(horario)){
                hoIdPar = horarioList.get(i).getIdHorario();
            }
        }

        for (int i = 0; i < periodoAcademicoList.size(); i++) {
            String periodo = String.format("%s - %s", periodoAcademicoList.get(i).getFechaInicio(), periodoAcademicoList.get(i).getFechaFin());
            if (periodoAdd.getText().toString().equals(periodo)){
                perIdPar = periodoAcademicoList.get(i).getIdPeriodo();
            }
        }

        guardarParalelo(validarDatosParalelo(nomPar, alumPar, asigIdPar, hoIdPar, perIdPar, getContext()));

    }

    public Paralelo validarDatosParalelo(String nomPar, int alumPar, int asigIdPar, int hoIdPar, int perIdPar, Context context) {
        Paralelo paralelo = new Paralelo();
        if (!nomPar.isEmpty()){
            if (nomPar.length() == 1){
                if (alumPar != 0 ){
                    if (asigIdPar != -1){
                        paralelo.setNombreParalelo(nomPar);
                        paralelo.setNumEstudiantes(alumPar);
                        paralelo.setAsignaturaID(asigIdPar);
                        paralelo.setHoraioID(hoIdPar);
                        paralelo.setPeriodoID(perIdPar);
                    }else {
                        Toast.makeText(context,"Agrege una Asignatura",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context,"Los alumnos no pueden ser 0",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context,"El nombre del paralelo debe ser una letra",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(context,"Ingresa el paralelo",Toast.LENGTH_LONG).show();
        }
        return paralelo;
    }

    private void guardarParalelo(Paralelo paralelo) {
        long insercion = operacionesParalelo.insertarParalelo(paralelo, idsDoc);
        if (insercion > 0){
            paralelo.setIdParalelo((int)insercion);
            paraleloCrearListener.onCrearParalelo(paralelo);
            Objects.requireNonNull(getDialog()).dismiss();
        }else {
            Toast.makeText(getContext(),"Ya existe este Paralelo",Toast.LENGTH_LONG).show();
        }
    }

    //Crea el Dialogo con el formulacion del paralelo
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }

    private void llamarDialogAgregarMultiItems(String componente, List<String> listItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(componente, listItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(CrearParaleloActivity.this,22);
        agregarMultiItems.setCancelable(false);
        agregarMultiItems.show(getParentFragmentManager(), Utilidades.CREAR);
    }

    private void llamarDialogAgregarSingleItem(String componente, List<String> listaItems, String itemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(componente, listaItems, itemAsignado);
        agregarSingleItem.setTargetFragment(CrearParaleloActivity.this,22);
        agregarSingleItem.setCancelable(false);
        agregarSingleItem.show(getParentFragmentManager(), Utilidades.CREAR);
    }

    private void idsComponentesSeleccionados(List<String> itemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < itemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(itemsSeleccionados.get(j))){
                    docenteList.clear();
                    docenteList = operacionesDocente.listarDocente();
                    idsDoc.add(docenteList.get(i).getIdDocente());
                }
            }
        }
    }

    private void obtenerListaHorario(){
        horarioList.clear();
        horarioList = operacionesHorario.listarHorario();
        for (int i = 0; i < horarioList.size();i++){
            String horario = String.format("%s Aula:%s De:%s A:%s",horarioList.get(i).getDia(),horarioList.get(i).getAula(), horarioList.get(i).getHoraEntrada(),horarioList.get(i).getHoraSalida());
            if (!listItemMultiCkeck.contains(horario)){
                listItemMultiCkeck.add(horario);
            }
        }
    }

    private void obtenerListaPeriodo(){
        periodoAcademicoList.clear();
        periodoAcademicoList = operacionesPeriodo.listarPeriodo();
        for (int i = 0; i < periodoAcademicoList.size(); i++){
            String periodo = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
            if (!listItemMultiCkeck.contains(periodo)){
                listItemMultiCkeck.add(periodo);
            }
        }
    }

    private void obtenerListaAsignatura(){
        asignaturaList.clear();
        asignaturaList = operacionesAsignatura.listarAsignatura();
        for (int i = 0; i < asignaturaList.size(); i++){
            if (!listItemMultiCkeck.contains(asignaturaList.get(i).getNombreAsignatura())){
                listItemMultiCkeck.add(asignaturaList.get(i).getNombreAsignatura());
            }
        }
    }

    @Override
    public void onAgregarItems(List<String> itemsSeleccionados, String componente) {
        if ("Docente".equals(componente) && !itemsSeleccionados.isEmpty()) {
            listaDocentesAsignados = itemsSeleccionados;
            itemsAgregados = new String[itemsSeleccionados.size()];
            for (int i = 0; i < itemsSeleccionados.size(); i++) {
                itemsAgregados[i] = itemsSeleccionados.get(i);
            }
            evaluacionCrearActivity.llenarRecycleView(recyclerView, getContext(), itemsSeleccionados);
            idsComponentesSeleccionados(itemsSeleccionados);
        }
    }

    @Override
    public void onRecibirItemAsignado(String componente, String itemAsignado) {
        switch (componente){
            case "Asignatura":
                asignaturaAdd.setText(itemAsignado);
                asignaturaList.clear();
                asignaturaList = operacionesAsignatura.listarAsignatura();
                break;
            case "Horario":
                horarioAdd.setText(itemAsignado);
                horarioList.clear();
                horarioList = operacionesHorario.listarHorario();
                break;
            case "Periodo":
                periodoAdd.setText(itemAsignado);
                periodoAcademicoList.clear();
                periodoAcademicoList = operacionesPeriodo.listarPeriodo();
                break;
            default:
                //ignore this part
                break;
        }
    }
}
