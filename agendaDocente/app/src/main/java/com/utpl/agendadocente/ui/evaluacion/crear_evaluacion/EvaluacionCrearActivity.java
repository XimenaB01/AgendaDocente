package com.utpl.agendadocente.ui.evaluacion.crear_evaluacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.flyweight.PruebasFactory;
import com.utpl.agendadocente.ui.cuestionario.crear_cuestionario.CuestionarioCrearActivity;
import com.utpl.agendadocente.ui.cuestionario.ICuestionario;
import com.utpl.agendadocente.ui.evaluacion.IEvaluacion;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener, ICuestionario.CuestionarioCrearListener{

    private static IEvaluacion.EvaluacionCrearListener evaluacionCrearListener;
    private static Integer idParalelo;
    private IEvaluacion.EvaluacionCrearListener listener;
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

    private TextInputEditText nomE;
    private TextInputEditText obsE;
    private Button btnFechaEva;
    private Button btnParaleloA;
    private Button btntipoEvaluacion;
    private Spinner cuest;
    private RadioButton rb1BimE;
    private RadioButton rb2BimE;
    private RecyclerView recyclerView;

    private String nombEva = "";
    private String tipoEva = "";
    private int idCuestEva = -1;
    private String bimEva = "";
    private String fechEva = "";
    private String obserEva = "";
    private String texto = "Sin Asignar";
    private List<Cuestionario> cuestList = operacionesCuestionario.listarCuestionario();
    private List<Paralelo> listaParalelo = operacionesParalelo.listarParalelo();
    private List<Asignatura> listaAsignaturas = operacionesAsignatura.listarAsignatura();


    private List<String> paralalosAsignados = new ArrayList<>();
    private ArrayList<String> listCuet = new ArrayList<>();
    private ArrayAdapter<String> adapter ;


    public EvaluacionCrearActivity (){
        //Required constructor
    }

    public static EvaluacionCrearActivity newInstance(String title, IEvaluacion.EvaluacionCrearListener listener, Integer id){
        idParalelo = id;
        evaluacionCrearListener = listener;
        EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        evaluacionCrearActivity.setArguments(bundle);

        evaluacionCrearActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return evaluacionCrearActivity;
    }

    public interface RetornoDeValor{
        void retornarvalor(String valor);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (evaluacionCrearListener != context){
                listener = (IEvaluacion.EvaluacionCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_crear_evaluacion,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarE);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        nomE = view.findViewById(R.id.textNomE);
        btntipoEvaluacion = view.findViewById(R.id.tipoE);
        cuest = view.findViewById(R.id.spinnerEva);
        rb1BimE = view.findViewById(R.id.rb1B);
        rb2BimE = view.findViewById(R.id.rb2B);
        obsE = view.findViewById(R.id.textObsEva);
        btnFechaEva = view.findViewById(R.id.btnfecE);
        btnParaleloA = view.findViewById(R.id.paraleloAsigEva);
        recyclerView = view.findViewById(R.id.paralelosAsignados);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.FABQNew);

        llenarRecycleView(recyclerView, getContext(), paralalosAsignados);

        btntipoEvaluacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerTipoEvaluacion(btntipoEvaluacion.getText().toString(), getContext(), null);
            }
        });

        btnParaleloA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paralalosAsignados = obtenerParalelos(paralalosAsignados, listaParalelo, listaAsignaturas, getContext(), recyclerView);
            }
        });

        obtenerspinnercuestio();

        btnFechaEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(EvaluacionCrearActivity.this,22);
                dialogDatePicker.setCancelable(false);
                dialogDatePicker.show(getParentFragmentManager(), Utilidades.CREAR);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearNuevoCuestionario();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        visible();

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                validarDatos();
                return true;
            }
        });

        return view;
    }

    private void validarDatos() {

        nombEva = Objects.requireNonNull(nomE.getText()).toString();

        obtenerTipoEvaluacion();
        obtenerCuestionario();
        obtenerBimestre();
        obtenerFecha();
        obtenerObservacion();


        List<Integer> ids = new ArrayList<>();
        if (btnParaleloA.getVisibility()==View.GONE && recyclerView.getVisibility()==View.GONE){
            ids.add(idParalelo);
        }else {
            ids = (obtenerIdsParalelos(paralalosAsignados, listaParalelo, listaAsignaturas));
        }

        if (!nombEva.isEmpty()){
            if (!ids.isEmpty()){
                for (int i = 0; i<ids.size(); i++){
                    enviarEvaluacion(ids.get(i));
                }
            }else {
                enviarEvaluacion(null);
            }

        }else{
            Toast.makeText(getContext(),"Agregar un nombre",Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerObservacion() {
        if (!Objects.requireNonNull(obsE.getText()).toString().isEmpty()){
            obserEva = Objects.requireNonNull(obsE.getText()).toString();
        }else {
            obserEva = texto;
        }
    }

    private void obtenerFecha() {
        if (!btnFechaEva.getText().toString().equals("Fecha de Evaluación")){
            fechEva = btnFechaEva.getText().toString();
        }else {
            fechEva = texto;
        }
    }

    private void obtenerBimestre() {
        if (rb1BimE.isChecked()){
            bimEva = rb1BimE.getText().toString();
        }else if (rb2BimE.isChecked()){
            bimEva = rb2BimE.getText().toString();
        }
    }

    private void obtenerCuestionario() {
        String cuet = cuest.getSelectedItem().toString();
        for (int i = 0; i < cuestList.size(); i++){
            if (cuestList.get(i).getNombreCuestionario().equals(cuet)){
                idCuestEva = cuestList.get(i).getIdCuestionario();
            }
        }
    }

    private void obtenerTipoEvaluacion() {
        if (!btntipoEvaluacion.getText().toString().equals("Tipo de Evaluación")){
            tipoEva = btntipoEvaluacion.getText().toString();
        }else {
            tipoEva = texto;
        }
    }

    private void crearNuevoCuestionario() {
        CuestionarioCrearActivity crearCuestionario = CuestionarioCrearActivity.newInstance("Nuevo Cuestionario", null);
        crearCuestionario.setTargetFragment(EvaluacionCrearActivity.this,22);
        crearCuestionario.setCancelable(false);
        crearCuestionario.show(getParentFragmentManager(), Utilidades.CREAR);
    }

    public void obtenerTipoEvaluacion(String tipoEvaluacion, Context context, final RetornoDeValor retonoDeValor){

        final String [] tipo = {"Presencial", "Online"};
        int posicion = -1;
        for (int i = 0; i<tipo.length; i++){
            if (tipo[i].equals(tipoEvaluacion)){
                posicion = i;
            }
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Tipo de Evaluación");
        dialog.setSingleChoiceItems(tipo, posicion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (retonoDeValor != null){
                    retonoDeValor.retornarvalor(tipo[i]);
                }else {
                    btntipoEvaluacion.setText(tipo[i]);
                }
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    private void enviarEvaluacion(Integer id){
        Evaluacion eva = (Evaluacion) PruebasFactory.getPrueba(bimEva);
        eva.setNombreEvaluacion(nombEva);
        eva.setTipo(tipoEva);
        eva.setBimestre(bimEva);
        eva.setFechaEvaluacion(fechEva);
        eva.setObservacion(obserEva);
        eva.setCuestionarioID(idCuestEva);
        eva.setParaleloID(id);

        long insercion = operacionesEvaluacion.insertarEvaluacion(eva.write());
        if (insercion > 0 ){
            int inser = (int)insercion;
            eva.setIdEvaluacion(inser);
            if (evaluacionCrearListener != null){
                evaluacionCrearListener.onCrearEvaluacion(eva);
            }else  {
                listener.onCrearEvaluacion(eva);
            }

            dismiss();
        }
    }

    public List<String> obtenerParalelos(List<String> lista, List<Paralelo> listaP, List<Asignatura> listaA, Context context, RecyclerView recyclerView){

        final String [] paralelos = new String[listaP.size()];
        final boolean [] estados = new boolean[listaP.size()];

        for (int i = 0; i < listaP.size(); i++){
            for (int j = 0; j < listaA.size(); j++){
                if (listaP.get(i).getAsignaturaID().equals(listaA.get(j).getIdAsignatura())){
                    paralelos[i] = listaA.get(j).getNombreAsignatura() + " - " + listaP.get(i).getNombreParalelo();
                    estados[i] = asignarEstado(lista, paralelos[i]);
                }
            }
        }

        return llamarDialogForParalelos(paralelos, estados , context , recyclerView);
    }

    private boolean asignarEstado(List<String> lista, String paralelos){
        boolean estados = false;
        if (!lista.isEmpty()){
            for (int z = 0; z < lista.size(); z++){
                if (lista.get(z).equals(paralelos)){
                    estados = true;
                }
            }
        }
        return estados;
    }

    private List<String> llamarDialogForParalelos(final String[] paralelos, final boolean[] estados, final Context context, final RecyclerView recyclerViews) {

        final List <String> nuevaLista = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Paralelos");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(paralelos, estados, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                estados[i] = b;
            }
        });
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < estados.length; j++){
                    boolean checked = estados[j];
                    if (checked){
                        if (!nuevaLista.contains(paralelos[j])){
                            nuevaLista.add(paralelos[j]);
                        }
                    }else {
                        nuevaLista.remove(paralelos[j]);
                    }
                }

                llenarRecycleView(recyclerViews, context, nuevaLista);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return nuevaLista;
  }

    public void llenarRecycleView(RecyclerView rv, Context ctt, List<String> lista){
        ParaleloAsigadoAdapter paraleloAsigadoAdapter = new ParaleloAsigadoAdapter(ctt, lista);
        rv.setLayoutManager(new LinearLayoutManager(ctt,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(paraleloAsigadoAdapter);
    }

    public List<Integer> obtenerIdsParalelos(List<String> lista, List<Paralelo> paraleloList, List<Asignatura> asignaturaList){
        List<Integer> ids = new ArrayList<>();

        if (!lista.isEmpty()){
            for (int i = 0; i <paraleloList.size(); i++){
                for (int j = 0; j < asignaturaList.size(); j++){
                    String nombreParalelo = asignaturaList.get(j).getNombreAsignatura()+" - "+paraleloList.get(i).getNombreParalelo();
                    ids.add(obtenerIdParalelo(lista, nombreParalelo, paraleloList.get(i).getAsignaturaID(), asignaturaList.get(j).getIdAsignatura(), paraleloList.get(i).getIdParalelo()));
                }
            }
        }else {
            ids.add(null);
        }
        return ids;
    }

    private Integer obtenerIdParalelo(List<String> lista, String nombrePalelo, Integer idAsignaturaForParalelo, Integer idAsignatura, Integer idParalelo){
        Integer ids = null;
        for (int y = 0; y < lista.size(); y++){
            if (idAsignaturaForParalelo.equals(idAsignatura) && lista.get(y).equals(nombrePalelo)){
                ids = idParalelo;
            }
        }
        return ids;
    }

    private void obtenerspinnercuestio(){
        llenarListaAdapter();
        adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal, listCuet);
        cuest.setAdapter(adapter);
    }

    private void llenarListaAdapter(){
        listCuet.add("Seleccione Cuestionario");
        for (int i = 0; i< cuestList.size(); i++){
            listCuet.add(cuestList.get(i).getNombreCuestionario());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, String fech, String tipo) {
        btnFechaEva.setText(fech);
    }

    @Override
    public void onCrearCuestionario(Cuestionario cuestionario) {
        adapter.clear();
        cuestList.add(cuestionario);
        llenarListaAdapter();
        adapter = new ArrayAdapter<>(requireContext(),R.layout.spinner_item_style_pesonal, listCuet);
        cuest.setAdapter(adapter);
    }

    private void visible(){
        if (idParalelo != null) {
            btnParaleloA.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }else {
            btnParaleloA.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
