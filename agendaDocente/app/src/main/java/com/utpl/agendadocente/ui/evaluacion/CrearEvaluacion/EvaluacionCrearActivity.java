package com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion;

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
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Cuestionario;
import com.utpl.agendadocente.Model.Evaluacion;
import com.utpl.agendadocente.Model.Paralelo;
import com.utpl.agendadocente.ui.cuestionario.CrearCuestionario.CuestionarioCrearActivity;
import com.utpl.agendadocente.ui.cuestionario.CrearCuestionario.CuestionarioCrearListener;
import com.utpl.agendadocente.ui.periodo.DialogDatePicker;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EvaluacionCrearActivity extends DialogFragment implements DialogDatePicker.DatePickerListener, CuestionarioCrearListener{

    private static EvaluacionCrearListener evaluacionCrearListener;
    private static Integer IdParalelo;
    private EvaluacionCrearListener listener;
    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());
    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
    private OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

    private TextInputEditText nomE, obsE;
    private Button btnFechaEva, btnParaleloA, btntipoEvaluacion;
    private Spinner cuest;
    private RadioButton rb1BimE, rb2BimE;
    private RecyclerView recyclerView;

    private String nombEva = "";
    private String tipoEva = "";
    private int idCuestEva = -1;
    private String bimEva = "";
    private String fechEva = "";
    private String obserEva = "";
    private List<Cuestionario> cuestList = operacionesCuestionario.ListarCuest();
    private List<Paralelo> ListaParalelo  = operacionesParalelo.ListarPar();
    private List<Asignatura> ListaAsignaturas = operacionesAsignatura.ListarAsig();


    private List<String> paralalosAsignados = new ArrayList<>();
    private ArrayList<String> listCuet = new ArrayList<>();
    private ArrayAdapter<String> adapter ;


    public EvaluacionCrearActivity (){}

    public static EvaluacionCrearActivity newInstance(String Title, EvaluacionCrearListener listener, Integer Id){
        IdParalelo = Id;
        evaluacionCrearListener = listener;
        EvaluacionCrearActivity evaluacionCrearActivity = new EvaluacionCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",Title);
        evaluacionCrearActivity.setArguments(bundle);

        evaluacionCrearActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return evaluacionCrearActivity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (evaluacionCrearListener != context){
                listener = (EvaluacionCrearListener) getTargetFragment();
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
            title = getArguments().getString(utilidades.TITULO);
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
                obtenerTipoEvaluacion(btntipoEvaluacion.getText().toString(), getContext());
            }
        });

        btnParaleloA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paralalosAsignados = obtenerParalelos(paralalosAsignados, ListaParalelo, ListaAsignaturas, getContext(), recyclerView);
            }
        });

        obtenerspinnercuestio();

        btnFechaEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDatePicker dialogDatePicker = DialogDatePicker.newInstance("");
                dialogDatePicker.setTargetFragment(EvaluacionCrearActivity.this,22);
                dialogDatePicker.setCancelable(false);
                dialogDatePicker.show(getParentFragmentManager(),utilidades.CREAR);
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
                nombEva = Objects.requireNonNull(nomE.getText()).toString();
                tipoEva = btntipoEvaluacion.getText().toString();
                String cuet = cuest.getSelectedItem().toString();
                for (int i = 0; i < cuestList.size(); i++){
                    if (cuestList.get(i).getNombreCuestionario().equals(cuet)){
                        idCuestEva = cuestList.get(i).getId_cuestionario();
                    }
                }

                if (rb1BimE.isChecked()){
                    bimEva = rb1BimE.getText().toString();
                }else if (rb2BimE.isChecked()){
                    bimEva = rb2BimE.getText().toString();
                }
                fechEva = btnFechaEva.getText().toString();
                obserEva = Objects.requireNonNull(obsE.getText()).toString();

                List<Integer> Ids = new ArrayList<>();
                if (btnParaleloA.getVisibility()==View.GONE && recyclerView.getVisibility()==View.GONE){
                    Ids.add(IdParalelo);
                }else {
                    obtenerIdsParalelos(paralalosAsignados, ListaParalelo, ListaAsignaturas);
                }

                if (!nombEva.isEmpty()){
                    for (int i = 0; i<Ids.size(); i++){
                        Evaluacion eva = new Evaluacion();
                        eva.setNombreEvaluacion(nombEva);
                        eva.setTipo(tipoEva);
                        eva.setBimestre(bimEva);
                        eva.setFechaEvaluacion(fechEva);
                        eva.setObservacion(obserEva);
                        eva.setCuestionarioID(idCuestEva);
                        eva.setParaleloID(Ids.get(i));

                        long insercion = operacionesEvaluacion.InsertarEva(eva);
                        if (insercion > 0 ){
                            int inser = (int)insercion;
                            eva.setId_evaluacion(inser);
                            if (evaluacionCrearListener != null){
                                evaluacionCrearListener.onCrearEvaluacion(eva);
                            }else  {
                                listener.onCrearEvaluacion(eva);
                            }

                            dismiss();
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Agregar un nombre",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        return view;
    }

    private void crearNuevoCuestionario() {
        CuestionarioCrearActivity crearCuestionario = CuestionarioCrearActivity.newInstance("Nuevo Cuestionario", null);
        crearCuestionario.setTargetFragment(EvaluacionCrearActivity.this,22);
        crearCuestionario.setCancelable(false);
        crearCuestionario.show(getChildFragmentManager(), utilidades.CREAR);
    }

    public void obtenerTipoEvaluacion(String TE, Context context){

        final String [] tipo = {"Presencial", "Online"};
        int posicion = -1;
        for (int i = 0; i<tipo.length; i++){
            if (tipo[i].equals(TE)){
                posicion = i;
            }
        }

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Tipo de Evaluación");
        dialog.setSingleChoiceItems(tipo, posicion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                btntipoEvaluacion.setText(tipo[i]);
                dialogInterface.dismiss();
            }
        });
        dialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog1 = dialog.create();
        dialog1.show();
    }

    public List<String> obtenerParalelos(List<String> lista, List<Paralelo> listaP, List<Asignatura> listaA, final Context context, final RecyclerView RV){

        final String [] paralelos = new String[listaP.size()];
        final boolean [] estados = new boolean[listaP.size()];
        final List <String> NuevaLista = new ArrayList<>();


        for (int i = 0; i < listaP.size(); i++){
            for (int j = 0; j < listaA.size(); j++){
                if (listaP.get(i).getAsignaturaID().equals(listaA.get(j).getId_asignatura())){
                    paralelos[i] = listaA.get(j).getNombreAsignatura() + " - " + listaP.get(i).getNombreParalelo();
                    if (lista.size() != 0 ){
                        for (int z = 0; z < lista.size(); z++){
                            if (lista.get(z).equals(paralelos[i])){
                                estados[i] = true;
                            }
                        }
                    }
                }
            }
        }

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
                        if (!NuevaLista.contains(paralelos[j])){
                            NuevaLista.add(paralelos[j]);
                        }
                    }else {
                        NuevaLista.remove(paralelos[j]);
                    }
                }

                llenarRecycleView(RV, context, NuevaLista);
            }
        });
        builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        return NuevaLista;
    }

    public void llenarRecycleView(RecyclerView rv, Context ctt, List<String> Lista){
        com.utpl.agendadocente.ui.evaluacion.CrearEvaluacion.paraleloAsigadoAdapter paraleloAsigadoAdapter = new paraleloAsigadoAdapter(ctt, Lista);
        rv.setLayoutManager(new LinearLayoutManager(ctt,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(paraleloAsigadoAdapter);
    }

    public List<Integer> obtenerIdsParalelos(List<String> Lista, List<Paralelo> ListaP, List<Asignatura> ListaA){
        List<Integer> Ids = new ArrayList<>();

        if (Lista.size() != 0){
            for (int i = 0; i <ListaP.size(); i++){
                for (int j = 0; j < ListaA.size(); j++){
                    for (int y = 0; y < Lista.size(); y++){
                        if (ListaP.get(i).getAsignaturaID().equals(ListaA.get(j).getId_asignatura())){
                            if (Lista.get(y).equals(ListaA.get(j).getNombreAsignatura()+" - "+ListaP.get(i).getNombreParalelo())){
                                Ids.add(ListaP.get(i).getId_paralelo());
                            }
                        }
                    }
                }
            }
        }else {
            Ids.add(null);
        }

        return Ids;
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
        if (IdParalelo != null) {
            btnParaleloA.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }else {
            btnParaleloA.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
