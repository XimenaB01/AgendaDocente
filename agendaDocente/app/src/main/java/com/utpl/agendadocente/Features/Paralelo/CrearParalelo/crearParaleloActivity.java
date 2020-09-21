package com.utpl.agendadocente.Features.Paralelo.CrearParalelo;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesDocente;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarSingleItem;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class crearParaleloActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener {

    private static ParaleloCrearListener paraleloCrearListener;

    private TextInputEditText nombre, alunmos;
    private TextView docenteAdd, tareaAdd, evaluacionAdd;
    private TextView asignaturaAdd, periodoAdd, horarioAdd;

    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());

    private String tipoComponente;
    private String [] itemsAgregados;
    private String itemAgregado = "";
    private List<Integer>IdsDoc = new ArrayList<>();
    private List<Integer>IdsTar = new ArrayList<>();
    private List<Integer>IdsEva = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private List<Docente> docenteList = operacionesDocente.listarDoc();
    private List<Tarea> tareaList = operacionesTarea.ListarTar();
    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();
    private List<Evaluacion> evaluacionList = operacionesEvaluacion.ListarEva();

    public crearParaleloActivity(){}

    public static crearParaleloActivity newInstance(String title, ParaleloCrearListener listener){
        paraleloCrearListener=listener;
        crearParaleloActivity crearPar = new crearParaleloActivity();
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
        docenteAdd = view.findViewById(R.id.agregarDocente);
        tareaAdd = view.findViewById(R.id.agregarTarea);
        evaluacionAdd = view.findViewById(R.id.agregarEvaluacion);
        asignaturaAdd = view.findViewById(R.id.agregarAsignatura);
        horarioAdd = view.findViewById(R.id.agregarHorario);
        periodoAdd = view.findViewById(R.id.agregarPeriodo);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        docenteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                String docentes = docenteAdd.getText().toString();
                tipoComponente = "Docente";

                //llena la lista con los nombres y apellidos de cada docente
                for (int i = 0; i < docenteList.size(); i++){
                    String docente = (String.format("%s %s",docenteList.get(i).getNombreDocente(),docenteList.get(i).getApellidoDocente()));
                    //verificar si no existe el docente, para agregarlo
                    if (!listItemMultiCkeck.contains(docente)){
                        listItemMultiCkeck.add(docente);
                    }
                }
                //verifica si en la variable docentes no existe las siguiente cadena "Agregar Docente"
                if (!docentes.contains("Agregar Docente")){
                    //divide o separa la cadena cada que encuentra ", " y guarda cada separacion en un array
                    String [] parts = docentes.split(", ");
                    itemsAgregados = new String[parts.length];
                    System.arraycopy(parts, 0, itemsAgregados, 0, parts.length);
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }else {
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }
            }
        });


        asignaturaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Asignatura";
                if(!asignaturaAdd.getText().equals("Agregar Asignatura")){
                    asignaturaList.clear();
                    asignaturaList = operacionesAsignatura.ListarAsig();
                    for (int i = 0; i < asignaturaList.size(); i++){
                        if (!listItemMultiCkeck.contains(asignaturaList.get(i).getNombreAsignatura())){
                            listItemMultiCkeck.add(asignaturaList.get(i).getNombreAsignatura());
                        }
                    }
                }else {
                    for (int i = 0; i < asignaturaList.size(); i++){
                        if (!listItemMultiCkeck.contains(asignaturaList.get(i).getNombreAsignatura())){
                            listItemMultiCkeck.add(asignaturaList.get(i).getNombreAsignatura());
                        }
                    }
                }

                String Asignatura = asignaturaAdd.getText().toString();
                if (!Asignatura.equals("Agregar Asignatura")){
                    itemAgregado = Asignatura;
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
                if (!horarioAdd.getText().equals("Agregar Horario")){
                    horarioList.clear();
                    horarioList = operacionesHorario.ListarHor();
                    for (int i = 0; i < horarioList.size();i++){
                        String horario = String.format("%s - %s",horarioList.get(i).getHora_entrada(),horarioList.get(i).getHora_salida());
                        if (!listItemMultiCkeck.contains(horario)){
                            listItemMultiCkeck.add(horario);
                        }
                    }
                }else {
                    for (int i = 0; i < horarioList.size();i++){
                        String horario = String.format("%s - %s",horarioList.get(i).getHora_entrada(),horarioList.get(i).getHora_salida());
                        if (!listItemMultiCkeck.contains(horario)){
                            listItemMultiCkeck.add(horario);
                        }
                    }
                }

                String Horario = horarioAdd.getText().toString();
                if (!Horario.equals("Agregar Horario")){
                    itemAgregado = Horario;
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
                if (periodoAdd.getText().equals("Agregar Periodo")){
                    periodoAcademicoList.clear();
                    periodoAcademicoList = operacionesPeriodo.ListarPer();
                    for (int i = 0; i < periodoAcademicoList.size(); i++){
                        String periodo = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (!listItemMultiCkeck.contains(periodo)){
                            listItemMultiCkeck.add(periodo);
                        }
                    }
                }
                else {
                    for (int i = 0; i < periodoAcademicoList.size(); i++){
                        String periodo = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (!listItemMultiCkeck.contains(periodo)){
                            listItemMultiCkeck.add(periodo);
                        }
                    }
                }
                String Periodo = periodoAdd.getText().toString();
                if (!Periodo.equals("Agregar Periodo")){
                    itemAgregado = Periodo;
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }else {
                    llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                }
            }
        });

        tareaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Tarea";
                String tareas = tareaAdd.getText().toString();
                for (int i = 0; i < tareaList.size(); i++){
                    if (!listItemMultiCkeck.contains(tareaList.get(i).getNombreTarea())){
                        listItemMultiCkeck.add(tareaList.get(i).getNombreTarea());
                    }
                }
                if(!tareas.contains("Agregar Tarea")){
                    String [] parts = tareas.split(" ,");
                    itemsAgregados = new String[parts.length];
                    System.arraycopy(parts, 0, itemsAgregados, 0, parts.length);
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }else {
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }
            }
        });

        evaluacionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemMultiCkeck.clear();
                tipoComponente = "Evaluación";
                String evaluaciones = evaluacionAdd.getText().toString();
                for (int i = 0; i < evaluacionList.size(); i++){
                    if (!listItemMultiCkeck.contains(evaluacionList.get(i).getNombreEvaluacion())){
                        listItemMultiCkeck.add(evaluacionList.get(i).getNombreEvaluacion());
                    }
                }
                if (!evaluaciones.contains("Agregar Evaluación")){
                    String [] parts = evaluaciones.split(" ,");
                    itemsAgregados = new String[parts.length];
                    System.arraycopy(parts, 0, itemsAgregados, 0, parts.length);
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }else {
                    llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                }
            }
        });

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CrearParalelo();
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

      return view;
    }

    private void CrearParalelo(){
        String nomPar = Objects.requireNonNull(nombre.getText()).toString();
        int alumPar = 0;
        if (!Objects.requireNonNull(alunmos.getText()).toString().isEmpty()){
            alumPar = Integer.parseInt(alunmos.getText().toString());
        }

        int asigIdPar = -1;
        for (int i = 0; i< asignaturaList.size(); i++){
            if (asignaturaAdd.getText().toString().equals(asignaturaList.get(i).getNombreAsignatura())){
                asigIdPar = asignaturaList.get(i).getId_asignatura();
            }
        }

        int hoIdPar = -1;
        for (int i = 0; i < horarioList.size(); i++){
            String horario = String.format("%s - %s", horarioList.get(i).getHora_entrada(), horarioList.get(i).getHora_salida());
            if (horarioAdd.getText().toString().equals(horario)){
                hoIdPar = horarioList.get(i).getId_horario();
            }
        }

        int perIdPar = -1;
        for (int i = 0; i < periodoAcademicoList.size(); i++) {
            String periodo = String.format("%s - %s", periodoAcademicoList.get(i).getFechaInicio(), periodoAcademicoList.get(i).getFechaFin());
            if (periodoAdd.getText().toString().equals(periodo)){
                perIdPar = periodoAcademicoList.get(i).getId_periodo();
            }
        }

        Paralelo paralelo = new Paralelo();

        if (!nomPar.isEmpty()){
            if (nomPar.length() == 1){
                if (alumPar != 0 ){
                    if (asigIdPar != -1){
                        paralelo.setNombreParalelo(nomPar);
                        paralelo.setNum_estudiantes(alumPar);
                        paralelo.setAsignaturaID(asigIdPar);
                        paralelo.setHoraioID(hoIdPar);
                        paralelo.setPeriodoID(perIdPar);

                        long insercion = operacionesParalelo.InsertarPar(paralelo, IdsDoc, IdsTar, IdsEva);
                        if (insercion > 0){
                            paraleloCrearListener.onCrearParalelo(paralelo);
                            Objects.requireNonNull(getDialog()).dismiss();
                        }else {
                            Toast.makeText(getContext(),"Ya existe este Paralelo",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"Agrege una Asignatura",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Los alumnos no pueden ser 0",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getContext(),"El nombre del paralelo debe ser una letra",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(),"Ingresa el paralelo",Toast.LENGTH_LONG).show();
        }

    }

    //Crea el Dialogo con el formulacion del paralelo
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void llamarDialogAgregarMultiItems(String Componente, List<String> ListItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(Componente, ListItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(crearParaleloActivity.this,22);
        agregarMultiItems.setCancelable(false);
        if (getFragmentManager() != null) {
            agregarMultiItems.show(getFragmentManager(),utilidades.CREAR);
        }
    }

    private void llamarDialogAgregarSingleItem(String Componente, List<String> ListaItems, String ItemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(Componente, ListaItems, ItemAsignado);
        agregarSingleItem.setTargetFragment(crearParaleloActivity.this,22);
        agregarSingleItem.setCancelable(false);
        if (getFragmentManager() != null) {
            agregarSingleItem.show(getFragmentManager(),utilidades.CREAR);
        }
    }

    @Override
    public void onAgregarItems(List<String> ItemsSeleccionados, String Componente) {
        StringBuilder ItemsAsignados = new StringBuilder();
        switch (Componente){
            case "Docente":
                if(ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    docenteAdd.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    docenteAdd.setText(ItemsAsignados);
                }
                break;
            case "Tarea":
                if (ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    tareaAdd.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    tareaAdd.setText(ItemsAsignados);
                }
                break;
            case "Evaluación":
                if (ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    evaluacionAdd.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    evaluacionAdd.setText(ItemsAsignados);
                }
                break;
        }

    }

    private void IdsComponentesSeleccionados(String Componente, List<String> ItemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < ItemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(ItemsSeleccionados.get(j))){
                    if (Componente.equals("Docente")){
                        docenteList.clear();
                        docenteList = operacionesDocente.listarDoc();
                        IdsDoc.add(docenteList.get(i).getId_docente());
                    }else if (Componente.equals("Tarea")){
                        tareaList.clear();
                        tareaList = operacionesTarea.ListarTar();
                        IdsTar.add(tareaList.get(i).getId_tarea());
                    }else {
                        evaluacionList.clear();
                        evaluacionList = operacionesEvaluacion.ListarEva();
                        IdsEva.add(evaluacionList.get(i).getId_evaluacion());
                    }
                }
            }
        }
    }

    private String obtnerItems(List<String> ItemsSeleccionados){
        StringBuilder item = new StringBuilder();
        for (int i = 0; i<ItemsSeleccionados.size(); i++){
            if (!item.toString().contains(ItemsSeleccionados.get(i))){
                item.append(ItemsSeleccionados.get(i));
                if (i < ItemsSeleccionados.size()-1){
                    item.append(", ");
                }
            }
        }
        return item.toString();
    }

    @Override
    public void onRecibirItemAsignado(String Componente, String ItemAsignado) {
        switch (Componente){
            case "Asignatura":
                asignaturaAdd.setText(ItemAsignado);
                break;
            case "Horario":
                horarioAdd.setText(ItemAsignado);
                break;
            case "Periodo":
                periodoAdd.setText(ItemAsignado);
                break;
        }
    }
}
