package com.utpl.agendadocente.ui.paralelo;

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

import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Model.Horario;
import com.utpl.agendadocente.Model.PeriodoAcademico;
import com.utpl.agendadocente.ui.asignatura.CrearAsignatura.AsignaturaCrearActivity;
import com.utpl.agendadocente.decorador.intef.IAsignatura;
import com.utpl.agendadocente.ui.horario.CrearHorario.HorarioCrearActivity;
import com.utpl.agendadocente.ui.horario.IHorario;
import com.utpl.agendadocente.ui.periodo.CrearPeriodo.PeriodoCrearActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.periodo.IPeriodo;

import java.util.List;

public class DialogAgregarSingleItem extends DialogFragment implements IAsignatura.AsignaturaCreateListener, IHorario.HorarioCrearListener, IPeriodo.PeriodoCreateListener {

    private static List<String> ListaItemSingleCkeck;
    private int posicion = -1;
    private static String itemAsignado = "";
    private String Componente;
    private String newItem;
    private ItemSingleCheckAdapter itemSingleCheckAdapter;

    public static DialogAgregarSingleItem newInstance(String Componente, List<String> ListaItemsComponente, String ItemAsignado){
        DialogAgregarSingleItem agregarSingleItem = new DialogAgregarSingleItem();
        ListaItemSingleCkeck = ListaItemsComponente;
        itemAsignado = ItemAsignado;
        Bundle bundle = new Bundle();
        bundle.putString("title",Componente);
        agregarSingleItem.setArguments(bundle);
        return agregarSingleItem;
    }

    public DialogAgregarSingleItem(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_single_item,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarAgreAsig);
        TextView nuevoItem = view.findViewById(R.id.btnNItemSingleCheck);
        RecyclerView listaItemsSingleCkeck = view.findViewById(R.id.checkListItemsSingleCheck);

        String Title = null;
        if (getArguments() != null){
            Componente = getArguments().getString(utilidades.TITULO);
            Title = "Agregar "+ Componente;
        }
        toolbar.setTitle(Title);

        nuevoItem.setText(String.format("Nuevo %s",Componente));
        nuevoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (Componente){
                    case "Asignatura":
                        llamarDialogCrearAsignatura();
                        break;
                    case "Horario":
                        llamarDialogCrearHorario();
                        break;
                    case "Periodo":
                        llamarDialogCrearPeriodo();
                        break;
                }
            }
        });

        if (!itemAsignado.isEmpty()){
            if (ListaItemSingleCkeck.size() != 0){
                for (int i = 0; i<ListaItemSingleCkeck.size();i++){
                    if (itemAsignado.equals(ListaItemSingleCkeck.get(i))){
                        posicion = i;
                    }
                }
            }
        }

        itemSingleCheckAdapter = new ItemSingleCheckAdapter(getContext(),ListaItemSingleCkeck,posicion);
        listaItemsSingleCkeck.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        listaItemsSingleCkeck.setAdapter(itemSingleCheckAdapter);

        toolbar.inflateMenu(R.menu.agregar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String Item = itemSingleCheckAdapter.getSelectedItem();
                if (Item.equals("")){
                    Item = String.format("Agregar %s",Componente);
                }
                listener.onRecibirItemAsignado(Componente, Item);
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

    private void llamarDialogCrearAsignatura(){
        AsignaturaCrearActivity asignaturaCrearActivity = AsignaturaCrearActivity.newInstance("Crear "+Componente,this);
        asignaturaCrearActivity.show(getParentFragmentManager(), utilidades.CREAR);
    }
    private void llamarDialogCrearHorario(){
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Crear "+Componente,this);
        horarioCrearActivity.show(getParentFragmentManager(), utilidades.CREAR);
    }
    private void llamarDialogCrearPeriodo(){
        PeriodoCrearActivity periodoCrearActivity = PeriodoCrearActivity.newInstance("Crear "+Componente, this);
        periodoCrearActivity.show(getParentFragmentManager(), utilidades.CREAR);
    }

    public interface RecibirItemListener{
        void onRecibirItemAsignado(String Componente, String ItemAsignado);
    }

    private RecibirItemListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RecibirItemListener)getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(" onRecibirItemAsignado no se esta implementando");
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
    public void onCrearAsignatura(Asignatura asignatura) {
        newItem = asignatura.getNombreAsignatura();
        ListaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearHorario(Horario horario) {
        newItem = String.format("%s Aula:%s De:%s A:%s",horario.getDia(), horario.getAula(), horario.getHora_entrada(),horario.getHora_salida());
        ListaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodoAcademico) {
        newItem = String.format("%s - %s",periodoAcademico.getFechaInicio(),periodoAcademico.getFechaFin());
        ListaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }
}
