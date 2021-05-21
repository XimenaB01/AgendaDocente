package com.utpl.agendadocente.features.paralelo.actualizar_paralelo;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.features.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.features.paralelo.DialogAgregarMultiItems;
import com.utpl.agendadocente.features.paralelo.DialogAgregarSingleItem;

import com.utpl.agendadocente.R;
import com.utpl.agendadocente.features.paralelo.crear_paralelo.CrearParaleloActivity;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.paralelo.IParalelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParaleloActualizarActivity extends DialogFragment implements DialogAgregarMultiItems.AgregarItemsListener, DialogAgregarSingleItem.RecibirItemListener{

    private static long idParalelo;
    private static int paraleloItemPosition;
    private static IParalelo.ActualizarParaleloListener actualizarParaleloListener;

    private TextInputEditText nombre;
    private TextInputEditText alunmos;
    private TextView asignaturaAddAct;
    private TextView horarioAddAct;
    private TextView periodoAddAct;
    private RecyclerView recyclerDoc;
    private Button docenteAddAct;
    private Toolbar toolbar;

    private EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();

    private OperacionesDocente operacionesDocente = (OperacionesDocente) OperacionesFactory.getOperacionDocente(getContext());
    private OperacionesPeriodo operacionesPeriodo = (OperacionesPeriodo) OperacionesFactory.getOperacionPeriodo(getContext());
    private OperacionesParalelo operacionesParalelo = (OperacionesParalelo) OperacionesFactory.getOperacionParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = (OperacionesAsignatura) OperacionesFactory.getOperacionAsignatura(getContext());
    private OperacionesHorario operacionesHorario = (OperacionesHorario) OperacionesFactory.getOperacionHorario(getContext());

    private List<Docente> docenteList = operacionesDocente.listarDocente();
    private List<Asignatura> asigList = operacionesAsignatura.listarAsignatura();
    private List<Horario> horarioList = operacionesHorario.listarHorario();
    private List<PeriodoAcademico> periodoAcademicoList = operacionesPeriodo.listarPeriodo();

    private String [] itemsAgregados;
    private List<String> listItemMultiCkeck = new ArrayList<>();
    private String itemAgregado = "";
    private String tipoComponente;
    private String formatoDocente = "%s %s";
    private String formatoHorario = "%s Aula:%s De:%s A:%s";
    private String formatoPeriodo = "%s - %s";
    private int asigIdParAct = -1;
    private int perIdParAct = -1;
    private int hoIdParAct = -1;
    private List<Integer> idsDoc = new ArrayList<>();
    private List<String> listaDocentesAsignados = new ArrayList<>();

    public ParaleloActualizarActivity(){
        //required constructor
    }

    public static ParaleloActualizarActivity newInstance(Paralelo paralelo, int position, IParalelo.ActualizarParaleloListener listener){
        idParalelo = paralelo.getIdParalelo();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_actualizar_paralelo,container,false);

        toolbar = view.findViewById(R.id.toolbarParA);
        nombre = view.findViewById(R.id.nomParAct);
        alunmos = view.findViewById(R.id.numAluAct);
        docenteAddAct = view.findViewById(R.id.agregarDocenteAct);
        asignaturaAddAct = view.findViewById(R.id.agregarAsignaturaAct);
        horarioAddAct = view.findViewById(R.id.agregarHorarioAct);
        periodoAddAct = view.findViewById(R.id.agregarPeriodoAct);
        recyclerDoc = view.findViewById(R.id.recyclerDocente);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        llenarFormulario();

        return view;
    }

    private void llenarFormulario(){
        Paralelo paralelo = operacionesParalelo.obtenerParalelo(idParalelo);

        if (paralelo != null){
            nombre.setText(paralelo.getNombreParalelo());
            alunmos.setText(String.valueOf(paralelo.getNumEstudiantes()));

            obtenerDocentes();
            evaluacionCrearActivity.llenarRecycleView(recyclerDoc, getContext(), listaDocentesAsignados);

            docenteAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForDocente();
                }
            });

            obtenerAsignaturas(paralelo.getAsignaturaID());
            asignaturaAddAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llenarListaForAsignatura();
                }
            });

            if (paralelo.getHoraioID() != null){
                obtenerHorario(paralelo.getHoraioID());
                horarioAddAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llenarListaForHorario();
                    }
                });
            }

            if (paralelo.getPeriodoID() != null){
                obtenerPeriodo(paralelo.getPeriodoID());
                periodoAddAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llenarListaForPeriodo();
                    }
                });
            }

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    obtenerDatosFormulario();
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
        String periodoText = periodoAddAct.getText().toString();
        if (!periodoText.equals("Agregar Periodo")){
            itemAgregado = periodoText;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    private void llenarListaForHorario() {
        listItemMultiCkeck.clear();
        tipoComponente = "Horario";

        listItemMultiCkeck = getListaHorarios(formatoHorario, horarioList);

        String horarioTexto = horarioAddAct.getText().toString();
        if (!horarioTexto.equals("Agregar Horario")){
            itemAgregado = horarioTexto;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    public List<String> getListaHorarios(String formatoHora, List<Horario> listHorario){
        List<String> listItems = new ArrayList<>();
        for (int i = 0; i < listHorario.size();i++){
            String horario = String.format(formatoHora,listHorario.get(i).getDia(), listHorario.get(i).getAula(), listHorario.get(i).getHoraEntrada(),listHorario.get(i).getHoraSalida());
            if (!listItems.contains(horario)){
                listItems.add(horario);
            }
        }
        return listItems;
    }

    private void llenarListaForAsignatura() {
        listItemMultiCkeck.clear();
        tipoComponente = "Asignatura";
        for (int i = 0; i < asigList.size(); i++){
            if (!listItemMultiCkeck.contains(asigList.get(i).getNombreAsignatura())){
                listItemMultiCkeck.add(asigList.get(i).getNombreAsignatura());
            }
        }
        String asignaturaText = asignaturaAddAct.getText().toString();
        if (!asignaturaText.equals("Agregar Asignatura")){
            itemAgregado = asignaturaText;
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }else {
            llamarDialogAgregarSingleItem(tipoComponente, listItemMultiCkeck, itemAgregado);
        }
    }

    private void llenarListaForDocente() {
        listItemMultiCkeck.clear();
        tipoComponente = "Docente";

        listItemMultiCkeck = getListaNombresDocente(formatoDocente, docenteList);

        llamarDialogAgregarMultiItems(tipoComponente, listItemMultiCkeck, itemsAgregados);
    }

    public List<String> getListaNombresDocente(String formatoDoc, List<Docente> listDocente){
        List<String> listItems = new ArrayList<>();
        for (int i = 0; i < docenteList.size(); i++){
            String docente = (String.format(formatoDoc, listDocente.get(i).getNombreDocente(),listDocente.get(i).getApellidoDocente()));
            //verificar si no existe el docente, para agregarlo
            if (!listItems.contains(docente)){
                listItems.add(docente);
            }
        }
        return listItems;
    }

    private void obtenerDatosFormulario() {

        String nomParAct = Objects.requireNonNull(nombre.getText()).toString();
        int alumParAct = Integer.parseInt(Objects.requireNonNull(alunmos.getText()).toString());

        for (int i = 0; i < asigList.size(); i++){
            if (asignaturaAddAct.getText().toString().equals(asigList.get(i).getNombreAsignatura())){
                asigIdParAct = asigList.get(i).getIdAsignatura();
            }
        }

        for (int i = 0; i < horarioList.size(); i++){
            String horario = String.format(formatoHorario, horarioList.get(i).getDia(), horarioList.get(i).getAula(), horarioList.get(i).getHoraEntrada(),horarioList.get(i).getHoraSalida());
            if (horarioAddAct.getText().toString().equals(horario)){
                hoIdParAct = horarioList.get(i).getIdHorario();
            }
        }

        for (int i = 0; i < periodoAcademicoList.size(); i++){
            String periodo = String.format(formatoPeriodo,periodoAcademicoList.get(i).getFechaInicio(),periodoAcademicoList.get(i).getFechaFin());
            if (periodoAddAct.getText().toString().equals(periodo)){
                perIdParAct = periodoAcademicoList.get(i).getIdPeriodo();
            }
        }

        idsComponentesSeleccionados(listaDocentesAsignados);

        CrearParaleloActivity crearParaleloActivity = new CrearParaleloActivity();
        guardarParalelo(crearParaleloActivity.validarDatosParalelo(nomParAct, alumParAct, asigIdParAct, hoIdParAct, perIdParAct, getContext()));

    }


    private void guardarParalelo(Paralelo paralelo) {

        paralelo.setIdParalelo((int)idParalelo);
        long insertion = operacionesParalelo.modificarParalelo(paralelo, idsDoc);

        if (insertion > 0){
            actualizarParaleloListener.onActualizarParalelo(paralelo, paraleloItemPosition);
            Objects.requireNonNull(getDialog()).dismiss();
        }else {
            Toast.makeText(getContext(),"El paralelo ya existe!!",Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerAsignaturas(Integer idAsignatura){
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(idAsignatura);
        asignaturaAddAct.setText(asignatura.getNombreAsignatura());
    }

    private void obtenerHorario(long idHOrario){
        if (idHOrario != -1){
            Horario horario = operacionesHorario.obtenerHorario(idHOrario);
            horarioAddAct.setText(String.format(formatoHorario,horario.getDia(), horario.getAula(), horario.getHoraEntrada(), horario.getHoraSalida()));
        }else {
            String mensaje = "Agregar Horario";
            horarioAddAct.setText(mensaje);
        }

    }

    private void obtenerPeriodo(long idPeriodo) {
        if (idPeriodo != -1){
            PeriodoAcademico periodoAcademico = operacionesPeriodo.obtenerPeriodo(idPeriodo);
            periodoAddAct.setText(String.format(formatoPeriodo,periodoAcademico.getFechaInicio(),periodoAcademico.getFechaFin()));
        }else {
            String mensaje = "Agregar Periodo";
            periodoAddAct.setText(mensaje);
        }
    }

    private void obtenerDocentes(){
        List<Docente> docList = operacionesParalelo.obtenerDocentesAsignadosParalelo(idParalelo);
        itemsAgregados = new String[docList.size()];
        if (!docList.isEmpty()){
            for (int i = 0; i< docList.size(); i++){
                listaDocentesAsignados.add(String.format(formatoDocente, docList.get(i).getNombreDocente(), docList.get(i).getApellidoDocente()));
                itemsAgregados[i] = String.format(formatoDocente, docList.get(i).getNombreDocente(), docList.get(i).getApellidoDocente());
                idsDoc.add(docList.get(i).getIdDocente());
            }
        }
    }

    private void llamarDialogAgregarMultiItems(String componente, List<String> listItemsMultiChecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems agregarMultiItems = DialogAgregarMultiItems.newInstance(componente, listItemsMultiChecks, listaItemsAsignados);
        agregarMultiItems.setTargetFragment(ParaleloActualizarActivity.this,22);
        agregarMultiItems.setCancelable(false);
        agregarMultiItems.show(getParentFragmentManager(), Utilidades.ACTUALIZAR);
    }

    private void llamarDialogAgregarSingleItem(String componente, List<String> listaItems, String itemAsignado){
        DialogAgregarSingleItem agregarSingleItem = DialogAgregarSingleItem.newInstance(componente, listaItems, itemAsignado);
        agregarSingleItem.setTargetFragment(ParaleloActualizarActivity.this,22);
        agregarSingleItem.setCancelable(false);
        agregarSingleItem.show(getParentFragmentManager(), Utilidades.ACTUALIZAR);
    }

    private void idsComponentesSeleccionados(List<String> itemsSeleccionados){
        idsDoc.clear();
        docenteList.clear();
        docenteList = operacionesDocente.listarDocente();

        for (int i = 0; i < itemsSeleccionados.size(); i++){
            for (int j = 0; j < docenteList.size(); j++){
                if (itemsSeleccionados.get(i).equals(docenteList.get(j).getNombreDocente() + " " + docenteList.get(j).getApellidoDocente())){
                    idsDoc.add(docenteList.get(j).getIdDocente());
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
            dialog.setCancelable(false);
        }
    }

    @Override
    public void onAgregarItems(List<String> itemsSeleccionados, String componente) {
        if(!itemsSeleccionados.isEmpty()){
            evaluacionCrearActivity.llenarRecycleView(recyclerDoc, getContext(), itemsSeleccionados);
            itemsAgregados = new String[itemsSeleccionados.size()];
            for (int i = 0; i < itemsSeleccionados.size(); i++){
                itemsAgregados[i] = itemsSeleccionados.get(i);
            }
            listaDocentesAsignados = itemsSeleccionados;
        }else {
            idsDoc.clear();//si no hay Item selecionados se borra todos los Ids de la Lista
        }
    }

    @Override
    public void onRecibirItemAsignado(String componente, String itemAsignado) {
        switch (componente) {
            case "Asignatura":
                asignaturaAddAct.setText(itemAsignado);
                break;
            case "Horario":
                horarioAddAct.setText(itemAsignado);
                break;
            case "Periodo":
                periodoAddAct.setText(itemAsignado);
                break;
            default:
                //ingnore this part
                break;
        }
    }
}
