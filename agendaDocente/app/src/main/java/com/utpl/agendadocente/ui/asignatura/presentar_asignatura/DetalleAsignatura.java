package com.utpl.agendadocente.ui.asignatura.presentar_asignatura;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.util.Utilidades;

import java.util.List;

public class DetalleAsignatura extends DialogFragment {

    private static Integer idAsignatura;
    private TextView nivel;
    private TextView descripcion;
    private TextView duracion;
    private TextView temas;
    private MaterialCardView cardDes;
    private MaterialCardView cardTem;
    private MaterialCardView cardDur;
    private MaterialCardView cardNiv;

    public DetalleAsignatura() {
        // Required empty public constructor
    }

    public static DetalleAsignatura newInstance(int id){
        idAsignatura = id;
        DetalleAsignatura detalleAsignatura = new DetalleAsignatura();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Asignatura");
        detalleAsignatura.setArguments(bundle);

        return detalleAsignatura;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_detalle_asignatura, container, false);

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDA);
        TextView asignatura = view.findViewById(R.id.nombreAsig);
        TextView area = view.findViewById(R.id.areaAsig);
        TextView carrera = view.findViewById(R.id.carreraAsig);
        nivel = view.findViewById(R.id.nivelAsig);
        descripcion = view.findViewById(R.id.descripcionAsig);
        temas = view.findViewById(R.id.temAsig);
        duracion = view.findViewById(R.id.durAsig);
        TextView creditos = view.findViewById(R.id.creditosAsig);
        TextView horas = view.findViewById(R.id.horasAsig);
        cardDes = view.findViewById(R.id.cardDescripcion);
        cardTem = view.findViewById(R.id.cardTemas);
        cardNiv = view.findViewById(R.id.cardNivel);
        cardDur = view.findViewById(R.id.cardDuracion);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        Asignatura asig = operacionesAsignatura.obtenerAsignatura(idAsignatura);

        if (asig != null){
            asignatura.setText(asig.getNombreAsignatura());
            area.setText(asig.getArea());
            carrera.setText(asig.getCarrera());
            creditos.setText(asig.getCreditos());
            horas.setText(asig.getHorario());

            presetarComponentesAdicionales(idAsignatura);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else {
            Toast.makeText(getContext(),"No se encontro la Asignatura",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void presetarComponentesAdicionales(Integer idAsignatura) {
        OperacionesComponente operacionesComponente = new OperacionesComponente(getContext());
        List<Componente> list = operacionesComponente.obtenerComponentes(idAsignatura);
        for (int i = 0; i < list.size(); i++){
            switch (list.get(i).getComponente()) {
                case "Descripcion":
                    cardDes.setVisibility(View.VISIBLE);
                    descripcion.setText(list.get(i).getValor());
                    break;
                case "Nivel":
                    cardNiv.setVisibility(View.VISIBLE);
                    nivel.setText(list.get(i).getValor());
                    break;
                case "Temas":
                    cardTem.setVisibility(View.VISIBLE);
                    temas.setText(list.get(i).getValor());
                    break;
                case "Duracion":
                    cardDur.setVisibility(View.VISIBLE);
                    duracion.setText(list.get(i).getValor());
                    break;
                default:
                    //Ingnore this part
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }

}
