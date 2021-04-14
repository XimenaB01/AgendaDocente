package com.utpl.agendadocente.ui.paralelo.replicar_paralelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.database.OperacionesTarea;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.ui.evaluacion.crear_evaluacion.ParaleloAsigadoAdapter;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.ui.paralelo.DialogAgregarSingleItem;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.paralelo.actualizar_paralelo.ParaleloActualizarActivity;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.paralelo.IParalelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReplicarActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener {

    private static long idParalelo;
    private IParalelo.ReplicarParaleloListener replicarParaleloListener;

    private TextInputEditText nombre;
    private TextInputEditText alunmos;
    private TextView asignaturaRep;
    private TextView periodoRep;
    private TextView horarioRep;
    private CheckBox checkBoxTar;
    private CheckBox checkBoxEva;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Button docenteRep;

    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesAsignatura  operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    private Paralelo paralelo;
    private ParaleloActualizarActivity actualizarActivity = new ParaleloActualizarActivity();
    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();

    private List<Integer> idsDoc = new ArrayList<>();
    private List<String> docenteListAsignados = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private List<Tarea> listTarerasReply = new ArrayList<>();
    private List<Evaluacion> listEvaluacionesReply = new ArrayList<>();

    private List<Asignatura> asignaturaList = operacionesAsignatura.listarAsignatura();
    private List<Horario> horarioList = operacionesHorario.listarHorario();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.listarPeriodo();
    private List<Docente> docenteList = operacionesDocente.listarDocente();
    private String tipoComponente = "";
    private String itemAgregado = "";
    private String [] itemsAgregados;
    private String nomPar ="";
    private String formatoDocente = "%s %s";
    private String formatoPeriodo = "%s - %s";
    private String formatoHorario = "%s Aula:%s De:%s A:%s";
    private int alumPar = 0;
    private int asigIdPar;
    private int hoIdPar;
    private int perIdPar;


    public ReplicarActivity(){
        //Required constructor
    }

    public static ReplicarActivity newInstance(Integer idparalelo){
        idParalelo = idparalelo;
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
            replicarParaleloListener = (IParalelo.ReplicarParaleloListener) getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(requireActivity().toString() + " must implements ReplicarParaleloListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_replicar_paralelo,container,false);

        toolbar = view.findViewById(R.id.toolbarR);
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
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);
        llenarFormulario();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.guardar);

        return view;    }

    private void llenarFormulario() {
        paralelo = operacionesParalelo.obtenerParalelo(idParalelo);

        if (paralelo != null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNumEstudiantes()));

            docenteRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForDocente();
                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForAsignatura();
                }
            });

            obtenerHorario(paralelo.getHoraioID());
            horarioRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForHorario();
                }
            });

            obtenerPeriodo(paralelo.getPeriodoID());
            periodoRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForPeriodo();
                }
            });

            //Funcionamiento para replicar el paralelo
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    nomPar = Objects.requireNonNull(nombre.getText()).toString();
                    alumPar = Integer.parseInt(Objects.requireNonNull(alunmos.getText()).toString());

                    asigIdPar = -1;
                    obtenerIdAsignatura();

                    hoIdPar = -1;
                    obtenerIdHorario();

                    perIdPar = -1;
                    obtenerIdPeriodo();

                    if (checkBoxTar.isChecked()){
                        OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
                        listTarerasReply = operacionesTarea.obtenerTareasId(idParalelo);
                    }

                    if (checkBoxEva.isChecked()){
                        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
                        listEvaluacionesReply = operacionesEvaluacion.obtenerEvaluacionesId(idParalelo);
                    }

                    validarDatosParaleloReplicado();

                    return true;
                }
            });

        }else {
            Toast.makeText(getContext(),"Paralelo no existe",Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerIdPeriodo() {
        for (int i = 0; i < periodoAcademicoList.size(); i++){
            String evaluacion = String.format(formatoPeriodo,periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
            if (periodoRep.getText().toString().equals(evaluacion)){
                perIdPar = periodoAcademicoList.get(i).getIdPeriodo();
            }
        }
    }

    private void obtenerIdHorario() {
        for (int i = 0; i < horarioList.size(); i++){
            String horario = String.format(formatoHorario, horarioList.get(i).getDia(), horarioList.get(i).getAula(), horarioList.get(i).getHoraEntrada(),horarioList.get(i).getHoraSalida());
            if (horarioRep.getText().toString().equals(horario)){
                hoIdPar = horarioList.get(i).getIdHorario();
            }
        }
    }

    private void obtenerIdAsignatura() {
        for (int i = 0; i < asignaturaList.size(); i++){
            if (asignaturaRep.getText().equals(asignaturaList.get(i).getNombreAsignatura())){
                asigIdPar = asignaturaList.get(i).getIdAsignatura();
            }
        }
    }

    private void validarDatosParaleloReplicado() {
        if (!nomPar.isEmpty()){
            if (nomPar.length() == 1) {
                if(alumPar != 0){
                    if (asigIdPar != -1){
                        guardarParaleloReplicado();
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
    }

    private void guardarParaleloReplicado() {
        paralelo.setNombreParalelo(nomPar);
        paralelo.setNumEstudiantes(alumPar);
        paralelo.setAsignaturaID(asigIdPar);
        paralelo.setHoraioID(hoIdPar);
        paralelo.setPeriodoID(perIdPar);

        long insercion = operacionesParalelo.insertarParalelo(paralelo, idsDoc);
        if (insercion > 0){
            int id = (int)insercion;
            paralelo.setIdParalelo(id);
            replicarParaleloListener.onReplicarParalelo(paralelo);
            replicarTareasForParalelo(id);
            replicarEvaluacionesForParalelo(id);
            dismiss();
        }else {
            Toast.makeText(getContext(),"Ya existe este Paralelo!!",Toast.LENGTH_LONG).show();
        }
    }

    private void replicarTareasForParalelo(int id){
        for (int i = 0; i < listTarerasReply.size(); i++){
            OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
            Tarea tarea = new Tarea();
            tarea.setNombreTarea(listTarerasReply.get(i).getNombreTarea());
            tarea.setDescripcionTarea(listTarerasReply.get(i).getDescripcionTarea());
            tarea.setEstadoTarea(listTarerasReply.get(i).getEstadoTarea());
            tarea.setFechaTarea(listTarerasReply.get(i).getFechaTarea());
            tarea.setObservacionTarea(listTarerasReply.get(i).getObservacionTarea());
            tarea.setParaleloId(id);
            operacionesTarea.insertarTarea(tarea);
        }

    }

    private void replicarEvaluacionesForParalelo(int id){
        for (int i = 0; i < listEvaluacionesReply.size(); i++){
            OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setNombreEvaluacion(listEvaluacionesReply.get(i).getNombreEvaluacion());
            evaluacion.setBimestre(listEvaluacionesReply.get(i).getBimestre());
            evaluacion.setFechaEvaluacion(listEvaluacionesReply.get(i).getFechaEvaluacion());
            evaluacion.setObservacion(listEvaluacionesReply.get(i).getObservacion());
            evaluacion.setTipo(listEvaluacionesReply.get(i).getTipo());
            evaluacion.setCuestionarioID(listEvaluacionesReply.get(i).getCuestionarioID());
            evaluacion.setParaleloID(id);
            operacionesEvaluacion.insertarEvaluacion(evaluacion);
        }
    }

    private void llenarListaForPeriodo() {
        listItemMultiCkeck.clear();
        tipoComponente = "Periodo";
        for (int i = 0; i < periodoAcademicoList.size(); i++){
            String periodo = String.format(formatoPeriodo,periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
            if (!listItemMultiCkeck.contains(periodo)){
                listItemMultiCkeck.add(periodo);
            }
        }
        String periodoTexto = periodoRep.getText().toString();
        if (!periodoTexto.equals("Agregar Periodo")){
            itemAgregado = periodoTexto;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    private void llenarListaForHorario() {
        listItemMultiCkeck.clear();
        tipoComponente = "Horario";

        listItemMultiCkeck = actualizarActivity.getListaHorarios(formatoHorario, horarioList);

        String horarioTexto = horarioRep.getText().toString();
        if (!horarioTexto.equals("Agregar Horario")){
            itemAgregado = horarioTexto;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    private void llenarListaForAsignatura() {
        listItemMultiCkeck.clear();
        tipoComponente = "Asignatura";
        for (int i = 0; i < asignaturaList.size(); i++){
            if (!listItemMultiCkeck.contains(asignaturaList.get(i).getNombreAsignatura())){
                listItemMultiCkeck.add(asignaturaList.get(i).getNombreAsignatura());
            }
        }
        String asignaturaTexto = asignaturaRep.getText().toString();
        if (!asignaturaTexto.equals("Agregar Asignatura")){
            itemAgregado = asignaturaTexto;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    private void llenarListaForDocente() {
        listItemMultiCkeck.clear();
        tipoComponente = "Docente";

        listItemMultiCkeck = actualizarActivity.getListaNombresDocente(formatoDocente, docenteList);
        //llena la lista con los nombres y apellidos de cada docente

        if (docenteListAsignados.size() != itemsAgregados.length){
            itemsAgregados = new String[docenteListAsignados.size()];
            for (int i = 0; i < docenteListAsignados.size(); i++){
                itemsAgregados[i] = docenteListAsignados.get(i);
            }
        }

        //verifica si en la variable docentes no existe las siguiente cadena "Agregar Docente"
        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
    }

    private void obtenerDocentes(){
        List<Docente> docListRepli = operacionesParalelo.obtenerDocentesAsignadosParalelo(idParalelo);
        itemsAgregados = new String[docListRepli.size()];
        if (!docListRepli.isEmpty()){
            for (int i = 0; i< docListRepli.size(); i++){
                docenteListAsignados.add(String.format(formatoDocente, docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente()));
                itemsAgregados[i] = String.format(formatoDocente, docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente());
                idsDoc.add(docListRepli.get(i).getIdDocente());
            }
        }
    }

    private void obtenerAsignaturas(Integer idAsignatura){
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(idAsignatura);
        asignaturaRep.setText(asignatura.getNombreAsignatura());

    }

    private void obtenerHorario(Integer idHorario){
        if (idHorario != -1){
            Horario horario = operacionesHorario.obtenerHorario(idHorario);
            horarioRep.setText(String.format(formatoHorario, horario.getDia(), horario.getAula(), horario.getHoraEntrada(),horario.getHoraSalida()));
        }else {
            String mensaje = "Agregar Horario";
            horarioRep.setText(mensaje);
        }

    }

    private void obtenerPeriodo(Integer idPeriodo) {
        if (idPeriodo != -1) {
            PeriodoAcademico periodoAcademico = operacionesPeriodo.obtenerPeriodo(idPeriodo);
            periodoRep.setText(String.format(formatoPeriodo, periodoAcademico.getFechaInicio(), periodoAcademico.getFechaFin()));
        }else {
            String mensaje = "Agregar Periodo";
            periodoRep.setText(mensaje);
        }
    }

    private void llamarDialogAgregarMultiItems(String componente, List<String> listItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(componente, listItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(ReplicarActivity.this,22);
        agregarMultiItems.setCancelable(false);
        agregarMultiItems.show(getParentFragmentManager(), Utilidades.ACTUALIZAR);
    }

    private void llamarDialogAgregarSingleItem(String componente, List<String> listaItems, String itemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(componente, listaItems, itemAsignado);
        agregarSingleItem.setTargetFragment(ReplicarActivity.this,22);
        agregarSingleItem.setCancelable(false);
        agregarSingleItem.show(getParentFragmentManager(), Utilidades.ACTUALIZAR);
    }

    private void idsComponentesSeleccionados(String componente, List<String> itemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < itemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(itemsSeleccionados.get(j)) && componente.equals("Docente") && !idsDoc.contains(docenteList.get(i).getIdDocente())){
                    idsDoc.add(docenteList.get(i).getIdDocente());
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
    public void onAgregarItems(List<String> itemsSeleccionados, String componente) {

        if(!itemsSeleccionados.isEmpty()){
            itemsAgregados = new String[itemsSeleccionados.size()];
            idsComponentesSeleccionados(componente,itemsSeleccionados);
            docenteListAsignados = itemsSeleccionados;
            for (int i = 0; i < itemsSeleccionados.size(); i++){
                itemsAgregados[i] = itemsSeleccionados.get(i);
            }
            evaluacionCrearActivity.llenarRecycleView(recyclerView, getContext(), docenteListAsignados);
        }else {
            idsDoc.clear();
        }
    }

    @Override
    public void onRecibirItemAsignado(String componente, String itemAsignado) {
        switch (componente) {
            case "Asignatura":
                asignaturaRep.setText(itemAsignado);
                asignaturaList.clear();
                asignaturaList = operacionesAsignatura.listarAsignatura();
                break;
            case "Horario":
                horarioRep.setText(itemAsignado);
                horarioList.clear();
                horarioList = operacionesHorario.listarHorario();
                break;
            case "Periodo":
                periodoRep.setText(itemAsignado);
                periodoAcademicoList.clear();
                periodoAcademicoList = operacionesPeriodo.listarPeriodo();
                break;
            default:
                //Ignore this part
                break;
        }
    }

}
