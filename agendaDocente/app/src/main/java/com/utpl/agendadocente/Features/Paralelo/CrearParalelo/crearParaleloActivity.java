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
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarAsignatura;
import com.utpl.agendadocente.Features.Docente.PresentarDocente.DialogAgregarDocentes;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarEvaluacion;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarHorario;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarPeriodo;
import com.utpl.agendadocente.Features.Paralelo.DialogAgregarTarea;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class crearParaleloActivity extends DialogFragment implements DialogAgregarDocentes.AgregarItemsListener {

    private static ParaleloCrearListener paraleloCrearListener;

    private TextInputEditText nombre, alunmos;
    private TextView docenteAdd, tareaAdd, evaluacionAdd;
    private TextView asignaturaAdd, periodoAdd, horarioAdd;

    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());

    private String tipoComponente;
    private String [] itemAgregados;
    private List<Integer>IdsDoc = new ArrayList<>();
    private List<Integer>IdsTar = new ArrayList<>();
    private List<Integer>IdsEva = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private List<Docente> docenteList = operacionesDocente.listarDoc();
    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();

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
                String docentes = docenteAdd.getText().toString();
                tipoComponente = "Docente";
                String docente;
                //llena la lista con los nombres y apellidos de cada docente
                for (int i = 0; i < docenteList.size(); i++){
                    docente = (String.format("%s %s",docenteList.get(i).getNombreDocente(),docenteList.get(i).getApellidoDocente()));
                    //verificar si no existe el docente, para agregarlo
                    if (!listItemMultiCkeck.contains(docente)){
                        listItemMultiCkeck.add(docente);
                    }
                }
                //verifica si en la variable docentes no existe las siguiente cadena "Agregar Docente"
                if (!docentes.contains("Agregar Docente")){
                    //divide o separa la cadena cada que encuentra ", " y guarda cada separacion en un array
                    String [] parts = docentes.split(", ");
                    itemAgregados = new String[parts.length];
                    System.arraycopy(parts, 0, itemAgregados, 0, parts.length);
                    llamarDialogAgregarDocentes(tipoComponente, listItemMultiCkeck, itemAgregados);
                }else {
                    llamarDialogAgregarDocentes(tipoComponente, listItemMultiCkeck,itemAgregados);
                }
            }
        });


        asignaturaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogAgregarAsignatura();
            }
        });

        horarioAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogAgregarHorario();
            }
        });

        periodoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogAgregarPeriodo();
            }
        });

        tareaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipoComponente = "Tarea";

                llamarDialogAgregarTareas();
            }
        });

        evaluacionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarDialogAgregarEvaluaciones();
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

    private void llamarDialogAgregarDocentes(String Componente, List<String> ListItemsMultiChecks,String [] listaItemsAsignados){
        DialogAgregarDocentes agregarDocentes = DialogAgregarDocentes.newInstance(Componente, ListItemsMultiChecks, listaItemsAsignados);
        agregarDocentes.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarDocentes.show(getFragmentManager(),utilidades.CREAR);
        }
    }

    private void llamarDialogAgregarAsignatura(){
        DialogAgregarAsignatura agregarAsignatura = new DialogAgregarAsignatura();
        agregarAsignatura.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarAsignatura.show(getFragmentManager(),utilidades.CREAR);
        }
    }
    private void llamarDialogAgregarHorario(){
        DialogAgregarHorario agregarHorario = new DialogAgregarHorario();
        agregarHorario.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarHorario.show(getFragmentManager(),utilidades.CREAR);
        }
    }
    private void llamarDialogAgregarPeriodo(){
        DialogAgregarPeriodo agregarPeriodo = new DialogAgregarPeriodo();
        agregarPeriodo.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarPeriodo.show(getFragmentManager(),utilidades.CREAR);
        }
    }
    private void llamarDialogAgregarTareas(){
        DialogAgregarTarea agregarTarea = new DialogAgregarTarea();
        agregarTarea.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarTarea.show(getFragmentManager(),utilidades.CREAR);
        }
    }
    private void llamarDialogAgregarEvaluaciones(){
        DialogAgregarEvaluacion agregarEvaluacion = new DialogAgregarEvaluacion();
        agregarEvaluacion.setTargetFragment(crearParaleloActivity.this,22);
        if (getFragmentManager() != null) {
            agregarEvaluacion.show(getFragmentManager(),utilidades.CREAR);
        }
    }

    @Override
    //public void onAgregarDocente(List<Docente> docentes) {
    public void onAgregarItems(List<String> docentes) {
        StringBuilder docentesAsignados = new StringBuilder();
        if(docentes.size() != 0){
            for (int i = 0; i<docentes.size(); i++){
                //docentesAsignados.append(String.format("%s %s",docentes.get(i).getNombreDocente(),docentes.get(i).getApellidoDocente()));
                docentesAsignados.append(String.format("%s",docentes.get(i)));
                if (i < docentes.size()-1){
                    docentesAsignados.append(", ");
                }
            }
            docenteAdd.setText(docentesAsignados);
        }else {
            docentesAsignados.append("Agregar Docente");
            docenteAdd.setText(docentesAsignados);
        }

    }
}
