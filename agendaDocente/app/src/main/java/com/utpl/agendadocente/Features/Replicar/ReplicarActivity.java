package com.utpl.agendadocente.Features.Replicar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
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

public class ReplicarActivity extends DialogFragment implements DialogSingleCheck.DialogSingleCheckListener, DialogMultiCheck.DialogMultiCheckListener,
        DocenteCreateListener, AsignaturaCreateListener, TareaCrearListener, HorarioCrearListener, PeriodoCreateListener, EvaluacionCrearListener {

    private static String NomParalelo;
    private static Integer IdAsignatura;
    private ReplicarParaleloListener replicarParaleloListener;

    private TextInputEditText nombre, alunmos;
    private TextView docenteRep, asignaturaRep, periodoRep, tareaRep, horarioRep, evaluacionRep;

    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesAsignatura  operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    private Paralelo paralelo;

    private List<Integer> IdsDoc = new ArrayList<>();
    private List<Integer> IdsTar = new ArrayList<>();
    private List<Integer> IdsEva = new ArrayList<>();

    private boolean [] estadoItemCheckDoc, estadoItemCheckTar, estadoItemCheckEva;
    private String [] listaItemSingleCheck;
    private String [] listaDocenteAsignados;
    private String [] listaTareaAsignadas;
    private String [] listaEvaluacionesAsignados;

    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();

    private List<EvaluacionAsignada> evaluacionAsignadas = operacionesParalelo.obtenerEvaluacionesPorEstado(NomParalelo,IdAsignatura);
    private List<DocenteAsignado> docenteAsignados = operacionesParalelo.obtenerDocentesPorEstado(NomParalelo,IdAsignatura);
    private List<TareaAsignada> tareaAsignadas = operacionesParalelo.obtenerTareasPorEstado(NomParalelo,IdAsignatura);

    private int positionItem = -1;
    private String nomPar ="";
    private int alumPar = 0;
    private int asigIdPar;
    private int hoIdPar;
    private int perIdPar;


    public ReplicarActivity(){}

    public static ReplicarActivity newInstance(String nomParalelo, Integer idAsignatura){
        NomParalelo = nomParalelo;
        IdAsignatura = idAsignatura;
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
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements ReplicarParaleloListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_replicar_paralelo,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarR);
        FloatingActionButton newDocente = view.findViewById(R.id.docenteAddRepBFA);
        FloatingActionButton newAsignatura = view.findViewById(R.id.asignaturaAddRepBFA);
        FloatingActionButton newTarea = view.findViewById(R.id.tareaAddRepBFA);
        FloatingActionButton newHorario = view.findViewById(R.id.horarioAddRepBFA);
        FloatingActionButton newPeriodo = view.findViewById(R.id.periodoAddRepBFA);
        FloatingActionButton newEvaluacion = view.findViewById(R.id.evaluacionAddRepBFA);
        nombre = view.findViewById(R.id.nomParRep);
        alunmos = view.findViewById(R.id.numAluRep);
        docenteRep = view.findViewById(R.id.docenteRep);
        asignaturaRep = view.findViewById(R.id.asignaturaRep);
        periodoRep = view.findViewById(R.id.periodoRep);
        tareaRep = view.findViewById(R.id.tareaRep);
        horarioRep = view.findViewById(R.id.horarioRep);
        evaluacionRep = view.findViewById(R.id.evaluacionRep);

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

        paralelo = operacionesParalelo.obtenerPar(NomParalelo,IdAsignatura);

        if (paralelo!=null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNum_estudiantes()));

            obtenerDocentes();
            docenteRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogMultiChecks(listaDocenteAsignados,estadoItemCheckDoc,"Docente");
                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listaItemSingleCheck = new String[asignaturaList.size()];
                    for (int i = 0; i < asignaturaList.size();i++){
                        listaItemSingleCheck[i] = asignaturaList.get(i).getNombreAsignatura();
                        if (asignaturaRep.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }
                    llamarDialogSingleCheck(listaItemSingleCheck,"Asignatura",positionItem);
                }
            });

            obtenerHorario(paralelo.getHoraioID());
            horarioRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listaItemSingleCheck = new String[horarioList.size()];
                    for (int i = 0; i < horarioList.size();i++){
                        listaItemSingleCheck[i] = String.format("%s - %s", horarioList.get(i).getHora_entrada(), horarioList.get(i).getHora_salida());
                        if (horarioRep.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }
                    llamarDialogSingleCheck(listaItemSingleCheck,"Horario",positionItem);
                }
            });

            obtenerPeriodo(paralelo.getPeriodoID());
            periodoRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listaItemSingleCheck = new String[periodoAcademicoList.size()];
                    for (int i = 0; i < periodoAcademicoList.size();i++){
                        listaItemSingleCheck[i] = String.format("%s - %s",periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
                        if (periodoRep.getText().toString().equals(listaItemSingleCheck[i])){
                            positionItem = i;
                        }
                    }
                    llamarDialogSingleCheck(listaItemSingleCheck,"Periodo",positionItem);
                }
            });

            obtenerTarea();
            tareaRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogMultiChecks(listaTareaAsignadas,estadoItemCheckTar,"Tarea");
                }
            });

            obtenerEvaluacion();
            evaluacionRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogMultiChecks(listaEvaluacionesAsignados,estadoItemCheckEva,"Evaluación");
                }
            });

            //***** LLamada de cada FloatingActionButton para crear cada componente si no existe
            newDocente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoDocente();
                }
            });

            newAsignatura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoAsignatura();
                }
            });

            newEvaluacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoEvaluacion();
                }
            });

            newHorario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoHorario();
                }
            });

            newPeriodo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoPeriodo();
                }
            });

            newTarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llamarDialogoCrearNuevoTarea();
                }
            });
            // fin de las llamadas de FloatingActionButton

            //Funcionamiento para replicar el paralelo
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    nomPar = Objects.requireNonNull(nombre.getText()).toString();
                    alumPar = Integer.parseInt(Objects.requireNonNull(alunmos.getText()).toString());
                    asigIdPar = -1;
                    for (int i = 0; i < asignaturaList.size(); i++){
                        if (asignaturaRep.getText().toString().equals(asignaturaList.get(i).getNombreAsignatura())){
                            asigIdPar = asignaturaList.get(i).getId_asignatura();
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

                    if (!nomPar.isEmpty()){
                        if (nomPar.length() == 1) {
                            if(alumPar != 0){
                                if (asigIdPar != -1){
                                    paralelo.setNombreParalelo(nomPar);
                                    paralelo.setNum_estudiantes(alumPar);
                                    paralelo.setAsignaturaID(asigIdPar);
                                    paralelo.setHoraioID(hoIdPar);
                                    paralelo.setPeriodoID(perIdPar);
                                    long insercion = operacionesParalelo.InsertarPar(paralelo,IdsDoc, IdsTar, IdsEva);
                                    if (insercion > 0){
                                        replicarParaleloListener.onReplicarParalelo(paralelo);
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
        List<Docente> docListRepli = operacionesParalelo.obtenerDocentesAsignadosParalelo(NomParalelo,IdAsignatura);
        StringBuilder Docentes = new StringBuilder();
        if (docListRepli.size() != 0){
            for (int i = 0; i< docListRepli.size(); i++){
                Docentes.append(String.format("%s %s", docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente()));
                if (i != docListRepli.size()-1){
                    Docentes.append("\n");
                }
            }
        }else {
            Docentes.append("Agregar Docente");
        }
        docenteRep.setText(Docentes);

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
            horarioRep.setText(mensaje);
        }
    }

    private void obtenerTarea() {
        List<Tarea> tareaListRepli = operacionesParalelo.obtenerTareasAsignadasParalelo(NomParalelo,IdAsignatura);
        StringBuilder Tareas = new StringBuilder();
        if (tareaListRepli.size() != 0){
            for (int i = 0; i< tareaListRepli.size(); i++){
                Tareas.append(tareaListRepli.get(i).getNombreTarea());
                if (i != tareaListRepli.size()-1){
                    Tareas.append("\n");
                }
            }
        }else {
            Tareas.append("Agregar Tarea");
        }
        tareaRep.setText(Tareas);

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
        List<Evaluacion> evaListRepli = operacionesParalelo.obtenerEvaluacionesAsignadasParalalelo(NomParalelo,IdAsignatura);
        StringBuilder Evaluaciones = new StringBuilder();
        if(evaListRepli.size() != 0) {
            for (int i = 0; i < evaListRepli.size(); i++){
                Evaluaciones.append(evaListRepli.get(i).getNombreEvaluacion());
                if (i != evaListRepli.size()-1){
                    Evaluaciones.append("\n");
                }
            }
        }else {
            Evaluaciones.append("Agregar Evaluación");
        }
        evaluacionRep.setText(Evaluaciones);

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
        dialogMultiCheck.setTargetFragment(ReplicarActivity.this,22);
        if (getFragmentManager() != null) {
            dialogMultiCheck.show(getFragmentManager(), "tag");
        }
    }
    private void llamarDialogSingleCheck(String [] listaItemChecks, String Componente, int position){
        DialogSingleCheck dialogSingleCheck = DialogSingleCheck.newInstance( listaItemChecks,Componente,position);
        dialogSingleCheck.setCancelable(false);
        dialogSingleCheck.setTargetFragment(ReplicarActivity.this,22);
        if (getFragmentManager() != null) {
            dialogSingleCheck.show(getFragmentManager(),"tag");
        }
    }

    private void llamarDialogoCrearNuevoDocente() {
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance("Nuevo Docente",null);
        crearDocenteActivity.setTargetFragment(ReplicarActivity.this,22);
        crearDocenteActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            crearDocenteActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoAsignatura() {
        AsignaturaCrearActivity asignaturaCrearActivity = AsignaturaCrearActivity.newInstance("Nuevo Asignatura",null);
        asignaturaCrearActivity.setTargetFragment(ReplicarActivity.this,22);
        asignaturaCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            asignaturaCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoEvaluacion() {
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nuevo Evaluación",null);
        evaluacionCrearActivity.setTargetFragment(ReplicarActivity.this,22);
        evaluacionCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            evaluacionCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoHorario() {
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Nuevo Horario",null);
        horarioCrearActivity.setTargetFragment(ReplicarActivity.this,22);
        horarioCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            horarioCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoPeriodo() {
        PeriodoCrearActivity periodoCrearActivity = PeriodoCrearActivity.newInstance("Nuevo Periodo",null);
        periodoCrearActivity.setTargetFragment(ReplicarActivity.this,22);
        periodoCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            periodoCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoTarea() {
        TareaCrearActivity tareaCrearActivity = TareaCrearActivity.newInstance("Nuevo Tarea",null);
        tareaCrearActivity.setTargetFragment(ReplicarActivity.this,22);
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
        }
    }

    @Override
    public void resutadoDialogMultiCheck(StringBuilder item, List<String> listaItems, String componente, boolean[] est) {
        switch (componente){
            case "Docente":
                docenteRep.setText(item);
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
                tareaRep.setText(item);
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
                evaluacionRep.setText(item);
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
                asignaturaRep.setText(item);
                break;
            case "Horario":
                horarioRep.setText(item);
                break;
            case "Periodo":
                periodoRep.setText(item);
                break;
        }
    }

    @Override
    public void onCrearAsignatura(Asignatura asignatura) {
        asignaturaList.add(asignatura);
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
