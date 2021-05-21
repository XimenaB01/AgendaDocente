package com.utpl.agendadocente.features.paralelo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.features.docente.crear_docente.CrearDocenteActivity;
import com.utpl.agendadocente.features.docente.IDocente;
import com.utpl.agendadocente.features.evaluacion.crear_evaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.features.evaluacion.IEvaluacion;
import com.utpl.agendadocente.features.tarea.crear_tarea.TareaCrearActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.features.tarea.ITarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogAgregarMultiItems extends DialogFragment implements IDocente.DocenteCreateListener, ITarea.TareaCrearListener, IEvaluacion.EvaluacionCrearListener {

    private TextView nuevoItem;
    private ItemMultiCheckAdapter itemMultiCheckAdapter;
    private static String [] listaItems;
    private static List<String> listaItemMultiCkecks;
    private List<Boolean> estado = new ArrayList<>();
    private String componente;
    private String texto = "Crear ";

    public DialogAgregarMultiItems(){
        //required constructor
    }

    public static DialogAgregarMultiItems newInstance(String title, List<String> listaItemMultiCkecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems dialogAgregarMultiItems = new DialogAgregarMultiItems();
        listaItems = listaItemsAsignados;
        DialogAgregarMultiItems.listaItemMultiCkecks = listaItemMultiCkecks;
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        dialogAgregarMultiItems.setArguments(bundle);
        return dialogAgregarMultiItems;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_multi_items,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarAgreDoc);
        nuevoItem = view.findViewById(R.id.btnNDocente);
        RecyclerView listaDocentes = view.findViewById(R.id.checkListDocente);

        String title = null;
        if (getArguments() != null) {
            componente = getArguments().getString(Utilidades.TITULO);
            title = "Agregar "+ componente;
        }
        toolbar.setTitle(title);

        nuevoItem.setText(String.format("Añadir Nuevo %s",getArguments().getString(Utilidades.TITULO)));
        llamarTipoItem();

        estado.addAll(Collections.nCopies(listaItemMultiCkecks.size(),Boolean.FALSE));
        if (listaItems != null && listaItems.length != 0) {
            for (int j = 0; j < listaItemMultiCkecks.size(); j++){
                for (int i= 0; i<listaItems.length;i++){
                    if (listaItemMultiCkecks.get(j).equals(listaItems[i])){
                        estado.set(j,true);
                    }
                }
            }
        }

        itemMultiCheckAdapter = new ItemMultiCheckAdapter(getContext(), listaItemMultiCkecks, estado);
        listaDocentes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDocentes.setAdapter(itemMultiCheckAdapter);

        toolbar.inflateMenu(R.menu.agregar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < itemMultiCheckAdapter.getSelectedItems().size(); i++){
                    if (!list.contains(itemMultiCheckAdapter.getSelectedItems().get(i))){
                        list.add(itemMultiCheckAdapter.getSelectedItems().get(i));
                    }
                }
                listener.onAgregarItems(list, componente);
                dismiss();
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

    private void llamarDialogoCrearDocente() {
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance(texto + componente, this);
        crearDocenteActivity.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    private void llamarDialogCrearTarea(){
        TareaCrearActivity tareaCrearActivity = TareaCrearActivity.newInstance(texto + componente,this, null);
        tareaCrearActivity.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    private void llamarDialogCrearEvaluacion(){
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance(texto + componente,this, null);
        evaluacionCrearActivity.show(getChildFragmentManager(), Utilidades.CREAR);
    }

    private void llamarTipoItem(){
        nuevoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (componente){
                    case "Docente":
                        llamarDialogoCrearDocente();
                        break;
                    case "Tarea":
                        llamarDialogCrearTarea();
                        break;
                    case "Evaluación":
                        llamarDialogCrearEvaluacion();
                        break;
                    default:
                        //Ignore this part
                        break;
                }
            }
        });
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
    public void onCrearDocente(Docente docente) {
        String newDocente = String.format("%s %s",docente.getNombreDocente(),docente.getApellidoDocente());
        listaItemMultiCkecks.add(newDocente);
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        listaItemMultiCkecks.add(tarea.getNombreTarea());
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        listaItemMultiCkecks.add(evaluacion.getNombreEvaluacion());
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    public interface AgregarItemsListener {
        void onAgregarItems(List<String> items, String componente);
    }

    private AgregarItemsListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AgregarItemsListener)getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException("onAgregarDocente no se está implementando");
        }
    }
}
