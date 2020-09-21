package com.utpl.agendadocente.Features.Paralelo.ActualizarParalelo;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
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
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarSingleItem;

import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParaleloActualizarActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener{

    private static String NomParalelo;
    private static Integer IdAsignatura;
    private static int paraleloItemPosition;
    private static ActualizarParaleloListener actualizarParaleloListener;

    private TextInputEditText nombre, alunmos;
    private TextView docenteAddAct, tareaAddAct, evaluacionAddAct;
    private TextView asignaturaAddAct, horarioAddAct, periodoAddAct;

    private Paralelo paralelo;

    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());

    private List<Docente> docenteList = operacionesDocente.listarDoc();
    private List<Tarea> tareaList = operacionesTarea.ListarTar();
    private List<Evaluacion> evaluacionList = operacionesEvaluacion.ListarEva();
    private List<Asignatura> asigList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();

    private String [] itemsAgregados;
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private String itemAgregado = "";
    private String tipoComponente;
    private List<Integer> IdsDoc = new ArrayList<>();
    private List<Integer> IdsTar = new ArrayList<>();
    private List<Integer> IdsEva = new ArrayList<>();

    public ParaleloActualizarActivity(){}

    public static ParaleloActualizarActivity newInstance(Paralelo paralelo, int position, ActualizarParaleloListener listener){
        NomParalelo = paralelo.getNombreParalelo();
        IdAsignatura = paralelo.getAsignaturaID();
        paraleloItemPosition = position;
        actualizarParaleloListener = listener;
        ParaleloActualizarActivity paraleloActualizar = new ParaleloActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Editar Paralelo");
        paraleloActualizar.setArguments(bundle);

        paraleloActualizar.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        return paraleloActualizar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_paralelo,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarParA);
        nombre = view.findViewById(R.id.nomParAct);
        alunmos = view.findViewById(R.id.numAluAct);
        docenteAddAct = view.findViewById(R.id.agregarDocenteAct);
        tareaAddAct = view.findViewById(R.id.agregarTareaAct);
        evaluacionAddAct = view.findViewById(R.id.agregarEvaluacionAct);
        asignaturaAddAct = view.findViewById(R.id.agregarAsignaturaAct);
        horarioAddAct = view.findViewById(R.id.agregarHorarioAct);
        periodoAddAct = view.findViewById(R.id.agregarPeriodoAct);


        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        paralelo = operacionesParalelo.obtenerPar(NomParalelo,IdAsignatura);

        if (paralelo != null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNum_estudiantes()));

            obtenerDocentes();
            docenteAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listItemMultiCkeck.clear();
                    String docentes = docenteAddAct.getText().toString();
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

            obtenerTarea();
            tareaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listItemMultiCkeck.clear();
                    tipoComponente = "Tarea";
                    String tareas = tareaAddAct.getText().toString();
                    for (int i = 0; i < tareaList.size(); i++){
                        if (!listItemMultiCkeck.contains(tareaList.get(i).getNombreTarea())){
                            listItemMultiCkeck.add(tareaList.get(i).getNombreTarea());
                        }
                    }
                    if(!tareas.contains("Agregar Tarea")){
                        String [] parts = tareas.split(", ");
                        itemsAgregados = new String[parts.length];
                        System.arraycopy(parts, 0, itemsAgregados, 0, parts.length);
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }else {
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }
                }
            });

            obtenerEvaluacion();
            evaluacionAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemMultiCkeck.clear();
                    tipoComponente = "Evaluación";
                    String evaluaciones = evaluacionAddAct.getText().toString();
                    for (int i = 0; i < evaluacionList.size(); i++){
                        if (!listItemMultiCkeck.contains(evaluacionList.get(i).getNombreEvaluacion())){
                            listItemMultiCkeck.add(evaluacionList.get(i).getNombreEvaluacion());
                        }
                    }
                    if (!evaluaciones.contains("Agregar Evaluación")){
                        String [] parts = evaluaciones.split(", ");
                        itemsAgregados = new String[parts.length];
                        System.arraycopy(parts, 0, itemsAgregados, 0, parts.length);
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }else {
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }

                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listItemMultiCkeck.clear();
                    tipoComponente = "Asignatura";
                    for (int i = 0; i < asigList.size(); i++){
                        if (!listItemMultiCkeck.contains(asigList.get(i).getNombreAsignatura())){
                            listItemMultiCkeck.add(asigList.get(i).getNombreAsignatura());
                        }
                    }
                    String Asignatura = asignaturaAddAct.getText().toString();
                    if (!Asignatura.equals("Agregar Asignatura")){
                        itemAgregado = Asignatura;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }

                }
            });

            obtenerHorario(paralelo.getHoraioID());
            horarioAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemMultiCkeck.clear();
                    tipoComponente = "Horario";
                    for (int i = 0; i < horarioList.size();i++){
                        String horario = String.format("%s - %s",horarioList.get(i).getHora_entrada(),horarioList.get(i).getHora_salida());
                        if (!listItemMultiCkeck.contains(horario)){
                            listItemMultiCkeck.add(horario);
                        }
                    }
                    String Horario = horarioAddAct.getText().toString();
                    if (!Horario.equals("Agregar Horario")){
                        itemAgregado = Horario;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }
                }
            });

            obtenerPeriodo(paralelo.getPeriodoID());
            periodoAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemMultiCkeck.clear();
                    tipoComponente = "Periodo";
                    for (int i = 0; i < periodoAcademicoList.size(); i++){
                        String periodo = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (!listItemMultiCkeck.contains(periodo)){
                            listItemMultiCkeck.add(periodo);
                        }
                    }
                    String Periodo = periodoAddAct.getText().toString();
                    if (!Periodo.equals("Agregar Periodo")){
                        itemAgregado = Periodo;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }
                }
            });

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ActualizarParalelo();
                    return true;
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }else {
            Toast.makeText(getContext(),"Paralelo no existe",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void ActualizarParalelo() {

        String nomParAct = Objects.requireNonNull(nombre.getText()).toString();
        int alumParAct = Integer.parseInt(Objects.requireNonNull(alunmos.getText()).toString());

        int asigIdParAct = -1;
        for (int i = 0; i < asigList.size(); i++){
            if (asignaturaAddAct.getText().toString().equals(asigList.get(i).getNombreAsignatura())){
                asigIdParAct = asigList.get(i).getId_asignatura();
            }
        }

        int hoIdParAct = -1;
        for (int i = 0; i < horarioList.size(); i++){
            String horario = String.format("%s - %s",horarioList.get(i).getHora_entrada(),horarioList.get(i).getHora_salida());
            if (horarioAddAct.getText().toString().equals(horario)){
                hoIdParAct = horarioList.get(i).getId_horario();
            }
        }

        int perIdParAct = -1;
        for (int i = 0; i < periodoAcademicoList.size(); i++){
            String periodo = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
            if (periodoAddAct.getText().toString().equals(periodo)){
                perIdParAct = periodoAcademicoList.get(i).getId_periodo();
            }
        }

        if (!nomParAct.isEmpty()){
            if (nomParAct.length() == 1){
                if (alumParAct != 0){
                    if (asigIdParAct != -1){
                        paralelo.setNombreParalelo(nomParAct);
                        paralelo.setNum_estudiantes(alumParAct);
                        paralelo.setAsignaturaID(asigIdParAct);
                        paralelo.setHoraioID(hoIdParAct);
                        paralelo.setPeriodoID(perIdParAct);
                        long insertion = operacionesParalelo.ModificarPar(NomParalelo,IdAsignatura,paralelo, IdsDoc, IdsTar, IdsEva);

                        if (insertion > 0){
                            actualizarParaleloListener.onActualizarParalelo(paralelo, paraleloItemPosition);
                            Objects.requireNonNull(getDialog()).dismiss();
                        }else {
                            Toast.makeText(getContext(),"El paralelo ya existe!!",Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "Agrege en Nombre del Paralelo", Toast.LENGTH_LONG).show();
        }

    }

    private void obtenerAsignaturas(Integer IdAsignatura){
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(IdAsignatura);
        asignaturaAddAct.setText(asignatura.getNombreAsignatura());
    }

    private void obtenerHorario(long IdHOrario){
        if (IdHOrario != -1){
            Horario horario = operacionesHorario.obtenerHor(IdHOrario);
            horarioAddAct.setText(String.format("%s - %s",horario.getHora_entrada(),horario.getHora_salida()));
        }else {
            String mensaje = "Agregar Horario";
            horarioAddAct.setText(mensaje);
        }

    }

    private void obtenerPeriodo(long IdPeriodo) {
        if (IdPeriodo != -1){
            PeriodoAcademico periodoAcademico = operacionesPeriodo.obtenerPer(IdPeriodo);
            periodoAddAct.setText(String.format("%s - %s",periodoAcademico.getFechaInicio(),periodoAcademico.getFechaFin()));
        }else {
            String mensaje = "Agregar Periodo";
            periodoAddAct.setText(mensaje);
        }
    }

    private void obtenerDocentes(){

        List<Docente> docList = operacionesParalelo.obtenerDocentesAsignadosParalelo(NomParalelo,IdAsignatura);
        StringBuilder Docentes = new StringBuilder();
        if (docList.size() != 0){
            for (int i = 0; i< docList.size(); i++){
                Docentes.append(String.format("%s %s", docList.get(i).getNombreDocente(), docList.get(i).getApellidoDocente()));
                IdsDoc.add(docList.get(i).getId_docente());
                if (i != docList.size()-1){
                    Docentes.append(", ");
                }
            }
        }else {
            Docentes.append("Agregar Docente");
        }
        docenteAddAct.setText(Docentes);

    }

    private void obtenerTarea() {
        List<Tarea> tarList = operacionesParalelo.obtenerTareasAsignadasParalelo(NomParalelo,IdAsignatura);
        StringBuilder Tareas = new StringBuilder();
        if (tarList.size() != 0 ){
            for (int i = 0; i < tarList.size(); i++){
                Tareas.append(tarList.get(i).getNombreTarea());
                IdsTar.add(tarList.get(i).getId_tarea());
                if (i != tarList.size()-1){
                    Tareas.append(", ");
                }
            }
        }else {
            Tareas.append("Agregar Tarea");
        }
        tareaAddAct.setText(Tareas);
    }

    private void obtenerEvaluacion() {

        List<Evaluacion> evaList = operacionesParalelo.obtenerEvaluacionesAsignadasParalalelo(NomParalelo,IdAsignatura);
        StringBuilder Evaluaciones = new StringBuilder();
        if (evaList.size() != 0 ){
            for (int i = 0; i < evaList.size(); i++){
                Evaluaciones.append(evaList.get(i).getNombreEvaluacion());
                IdsEva.add(evaList.get(i).getId_evaluacion());
                if (i != evaList.size()-1){
                    Evaluaciones.append(", ");
                }
            }
        }else {
            Evaluaciones.append("Agregar Evaluación");
        }
        evaluacionAddAct.setText(Evaluaciones);

    }

    private void llamarDialogAgregarMultiItems(String Componente, List<String> ListItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(Componente, ListItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(ParaleloActualizarActivity.this,22);
        agregarMultiItems.setCancelable(false);
        if (getFragmentManager() != null) {
            agregarMultiItems.show(getFragmentManager(),utilidades.ACTUALIZAR);
        }
    }

    private void llamarDialogAgregarSingleItem(String Componente, List<String> ListaItems, String ItemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(Componente, ListaItems, ItemAsignado);
        agregarSingleItem.setTargetFragment(ParaleloActualizarActivity.this,22);
        agregarSingleItem.setCancelable(false);
        if (getFragmentManager() != null) {
            agregarSingleItem.show(getFragmentManager(),utilidades.ACTUALIZAR);
        }
    }

    private void IdsComponentesSeleccionados(String Componente, List<String> ItemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < ItemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(ItemsSeleccionados.get(j))){
                    if (Componente.equals("Docente")){
                        if (!IdsDoc.contains(docenteList.get(i).getId_docente())){
                            docenteList.clear();
                            docenteList = operacionesDocente.listarDoc();
                            IdsDoc.add(docenteList.get(i).getId_docente());
                        }
                    }else if (Componente.equals("Tarea")){
                        if (!IdsTar.contains(tareaList.get(i).getId_tarea())){
                            tareaList.clear();
                            tareaList = operacionesTarea.ListarTar();
                            IdsTar.add(tareaList.get(i).getId_tarea());
                        }
                    }else {
                        if (!IdsEva.contains(evaluacionList.get(i).getId_evaluacion())){
                            evaluacionList.clear();
                            evaluacionList = operacionesEvaluacion.ListarEva();
                            IdsEva.add(evaluacionList.get(i).getId_evaluacion());
                        }
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
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onAgregarItems(List<String> ItemsSeleccionados, String Componente) {
        StringBuilder ItemsAsignados = new StringBuilder();
        switch (Componente){
            case "Docente":
                if(ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    docenteAddAct.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    docenteAddAct.setText(ItemsAsignados);
                }
                break;
            case "Tarea":
                if (ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    tareaAddAct.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    tareaAddAct.setText(ItemsAsignados);
                }
                break;
            case "Evaluación":
                if (ItemsSeleccionados.size() != 0){
                    ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
                    evaluacionAddAct.setText(ItemsAsignados);
                    IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
                }else {
                    ItemsAsignados.append(String.format("Agregar %s",Componente));
                    evaluacionAddAct.setText(ItemsAsignados);
                }
                break;
        }
    }

    @Override
    public void onRecibirItemAsignado(String Componente, String ItemAsignado) {
        switch (Componente) {
            case "Asignatura":
                asignaturaAddAct.setText(ItemAsignado);
                break;
            case "Horario":
                horarioAddAct.setText(ItemAsignado);
                break;
            case "Periodo":
                periodoAddAct.setText(ItemAsignado);
                break;
        }
    }
}
