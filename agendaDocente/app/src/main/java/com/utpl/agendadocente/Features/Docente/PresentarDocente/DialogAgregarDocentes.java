package com.utpl.agendadocente.Features.Docente.PresentarDocente;

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
import com.utpl.agendadocente.Features.Docente.CrearDocente.CrearDocenteActivity;
import com.utpl.agendadocente.Features.Docente.CrearDocente.DocenteCreateListener;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogAgregarDocentes extends DialogFragment implements DocenteCreateListener {

    private ItemDocenteCheckAdapter itemDocenteCheckAdapter;
    private static String [] listaItems;
    private static List<String> ListaItemMultiCkecks;
    private List<Boolean> estado = new ArrayList<>();
    private String Componente;

    public DialogAgregarDocentes(){}

    public static DialogAgregarDocentes newInstance(String title, List<String> listaItemMultiCkecks, String [] listaItemsAsignados){
        DialogAgregarDocentes dialogAgregarDocentes = new DialogAgregarDocentes();
        listaItems = listaItemsAsignados;
        ListaItemMultiCkecks = listaItemMultiCkecks;
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        dialogAgregarDocentes.setArguments(bundle);
        return dialogAgregarDocentes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_docente,container,false);

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
                    case "Asignatura":
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

        itemDocenteCheckAdapter = new ItemDocenteCheckAdapter(getContext(), ListaItemMultiCkecks, estado);
        listaDocentes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listaDocentes.setAdapter(itemDocenteCheckAdapter);

        toolbar.inflateMenu(R.menu.agregar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                List<String> list = itemDocenteCheckAdapter.getSelectedItems();
                listener.onAgregarItems(list);
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
        CrearDocenteActivity crearDocenteActivity = CrearDocenteActivity.newInstance("Crear Docente", this);
        if (getFragmentManager() != null) {
            crearDocenteActivity.show(getFragmentManager(), utilidades.CREAR);
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
        itemDocenteCheckAdapter.notifyDataSetChanged();
    }

    public interface AgregarItemsListener {
        void onAgregarItems(List<String> items);
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
