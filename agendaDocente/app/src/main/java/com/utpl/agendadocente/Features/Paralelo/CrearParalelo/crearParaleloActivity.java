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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Horario;
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

public class crearParaleloActivity extends DialogFragment implements DialogSingleCheck.DialogSingleCheckListener, DialogMultiCheck.DialogMultiCheckListener,
        DocenteCreateListener, AsignaturaCreateListener, TareaCrearListener, HorarioCrearListener, PeriodoCreateListener, EvaluacionCrearListener {

    private static ParaleloCrearListener paraleloCrearListener;

    private TextInputEditText nombre, alunmos;
    private TextView docenteAdd, tareaAdd, evaluacionAdd;
    private TextView asignaturaAdd, periodoAdd, horarioAdd;

    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());

    private boolean [] estadoItemCheck, estadoItemCheckTar, estadoItemCheckEva;
    private String [] listaItemsMultiChecksDocentes;
    private String [] listaItemsMultiChecksTareas;
    private String [] listaItemsMultiChecksEvaluaciones;
    private String [] listaItems;
    private List<Integer>IdsDoc = new ArrayList<>();
    private List<Integer>IdsTar = new ArrayList<>();
    private List<Integer>IdsEva = new ArrayList<>();
    private List<Docente> docList = operacionesDocente.listarDoc();
    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();
    private List<Tarea> tareaList = operacionesTarea.ListarTar();
    private List<Evaluacion> evaList = operacionesEvaluacion.ListarEva();

    private int posicionItem = -1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_paralelo, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarPar);
        FloatingActionButton newDocente = view.findViewById(R.id.docenteAddBFA);
        FloatingActionButton newAsignatura = view.findViewById(R.id.asignaturaAddBFA);
        FloatingActionButton newTarea = view.findViewById(R.id.tareaAddBFA);
        FloatingActionButton newHorario = view.findViewById(R.id.horarioAddBFA);
        FloatingActionButton newPeriodo = view.findViewById(R.id.periodoAddBFA);
        FloatingActionButton newEvaluacion = view.findViewById(R.id.evaluacionAddBFA);

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

        obtenerDocentes();
        docenteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogMultiChecks(listaItemsMultiChecksDocentes,estadoItemCheck,"Docente");
            }
        });


        asignaturaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaItems = new String[asignaturaList.size()];

                for (int i = 0; i < asignaturaList.size(); i++){
                    listaItems[i] = asignaturaList.get(i).getNombreAsignatura();
                    if (asignaturaAdd.getText().toString().equals(listaItems[i])){
                        posicionItem = i;
                    }
                }

                llamarDialogSingleCheck(listaItems,"Asignatura",  posicionItem);
            }
        });

        horarioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaItems = new String[horarioList.size()];

                for (int i = 0; i < horarioList.size(); i++){
                    listaItems[i] = String.format("%s - %s", horarioList.get(i).getHora_entrada(), horarioList.get(i).getHora_salida());
                    if (horarioAdd.getText().toString().equals(listaItems[i])){
                        posicionItem = i;
                    }
                }

                llamarDialogSingleCheck(listaItems,"Horario",  posicionItem);
            }
        });

        periodoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listaItems = new String[periodoAcademicoList.size()];

                for (int i = 0; i < periodoAcademicoList.size(); i++){
                    listaItems[i] = String.format("%s - %s", periodoAcademicoList.get(i).getFechaInicio(), periodoAcademicoList.get(i).getFechaFin());
                    if (periodoAdd.getText().toString().equals(listaItems[i])){
                        posicionItem = i;
                    }
                }

                llamarDialogSingleCheck(listaItems,"Periodo",  posicionItem);
            }
        });

        obtenerTarea();
        tareaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogMultiChecks(listaItemsMultiChecksTareas,estadoItemCheckTar,"Tarea");
            }
        });

        obtenerEvaluacion();
        evaluacionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogMultiChecks(listaItemsMultiChecksEvaluaciones,estadoItemCheckEva,"Evaluación");
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

      return view;
    }

    private void obtenerDocentes(){

        listaItemsMultiChecksDocentes = new String[docList.size()];

        for (int i = 0; i < docList.size(); i++){
            listaItemsMultiChecksDocentes[i] = String.format("%s %s",docList.get(i).getNombreDocente(),docList.get(i).getApellidoDocente());
        }

        estadoItemCheck = new boolean[listaItemsMultiChecksDocentes.length];
    }

    private void obtenerTarea() {
        listaItemsMultiChecksTareas = new String[tareaList.size()];

        for (int i = 0; i < tareaList.size(); i++){
            listaItemsMultiChecksTareas[i] = tareaList.get(i).getNombreTarea();
        }

        estadoItemCheckTar = new boolean[listaItemsMultiChecksTareas.length];
    }

    private void obtenerEvaluacion() {
        listaItemsMultiChecksEvaluaciones = new String[evaList.size()];

        for (int i = 0; i < evaList.size(); i++){
            listaItemsMultiChecksEvaluaciones[i] = evaList.get(i).getNombreEvaluacion();
        }

        estadoItemCheckEva = new boolean[listaItemsMultiChecksEvaluaciones.length];
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

        for (int i = 0; i < IdsDoc.size(); i++){
            Log.e("id"+i,IdsDoc.get(i)+"");
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

    private void llamarDialogSingleCheck(String [] listaItems, String Componente, int position){
        DialogSingleCheck dialogSingleCheck = DialogSingleCheck.newInstance(listaItems, Componente, position);
        dialogSingleCheck.setTargetFragment(crearParaleloActivity.this,22);
        dialogSingleCheck.setCancelable(false);
        if (getFragmentManager() != null) {
            dialogSingleCheck.show(getFragmentManager(),"tag");
        }
    }

    private void llamarDialogMultiChecks(String [] listaItems, boolean [] estadosLista, String Componente) {

        DialogMultiCheck dialogMultiCheck = DialogMultiCheck.newInstance(listaItems, estadosLista,Componente);
        dialogMultiCheck.setTargetFragment(crearParaleloActivity.this,22);
        dialogMultiCheck.setCancelable(false);
        if (getFragmentManager() != null) {
            dialogMultiCheck.show(getFragmentManager(),"tag");
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

    @Override
    public void resutadoDialogSingle(String item, String componente) {
        switch (componente) {
            case "Asignatura":
                asignaturaAdd.setText(item);
                break;
            case "Horario":
                horarioAdd.setText(item);
                break;
            case "Periodo":
                periodoAdd.setText(item);
                break;
        }
    }

    @Override
    public void resutadoDialogMultiCheck(StringBuilder item, List<String> listaItems, String componente, boolean [] estado) {
        switch (componente){
            case "Docente":
                docenteAdd.setText(item);
                estadoItemCheck = estado;
                IdsDoc.clear();
                for (int i = 0; i < listaItems.size();i++){
                    for (int y = 0; y <docList.size(); y++ ){
                        String docente = String.format("%s %s",docList.get(y).getNombreDocente(), docList.get(y).getApellidoDocente());
                        if (listaItems.get(i).equals(docente)){
                            IdsDoc.add(docList.get(y).getId_docente());
                        }
                    }
                }
                break;
            case "Tarea":
                tareaAdd.setText(item);
                estadoItemCheckTar = estado;
                IdsTar.clear();
                for (int i = 0; i < listaItems.size();i++){
                    for (int y = 0; y <tareaList.size(); y++ ){
                        String tarea = tareaList.get(y).getNombreTarea();
                        if (listaItems.get(i).equals(tarea)){
                            IdsTar.add(tareaList.get(y).getId_tarea());
                        }
                    }
                }
                break;
            case "Evaluación":
                evaluacionAdd.setText(item);
                estadoItemCheckEva = estado;
                IdsEva.clear();
                for (int i = 0; i < listaItems.size();i++){
                    for (int y = 0; y <evaList.size(); y++ ){
                        String evaluacion = evaList.get(y).getNombreEvaluacion();
                        if (listaItems.get(i).equals(evaluacion)){
                            IdsEva.add(evaList.get(y).getId_evaluacion());
                        }
                    }
                }
                break;
        }
    }

    private void llamarDialogoCrearNuevoDocente() {
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance("Nuevo Docente",null);
        crearDocenteActivity.setTargetFragment(crearParaleloActivity.this,22);
        crearDocenteActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            crearDocenteActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoAsignatura() {
        AsignaturaCrearActivity asignaturaCrearActivity = AsignaturaCrearActivity.newInstance("Nuevo Asignatura",null);
        asignaturaCrearActivity.setTargetFragment(crearParaleloActivity.this,22);
        asignaturaCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            asignaturaCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoEvaluacion() {
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nuevo Evaluación",null);
        evaluacionCrearActivity.setTargetFragment(crearParaleloActivity.this,22);
        evaluacionCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            evaluacionCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoHorario() {
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Nuevo Horario",null);
        horarioCrearActivity.setTargetFragment(crearParaleloActivity.this,22);
        horarioCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            horarioCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoPeriodo() {
        PeriodoCrearActivity periodoCrearActivity = PeriodoCrearActivity.newInstance("Nuevo Periodo",null);
        periodoCrearActivity.setTargetFragment(crearParaleloActivity.this,22);
        periodoCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            periodoCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogoCrearNuevoTarea() {
        TareaCrearActivity tareaCrearActivity = TareaCrearActivity.newInstance("Nuevo Tarea",null);
        tareaCrearActivity.setTargetFragment(crearParaleloActivity.this,22);
        tareaCrearActivity.setCancelable(false);
        if (getFragmentManager() != null) {
            tareaCrearActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    @Override
    public void onCrearAsignatura(Asignatura asignatura) {
        asignaturaList.add(asignatura);
    }

    @Override
    public void onCrearDocente(Docente docente) {
        docList.add(docente);
        obtenerDocentes();
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        tareaList.add(tarea);
        obtenerTarea();
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        evaList.add(evaluacion);
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
}
