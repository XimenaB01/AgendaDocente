package com.utpl.agendadocente.ui.paralelo.Replicar;

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
    private TextView docenteRep, asignaturaRep, periodoRep, horarioRep;

    private OperacionesDocente operacionesDocente = new OperacionesDocente(getContext());
    private OperacionesTarea operacionesTarea = new OperacionesTarea(getContext());
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesHorario operacionesHorario = new OperacionesHorario(getContext());
    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(getContext());
    private OperacionesAsignatura  operacionesAsignatura = new OperacionesAsignatura(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());

    private Paralelo paralelo;

    private List<Integer> IdsDoc = new ArrayList<>();
    private List<Integer> IdsTar = new ArrayList<>();
    private List<Integer> IdsEva = new ArrayList<>();
    private List<String> listItemMultiCkeck = new ArrayList<>();

    private List<Asignatura> asignaturaList = operacionesAsignatura.ListarAsig();
    private List<Horario> horarioList = operacionesHorario.ListarHor();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.ListarPer();
    private List<Docente> docenteList = operacionesDocente.listarDoc();
    private List<Tarea> tareaList = operacionesTarea.ListarTar();
    private List<Evaluacion> evaluacionList = operacionesEvaluacion.ListarEva();
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

        if (paralelo!=null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNum_estudiantes()));

            obtenerDocentes();
            docenteRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemMultiCkeck.clear();
                    String docentes = docenteRep.getText().toString();
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
                                    long insercion = operacionesParalelo.InsertarPar(paralelo,IdsDoc);
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
        List<Docente> docListRepli = operacionesParalelo.obtenerDocentesAsignadosParalelo(IdParalelo);
        StringBuilder Docentes = new StringBuilder();
        if (docListRepli.size() != 0){
            for (int i = 0; i< docListRepli.size(); i++){
                Docentes.append(String.format("%s %s", docListRepli.get(i).getNombreDocente(), docListRepli.get(i).getApellidoDocente()));
                IdsDoc.add(docListRepli.get(i).getId_docente());
                if (i != docListRepli.size()-1){
                    Docentes.append(", ");
                }
            }
        }else {
            Docentes.append("Agregar Docente");
        }
        docenteRep.setText(Docentes);
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

    private void llamarDialogAgregarMultiItems(String Componente, List<String> ListItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(Componente, ListItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(ReplicarActivity.this,22);
        agregarMultiItems.setCancelable(false);
        agregarMultiItems.show(getChildFragmentManager(),utilidades.ACTUALIZAR);
    }

    private void llamarDialogAgregarSingleItem(String Componente, List<String> ListaItems, String ItemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(Componente, ListaItems, ItemAsignado);
        agregarSingleItem.setTargetFragment(ReplicarActivity.this,22);
        agregarSingleItem.setCancelable(false);
        agregarSingleItem.show(getChildFragmentManager(),utilidades.ACTUALIZAR);
    }

    private void IdsComponentesSeleccionados(String Componente, List<String> ItemsSeleccionados){
        for (int i = 0; i < listItemMultiCkeck.size(); i++){
            for (int j = 0; j < ItemsSeleccionados.size();j++){
                if (listItemMultiCkeck.get(i).equals(ItemsSeleccionados.get(j))){
                    if (Componente.equals("Docente")){
                        if (!IdsDoc.contains(docenteList.get(i).getId_docente())){
                            IdsDoc.add(docenteList.get(i).getId_docente());
                        }
                    }else if (Componente.equals("Tarea")){
                        if (!IdsTar.contains(tareaList.get(i).getId_tarea())){
                            IdsTar.add(tareaList.get(i).getId_tarea());
                        }
                    }else {
                        if (!IdsEva.contains(evaluacionList.get(i).getId_evaluacion())){
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
        }
    }

    @Override
    public void onAgregarItems(List<String> ItemsSeleccionados, String Componente) {
        StringBuilder ItemsAsignados = new StringBuilder();
        if(ItemsSeleccionados.size() != 0){
            ItemsAsignados.append(obtnerItems(ItemsSeleccionados));
            docenteRep.setText(ItemsAsignados);
            IdsComponentesSeleccionados(Componente,ItemsSeleccionados);
        }else {
            ItemsAsignados.append(String.format("Agregar %s",Componente));
            docenteRep.setText(ItemsAsignados);
        }
    }

    @Override
    public void onRecibirItemAsignado(String Componente, String ItemAsignado) {
        switch (Componente) {
            case "Asignatura":
                asignaturaRep.setText(ItemAsignado);
                break;
            case "Horario":
                horarioRep.setText(ItemAsignado);
                break;
            case "Periodo":
                periodoRep.setText(ItemAsignado);
                break;
        }
    }
}
