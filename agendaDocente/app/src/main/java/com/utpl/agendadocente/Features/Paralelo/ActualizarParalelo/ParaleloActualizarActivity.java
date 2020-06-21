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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.DocenteAsignado;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.EvaluacionAsignada;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Entidades.TareaAsignada;
import com.utpl.agendadocente.Features.Asignatura.CrearAsignatura.AsignaturaCrearActivity;
import com.utpl.agendadocente.Features.Asignatura.CrearAsignatura.AsignaturaCreateListener;
import com.utpl.agendadocente.Features.Docente.CrearDocente.CrearDocenteActivity;
import com.utpl.agendadocente.Features.Docente.CrearDocente.DocenteCreateListener;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearListener;
import com.utpl.agendadocente.Features.Horario.CrearHorario.HorarioCrearActivity;
import com.utpl.agendadocente.Features.Horario.CrearHorario.HorarioCrearListener;
import com.utpl.agendadocente.Features.Paralelo.DialogMultiCheck;
import com.utpl.agendadocente.Features.Paralelo.DialogSingleCheck;
import com.utpl.agendadocente.Features.Periodo.CrearPeriodo.PeriodoCrearActivity;
import com.utpl.agendadocente.Features.Periodo.CrearPeriodo.PeriodoCreateListener;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearActivity;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearListener;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParaleloActualizarActivity extends DialogFragment implements DialogMultiCheck.DialogMultiCheckListener, DialogSingleCheck.DialogSingleCheckListener,
        DocenteCreateListener, AsignaturaCreateListener, TareaCrearListener, HorarioCrearListener, PeriodoCreateListener, EvaluacionCrearListener {

    private static String NomParalelo;
    private static Integer IdAsignatura;
    private static int paraleloItemPosition;
    private static ActualizarParaleloListener actualizarParaleloListener;


    private TextInputEditText nombre, alunmos;
    private TextView docenteAddAct, tareaAddAct, evaluacionAddAct;
    private TextView asignaturaAddAct, horarioAddAct, periodoAddAct;

    private Paralelo paralelo;

    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());

    private List<Asignatura> asigList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();

    private int positionItem = -1;
    private boolean [] estadoItemCheckDoc, estadoItemCheckTar, estadoItemCheckEva;
    private String [] listaItemSingleCheck;
    private String [] listaDocenteAsignados;
    private String [] listaTareaAsignadas;
    private String [] listaEvaluacionesAsignados;
    private List<EvaluacionAsignada> evaluacionAsignadas = operacionesParalelo.obtenerEvaluacionesPorEstado(NomParalelo,IdAsignatura);
    private List<DocenteAsignado> docenteAsignados = operacionesParalelo.obtenerDocentesPorEstado(NomParalelo,IdAsignatura);
    private List<TareaAsignada> tareaAsignadas = operacionesParalelo.obtenerTareasPorEstado(NomParalelo,IdAsignatura);
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
        FloatingActionButton newDocenteAct = view.findViewById(R.id.docenteAddActBFA);
        FloatingActionButton newAsignaturaAct = view.findViewById(R.id.asignaturaAddActBFA);
        FloatingActionButton newTareaAct = view.findViewById(R.id.tareaAddActBFA);
        FloatingActionButton newHorarioAct = view.findViewById(R.id.horarioAddActBFA);
        FloatingActionButton newPeriodoAct = view.findViewById(R.id.periodoAddActBFA);
        FloatingActionButton newEvaluacionAct = view.findViewById(R.id.evaluacionAddActBFA);
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
                    llamarDialogMultiChecks(listaDocenteAsignados, estadoItemCheckDoc, "Docente");
                }
            });

            obtenerTarea();
            tareaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogMultiChecks(listaTareaAsignadas, estadoItemCheckTar, "Tarea");
                }
            });

            obtenerEvaluacion();
            evaluacionAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogMultiChecks(listaEvaluacionesAsignados, estadoItemCheckEva, "Evaluación");
                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listaItemSingleCheck = new String[asigList.size()];
                    for (int i = 0; i < asigList.size(); i++){
                        listaItemSingleCheck[i] = asigList.get(i).getNombreAsignatura();
                        if (asignaturaAddAct.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }
                    llamarDialogSingleCheck(listaItemSingleCheck,"Asignatura",positionItem);
                }
            });

            obtenerHorario(paralelo.getHoraioID());
            horarioAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listaItemSingleCheck = new String[horarioList.size()];
                    for (int i = 0; i < horarioList.size(); i++){
                        listaItemSingleCheck[i] = String.format("%s - %s", horarioList.get(i).getHora_entrada(), horarioList.get(i).getHora_salida());
                        if (horarioAddAct.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }

                    llamarDialogSingleCheck(listaItemSingleCheck, "Horario", positionItem);
                }
            });

            obtenerPeriodo(paralelo.getPeriodoID());
            periodoAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listaItemSingleCheck = new String[periodoAcademicoList.size()];
                    for (int i = 0; i < periodoAcademicoList.size(); i++){
                        listaItemSingleCheck[i] = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (periodoAddAct.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }

                    llamarDialogSingleCheck(listaItemSingleCheck,"Periodo",positionItem);
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

            newDocenteAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoDocente();
                }
            });

            newAsignaturaAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoAsignatura();
                }
            });

            newHorarioAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoHorario();
                }
            });

            newPeriodoAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoPeriodo();
                }
            });

            newTareaAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoTarea();
                }
            });

            newEvaluacionAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoEvaluacion();
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
                if (i != docList.size()-1){
                    Docentes.append("\n");
                }
            }
        }else {
            Docentes.append("Agregar Docente");
        }
        docenteAddAct.setText(Docentes);

        listaDocenteAsignados = new String[docenteAsignados.size()];
        estadoItemCheckDoc = new boolean[docenteAsignados.size()];
        for (int i = 0; i < docenteAsignados.size(); i++){
            listaDocenteAsignados[i] = String.format("%s %s", docenteAsignados.get(i).getNombreDocente(), docenteAsignados.get(i).getApellidoDocente());
            estadoItemCheckDoc[i] = docenteAsignados.get(i).getEstado();
            if (docenteAsignados.get(i).getEstado()){
                IdsDoc.add(docenteAsignados.get(i).getId_docente());
            }
        }
    }

    private void obtenerTarea() {
        List<Tarea> tarList = operacionesParalelo.obtenerTareasAsignadasParalelo(NomParalelo,IdAsignatura);
        StringBuilder Tareas = new StringBuilder();
        if (tarList.size() != 0 ){
            for (int i = 0; i < tarList.size(); i++){
                Tareas.append(tarList.get(i).getNombreTarea());
                if (i != tarList.size()-1){
                    Tareas.append("\n");
                }
            }
        }else {
            Tareas.append("Agregar Tarea");
        }
        tareaAddAct.setText(Tareas);

        listaTareaAsignadas = new String[tareaAsignadas.size()];
        estadoItemCheckTar = new boolean[tareaAsignadas.size()];
        for (int i = 0; i < tareaAsignadas.size(); i++){
            listaTareaAsignadas[i] = tareaAsignadas.get(i).getNombreTarea();
            estadoItemCheckTar[i] = tareaAsignadas.get(i).isEstado();
            if (tareaAsignadas.get(i).isEstado()){
                IdsTar.add(tareaAsignadas.get(i).getId_tarea());
            }
        }

    }

    private void obtenerEvaluacion() {

        List<Evaluacion> evaList = operacionesParalelo.obtenerEvaluacionesAsignadasParalalelo(NomParalelo,IdAsignatura);
        StringBuilder Evaluaciones = new StringBuilder();
        if (evaList.size() != 0 ){
            for (int i = 0; i < evaList.size(); i++){
                Evaluaciones.append(evaList.get(i).getNombreEvaluacion());
                if (i != evaList.size()-1){
                    Evaluaciones.append("\n");
                }
            }
        }else {
            Evaluaciones.append("Agregar Evaluación");
        }
        evaluacionAddAct.setText(Evaluaciones);

        listaEvaluacionesAsignados = new String[evaluacionAsignadas.size()];
        estadoItemCheckEva = new boolean[evaluacionAsignadas.size()];
        for (int i = 0; i < evaluacionAsignadas.size(); i++){
            listaEvaluacionesAsignados[i] = evaluacionAsignadas.get(i).getNombreEvaluacion();
            estadoItemCheckEva[i] = evaluacionAsignadas.get(i).isEstado();
            if (evaluacionAsignadas.get(i).isEstado()){
                IdsEva.add(evaluacionAsignadas.get(i).getId_evaluacion());
            }
        }
    }

    private void llamarDialogMultiChecks(String [] listaItemChecks, boolean [] estados,String Componente){
        DialogMultiCheck dialogMultiCheck = DialogMultiCheck.newInstance(listaItemChecks, estados, Componente);
        dialogMultiCheck.setCancelable(false);
        dialogMultiCheck.setTargetFragment(ParaleloActualizarActivity.this,22);
        if (getFragmentManager() != null) {
            dialogMultiCheck.show(getFragmentManager(), "tag");
        }
    }
    private void llamarDialogSingleCheck(String [] listaItemChecks, String Componente, int position){
        DialogSingleCheck dialogSingleCheck = DialogSingleCheck.newInstance( listaItemChecks,Componente,position);
        dialogSingleCheck.setCancelable(false);
        dialogSingleCheck.setTargetFragment(ParaleloActualizarActivity.this,22);
        if (getFragmentManager() != null) {
            dialogSingleCheck.show(getFragmentManager(),"tag");
        }
    }

    private void llamarDialogoCrearNuevoDocente() {
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance("Nuevo Docente",null);
        crearDocenteActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        crearDocenteActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            crearDocenteActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoAsignatura() {
        AsignaturaCrearActivity asignaturaCrearActivity = AsignaturaCrearActivity.newInstance("Nuevo Asignatura",null);
        asignaturaCrearActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        asignaturaCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            asignaturaCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoEvaluacion() {
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nuevo Evaluación",null);
        evaluacionCrearActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        evaluacionCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            evaluacionCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoHorario() {
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Nuevo Horario",null);
        horarioCrearActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        horarioCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            horarioCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoPeriodo() {
        PeriodoCrearActivity periodoCrearActivity = PeriodoCrearActivity.newInstance("Nuevo Periodo",null);
        periodoCrearActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        periodoCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            periodoCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoTarea() {
        TareaCrearActivity tareaCrearActivity = TareaCrearActivity.newInstance("Nuevo Tarea",null);
        tareaCrearActivity.setTargetFragment(ParaleloActualizarActivity.this,22);
        tareaCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            tareaCrearActivity.show(getFragmentManager(), utilidades.CREAR);
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
            dialog.setCancelable(false);
        }
    }

    @Override
    public void resutadoDialogMultiCheck(StringBuilder item, List<String> listaItems, String componente, boolean[] est) {
        switch (componente){
            case "Docente":
                docenteAddAct.setText(item);
                IdsDoc.clear();
                for (int i = 0; i < listaItems.size(); i++){
                    for (int y = 0; y < docenteAsignados.size(); y++){
                        String docente = String.format("%s %s",docenteAsignados.get(y).getNombreDocente(),docenteAsignados.get(y).getApellidoDocente());
                        if (listaItems.get(i).equals(docente)){
                            IdsDoc.add(docenteAsignados.get(y).getId_docente());
                        }
                    }
                }
                break;
            case "Tarea":
                tareaAddAct.setText(item);
                IdsTar.clear();
                for (int i = 0; i < listaItems.size(); i++){
                    for (int y = 0; y < tareaAsignadas.size(); y++){
                        String tarea = tareaAsignadas.get(y).getNombreTarea();
                        if (listaItems.get(i).equals(tarea)){
                            IdsTar.add(tareaAsignadas.get(y).getId_tarea());
                        }
                    }
                }
                break;
            case "Evaluación":
                evaluacionAddAct.setText(item);
                IdsEva.clear();
                for (int i = 0; i < listaItems.size(); i++){
                    for (int y = 0; y < evaluacionAsignadas.size(); y++){
                        String evaluacion = evaluacionAsignadas.get(y).getNombreEvaluacion();
                        if (listaItems.get(i).equals(evaluacion)){
                            IdsEva.add(evaluacionAsignadas.get(y).getId_evaluacion());
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void resutadoDialogSingle(String item, String componente) {
        switch (componente) {
            case "Asignatura":
                asignaturaAddAct.setText(item);
                break;
            case "Horario":
                horarioAddAct.setText(item);
                break;
            case "Periodo":
                periodoAddAct.setText(item);
                break;
        }
    }

    @Override
    public void onCrearAsignatura(Asignatura asignatura) {
        asigList.add(asignatura);
    }

    @Override
    public void onCrearDocente(Docente docente) {
        docenteAsignados = operacionesParalelo.obtenerDocentesPorEstado(NomParalelo,IdAsignatura);
        obtenerDocentes();
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        evaluacionAsignadas = operacionesParalelo.obtenerEvaluacionesPorEstado(NomParalelo,IdAsignatura);
        obtenerEvaluacion();
    }

    @Override
    public void onCrearHorario(Horario horario) {
        horarioList.add(horario);
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodoAcademico) {
        periodoAcademicoList.add(periodoAcademico);
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        tareaAsignadas = operacionesParalelo.obtenerTareasPorEstado(NomParalelo,IdAsignatura);
        obtenerTarea();
    }
}
