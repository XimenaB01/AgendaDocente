package com.utpl.agendadocente.ui.paralelo.Replicar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Docente;
import com.utpl.agendadocente.Model.Evaluacion;
import com.utpl.agendadocente.Model.Horario;
import com.utpl.agendadocente.Model.Paralelo;
import com.utpl.agendadocente.Model.PeriodoAcademico;
import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarSingleItem;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplicarActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener {

    private static long IdParalelo;
    private ReplicarParaleloListener replicarParaleloListener;

    private TextInputEditText nombre, alunmos;
    private TextView asignaturaRep, periodoRep, horarioRep;
    private Button docenteRep;
    private CheckBox checkBoxTar, checkBoxEva;
    private RecyclerView recyclerView;

    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesAsignatura  operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    private Paralelo paralelo;
    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();

    private List<Integer> IdsDoc = new ArrayList<>();
    private List<String> docenteListAsignados = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();

    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();
    private List<Docente> docenteList = operacionesDocente.listarDoc();
    private String tipoComponente = "";
    private String itemAgregado = "";
    private String [] itemsAgregados;
    private String nomPar ="";
    private int alumPar = 0;
    private int asigIdPar;
    private int hoIdPar;
    private int perIdPar;


    public ReplicarActivity(){}

    public static ReplicarActivity newInstance(Integer IDParalelo){
        IdParalelo = IDParalelo;
        ReplicarActivity replicar = new ReplicarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title","Replicar paralelo");
        replicar.setArguments(bundle);
        replicar.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        return replicar;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            replicarParaleloListener = (ReplicarParaleloListener) getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(requireActivity().toString() + " must implements ReplicarParaleloListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_replicar_paralelo,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarR);
        nombre = view.findViewById(R.id.nomParRep);
        alunmos = view.findViewById(R.id.numAluRep);
        docenteRep = view.findViewById(R.id.docenteRep);
        asignaturaRep = view.findViewById(R.id.asignaturaRep);
        periodoRep = view.findViewById(R.id.periodoRep);
        horarioRep = view.findViewById(R.id.horarioRep);
        recyclerView = view.findViewById(R.id.recyclerRepDoc);
        checkBoxEva = view.findViewById(R.id.replicarEvaluaciones);
        checkBoxTar = view.findViewById(R.id.replicarTareas);

        obtenerDocentes();
        evaluacionCrearActivity.llenarRecycleView(recyclerView, getContext(), docenteListAsignados);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.guardar);

        paralelo = operacionesParalelo.obtenerPar(IdParalelo);

        if (paralelo != null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNum_estudiantes()));

            docenteRep.setOnClickListener(new View.OnClickListener() {
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

                    if (docenteListAsignados.size() != itemsAgregados.length){
                        itemsAgregados = new String[docenteListAsignados.size()];

                        for (int i = 0; i < docenteListAsignados.size(); i++){
                            itemsAgregados[i] = docenteListAsignados.get(i);
                        }
                    }

                    //verifica si en la variable docentes no existe las siguiente cadena "Agregar Docente"
                    if (itemsAgregados.length != 0){
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }else {
                        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
                    }
                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemMultiCkeck.clear();
                    tipoComponente = "Asignatura";
                    for (int i = 0; i < asignaturaList.size(); i++){
                        if (!listItemMultiCkeck.contains(asignaturaList.get(i).getNombreAsignatura())){
                            listItemMultiCkeck.add(asignaturaList.get(i).getNombreAsignatura());
                        }
                    }
                    String Asignatura = asignaturaRep.getText().toString();
                    if (!Asignatura.equals("Agregar Asignatura")){
                        itemAgregado = Asignatura;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }
                }
            });

            obtenerHorario(paralelo.getHoraioID());
            horarioRep.setOnClickListener(new View.OnClickListener() {
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
                    String Horario = horarioRep.getText().toString();
                    if (!Horario.equals("Agregar Horario")){
                        itemAgregado = Horario;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }
                }
            });

            obtenerPeriodo(paralelo.getPeriodoID());
            periodoRep.setOnClickListener(new View.OnClickListener() {
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
                    String Periodo = periodoRep.getText().toString();
                    if (!Periodo.equals("Agregar Periodo")){
                        itemAgregado = Periodo;
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }else {
                        llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
                    }
                }
            });

            //Funcionamiento para replicar el paralelo
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    nomPar = Objects.requireNonNull(nombre.getText()).toString();
                    alumPar = Integer.parseInt(Objects.requireNonNull(alunmos.getText()).toString());

                    asigIdPar = -1;
                    for (int i = 0; i < asignaturaList.size(); i++){
                        if (asignaturaRep.getText().equals(asignaturaList.get(i).getNombreAsignatura())){
                            asigIdPar = asignaturaList.get(i).getId_asignatura();
                            Log.e("idA", asigIdPar+"");
                        }
                    }

                    hoIdPar = -1;
                    for (int i = 0; i < horarioList.size(); i++){
                        String horario = String.format("%s - %s",horarioList.get(i).getHora_entrada(),horarioList.get(i).getHora_salida());
                        if (horarioRep.getText().toString().equals(horario)){
                            hoIdPar = horarioList.get(i).getId_horario();
                        }
                    }

                    perIdPar = -1;
                    for (int i = 0; i < periodoAcademicoList.size(); i++){
                        String evaluacion = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (periodoRep.getText().toString().equals(evaluacion)){
                            perIdPar = periodoAcademicoList.get(i).getId_periodo();
                        }
                    }

                    List<Tarea> listTarerasReply = new ArrayList<>();

                    if (checkBoxTar.isChecked()){
                        OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
                        listTarerasReply = operacionesTarea.obtenerTareasId(IdParalelo);
                    }

                    List<Evaluacion> listEvaluacionesReply = new ArrayList<>();
                    if (checkBoxEva.isChecked()){
                        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
                        listEvaluacionesReply = operacionesEvaluacion.obtenerEvaluacionesId(IdParalelo);
                    }

                    if (!nomPar.isEmpty()){
                        if (nomPar.length() == 1) {
                            if(alumPar != 0){
                                if (asigIdPar != -1){
                                    paralelo.setNombreParalelo(nomPar);
                                    paralelo.setNum_estudiantes(alumPar);
                                    paralelo.setAsignaturaID(asigIdPar);
                                    paralelo.setHoraioID(hoIdPar);
                                    paralelo.setPeriodoID(perIdPar);

                                    long insercion = operacionesParalelo.InsertarPar(paralelo,IdsDoc);
                                    if (insercion > 0){
                                        int Id = (int)insercion;
                                        paralelo.setId_paralelo(Id);
                                        replicarParaleloListener.onReplicarParalelo(paralelo);

                                        for (int i = 0; i < listTarerasReply.size(); i++){
                                            OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
                                            Tarea tarea = new Tarea();
                                            tarea.setNombreTarea(listTarerasReply.get(i).getNombreTarea());
                                            tarea.setDescripcionTarea(listTarerasReply.get(i).getDescripcionTarea());
                                            tarea.setEstadoTarea(listTarerasReply.get(i).getEstadoTarea());
                                            tarea.setFechaTarea(listTarerasReply.get(i).getFechaTarea());
                                            tarea.setObservacionTarea(listTarerasReply.get(i).getObservacionTarea());
                                            tarea.setParaleloId(Id);
                                            operacionesTarea.InsertarTar(tarea);
                                        }

                                        for (int i = 0; i < listEvaluacionesReply.size(); i++){
                                            OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
                                            Evaluacion evaluacion = new Evaluacion();
                                            evaluacion.setNombreEvaluacion(listEvaluacionesReply.get(i).getNombreEvaluacion());
                                            evaluacion.setBimestre(listEvaluacionesReply.get(i).getBimestre());
                                            evaluacion.setFechaEvaluacion(listEvaluacionesReply.get(i).getFechaEvaluacion());
                                            evaluacion.setObservacion(listEvaluacionesReply.get(i).getObservacion());
                                            evaluacion.setTipo(listEvaluacionesReply.get(i).getTipo());
                                            evaluacion.setCuestionarioID(listEvaluacionesReply.get(i).getCuestionarioID());
                                            evaluacion.setParaleloID(Id);
                                            operacionesEvaluacion.InsertarEva(evaluacion);
                                        }
                                        dismiss();
                                    }else {
                                        Toast.makeText(getContext(),"Ya existe este Paralelo!!",Toast.LENGTH_LONG).show();
                                    }

                                }else {
                                    Toast.makeText(getContext(),"Agrege una Asignatura",Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getContext(),"EL número de estudiantes no puede ser 0!!",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getContext(),"El nombre del paralelo debe ser una letra",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"Ingrese el nombre del Paralelo",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });

        }else {
            Toast.makeText(getContext(),"Paralelo no existe",Toast.LENGTH_LONG).show();
        }

        return view;    }

    private void obtenerDocentes(){
        List<Docente> docListRepli = operacionesParalelo.obtenerDocentesAsignadosParalelo(IdParalelo);
        itemsAgregados = new String[docListRepli.size()];
        if (docListRepli.size() != 0){
            for (int i = 0; i< docListRepli.size(); i++){
                docenteListAsignados.add(String.format("%s %s", docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente()));
                itemsAgregados[i] = String.format("%s %s", docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente());
                IdsDoc.add(docListRepli.get(i).getId_docente());
            }
        }
    }

    private void obtenerAsignaturas(Integer IdAsignatura){
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(IdAsignatura);
        asignaturaRep.setText(asignatura.getNombreAsignatura());

    }

    private void obtenerHorario(Integer IdHOrario){
        if (IdHOrario != -1){
            Horario horario = operacionesHorario.obtenerHor(IdHOrario);
            horarioRep.setText(String.format("%s - %s",horario.getHora_entrada(),horario.getHora_salida()));
        }else {
            String mensaje = "Agregar Horario";
            horarioRep.setText(mensaje);
        }

    }

    private void obtenerPeriodo(Integer IdPeriodo) {
        if (IdPeriodo != -1) {
            PeriodoAcademico periodoAcademico = operacionesPeriodo.obtenerPer(IdPeriodo);
            periodoRep.setText(String.format("%s - %s", periodoAcademico.getFechaInicio(), periodoAcademico.getFechaFin()));
        }else {
            String mensaje = "Agregar Periodo";
            periodoRep.setText(mensaje);
        }
    }

    private void llamarDialogAgregarMultiItems(String Componente, List<String> ListItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(Componente, ListItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(ReplicarActivity.this,22);
        agregarMultiItems.setCancelable(false);
        agregarMultiItems.show(getParentFragmentManager(),utilidades.ACTUALIZAR);
    }

    private void llamarDialogAgregarSingleItem(String Componente, List<String> ListaItems, String ItemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(Componente, ListaItems, ItemAsignado);
        agregarSingleItem.setTargetFragment(ReplicarActivity.this,22);
        agregarSingleItem.setCancelable(false);
        agregarSingleItem.show(getParentFragmentManager(),utilidades.ACTUALIZAR);
    }

    private void IdsComponentesSeleccionados(String Componente, List<String> ItemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < ItemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(ItemsSeleccionados.get(j))){
                    if (Componente.equals("Docente")){
                        if (!IdsDoc.contains(docenteList.get(i).getId_docente())){
                            IdsDoc.add(docenteList.get(i).getId_docente());
                        }
                    }
                }
            }
        }
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
        }
    }

    @Override
    public void onAgregarItems(List<String> ItemsSeleccionados, String Componente) {

        if(ItemsSeleccionados.size() != 0){
            itemsAgregados = new String[ItemsSeleccionados.size()];
            IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
            docenteListAsignados = ItemsSeleccionados;
            for (int i = 0; i < ItemsSeleccionados.size(); i++){
                itemsAgregados[i] = ItemsSeleccionados.get(i);
            }
            evaluacionCrearActivity.llenarRecycleView(recyclerView, getContext(), docenteListAsignados);
        }else {
            IdsDoc.clear();
        }
    }

    @Override
    public void onRecibirItemAsignado(String Componente, String ItemAsignado) {
        switch (Componente) {
            case "Asignatura":
                asignaturaRep.setText(ItemAsignado);
                asignaturaList.clear();
                asignaturaList = operacionesAsignatura.ListarAsig();
                break;
            case "Horario":
                horarioRep.setText(ItemAsignado);
                horarioList.clear();
                horarioList = operacionesHorario.ListarHor();
                break;
            case "Periodo":
                periodoRep.setText(ItemAsignado);
                periodoAcademicoList.clear();
                periodoAcademicoList = operacionesPeriodo.ListarPer();
                break;
        }
    }

}
