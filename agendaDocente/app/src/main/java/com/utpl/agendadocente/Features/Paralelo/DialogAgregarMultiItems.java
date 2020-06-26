package com.utpl.agendadocente.Features.Paralelo;

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

import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Docente.CrearDocente.CrearDocenteActivity;
import com.utpl.agendadocente.Features.Docente.CrearDocente.DocenteCreateListener;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearListener;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearActivity;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearListener;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogAgregarMultiItems extends DialogFragment implements DocenteCreateListener, TareaCrearListener, EvaluacionCrearListener {

    private ItemMultiCheckAdapter itemMultiCheckAdapter;
    private static String [] listaItems;
    private static List<String> ListaItemMultiCkecks;
    private List<Boolean> estado = new ArrayList<>();
    private String Componente;

    public DialogAgregarMultiItems(){}

    public static DialogAgregarMultiItems newInstance(String title, List<String> listaItemMultiCkecks, String [] listaItemsAsignados){
        DialogAgregarMultiItems dialogAgregarMultiItems = new DialogAgregarMultiItems();
        listaItems = listaItemsAsignados;
        ListaItemMultiCkecks = listaItemMultiCkecks;
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
        TextView nuevoDocente = view.findViewById(R.id.btnNDocente);
        RecyclerView listaDocentes = view.findViewById(R.id.checkListDocente);

        String title = null;
        if (getArguments() != null) {
            Componente = getArguments().getString(utilidades.TITULO);
            title = "Agregar "+ Componente;
        }
        toolbar.setTitle(title);

        nuevoDocente.setText(String.format("Añadir Nuevo %s",getArguments().getString(utilidades.TITULO)));
        nuevoDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Componente){
                    case "Docente":
                        llamarDialogoCrearDocente();
                        break;
                    case "Tarea":
                        llamarDialogCrearTarea();
                        break;
                    case "Evaluación":
                        llamarDialogCrearEvaluacion();
                        break;
                }
            }
        });

        estado.addAll(Collections.nCopies(ListaItemMultiCkecks.size(),Boolean.FALSE));
        if (listaItems != null) {
            if (listaItems.length != 0){
                for (int j = 0; j < ListaItemMultiCkecks.size();j++){
                    for (int i= 0; i<listaItems.length;i++){
                        if (ListaItemMultiCkecks.get(j).equals(listaItems[i])){
                            estado.set(j,true);
                        }
                    }
                }
            }
        }

        itemMultiCheckAdapter = new ItemMultiCheckAdapter(getContext(), ListaItemMultiCkecks, estado);
        listaDocentes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDocentes.setAdapter(itemMultiCheckAdapter);

        toolbar.inflateMenu(R.menu.agregar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<String> list = itemMultiCheckAdapter.getSelectedItems();
                listener.onAgregarItems(list, Componente);
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
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance("Crear "+Componente, this);
        if (getFragmentManager() != null) {
            crearDocenteActivity.show(getFragmentManager(), utilidades.CREAR);
        }
    }

    private void llamarDialogCrearTarea(){
        TareaCrearActivity tareaCrearActivity = TareaCrearActivity.newInstance("Crear "+Componente,this);
        if (getFragmentManager() != null){
            tareaCrearActivity.show(getFragmentManager(),utilidades.CREAR);
        }
    }

    private void llamarDialogCrearEvaluacion(){
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Crear "+Componente,this);
        if (getFragmentManager() != null){
            evaluacionCrearActivity.show(getFragmentManager(), utilidades.CREAR);
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
    public void onCrearDocente(Docente docente) {
        String newDocente = String.format("%s %s",docente.getNombreDocente(),docente.getApellidoDocente());
        ListaItemMultiCkecks.add(newDocente);
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        ListaItemMultiCkecks.add(tarea.getNombreTarea());
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        ListaItemMultiCkecks.add(evaluacion.getNombreEvaluacion());
        estado.add(false);
        itemMultiCheckAdapter.notifyDataSetChanged();
    }

    public interface AgregarItemsListener {
        void onAgregarItems(List<String> items, String Componente);
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
