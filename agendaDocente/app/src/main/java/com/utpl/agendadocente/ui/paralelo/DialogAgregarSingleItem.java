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

import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.ui.asignatura.crear_asignatura.AsignaturaCrearActivity;
import com.utpl.agendadocente.decorador.intef.IAsignatura;
import com.utpl.agendadocente.ui.horario.crear_horario.HorarioCrearActivity;
import com.utpl.agendadocente.ui.horario.IHorario;
import com.utpl.agendadocente.ui.periodo.crear_periodo.PeriodoCrearActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.periodo.IPeriodo;

import java.util.List;

public class DialogAgregarSingleItem extends DialogFragment implements IAsignatura.AsignaturaCreateListener, IHorario.HorarioCrearListener, IPeriodo.PeriodoCreateListener {

    private static List<String> listaItemSingleCkeck;
    private int posicion = -1;
    private static String itemAsignado = "";
    private String texto = "Crear ";
    private String componente;
    private String newItem;
    private ItemSingleCheckAdapter itemSingleCheckAdapter;

    public static DialogAgregarSingleItem newInstance(String componente, List<String> listaItemsComponente, String itemsAsignado){
        DialogAgregarSingleItem agregarSingleItem = new DialogAgregarSingleItem();
        listaItemSingleCkeck = listaItemsComponente;
        itemAsignado = itemsAsignado;
        Bundle bundle = new Bundle();
        bundle.putString("title",componente);
        agregarSingleItem.setArguments(bundle);
        return agregarSingleItem;
    }

    public DialogAgregarSingleItem(){
        //required constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_agregar_single_item,container,false);

        Toolbar toolbar = view.findViewById(R.id.toolbarAgreAsig);
        TextView nuevoItem = view.findViewById(R.id.btnNItemSingleCheck);
        RecyclerView listaItemsSingleCkeck = view.findViewById(R.id.checkListItemsSingleCheck);

        String title = null;
        if (getArguments() != null){
            componente = getArguments().getString(Utilidades.TITULO);
            title = "Agregar "+ componente;
        }
        toolbar.setTitle(title);

        nuevoItem.setText(String.format("Nuevo %s", componente));
        nuevoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (componente){
                    case "Asignatura":
                        llamarDialogCrearAsignatura();
                        break;
                    case "Horario":
                        llamarDialogCrearHorario();
                        break;
                    case "Periodo":
                        llamarDialogCrearPeriodo();
                        break;
                    default:
                        //Ignore this part
                        break;
                }
            }
        });

        if (!itemAsignado.isEmpty() && !listaItemSingleCkeck.isEmpty()){
            for (int i = 0; i< listaItemSingleCkeck.size(); i++){
                if (itemAsignado.equals(listaItemSingleCkeck.get(i))){
                    posicion = i;
                }
            }
        }

        itemSingleCheckAdapter = new ItemSingleCheckAdapter(getContext(), listaItemSingleCkeck,posicion);
        listaItemsSingleCkeck.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        listaItemsSingleCkeck.setAdapter(itemSingleCheckAdapter);

        toolbar.inflateMenu(R.menu.agregar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String newItems = itemSingleCheckAdapter.getSelectedItem();
                if (newItems.equals("")){
                    newItems = String.format("Agregar %s", componente);
                }
                listener.onRecibirItemAsignado(componente, newItems);
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
        AsignaturaCrearActivity asignaturaCrearActivity = AsignaturaCrearActivity.newInstance(texto + componente,this);
        asignaturaCrearActivity.show(getParentFragmentManager(), Utilidades.CREAR);
    }
    private void llamarDialogCrearHorario(){
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance(texto + componente,this);
        horarioCrearActivity.show(getParentFragmentManager(), Utilidades.CREAR);
    }
    private void llamarDialogCrearPeriodo(){
        PeriodoCrearActivity periodoCrearActivity = PeriodoCrearActivity.newInstance(texto + componente, this);
        periodoCrearActivity.show(getParentFragmentManager(), Utilidades.CREAR);
    }

    public interface RecibirItemListener{
        void onRecibirItemAsignado(String componente, String itemAsignado);
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
        listaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearHorario(Horario horario) {
        newItem = String.format("%s Aula:%s De:%s A:%s",horario.getDia(), horario.getAula(), horario.getHoraEntrada(),horario.getHoraSalida());
        listaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodoAcademico) {
        newItem = String.format("%s - %s",periodoAcademico.getFechaInicio(),periodoAcademico.getFechaFin());
        listaItemSingleCkeck.add(newItem);
        itemSingleCheckAdapter.notifyDataSetChanged();
    }
}
