package com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

public class EvaluacionDetalle extends DialogFragment {

    private static long idEvaluacion;

    public EvaluacionDetalle(){}

    public static EvaluacionDetalle newInstance(int id){
        idEvaluacion = id;
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Evaluación");
        evaluacionDetalle.setArguments(bundle);
        return evaluacionDetalle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detalle_evaluacion, container, false);

        OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDE);
        TextView nombreEva = view.findViewById(R.id.nomEvaDet);
        TextView fechaEva = view.findViewById(R.id.fechEvaDet);
        TextView tipoEva = view.findViewById(R.id.tipEvaDet);
        TextView bimestreEva = view.findViewById(R.id.bimEvaDet);
        TextView observacionEva = view.findViewById(R.id.ObsEvaDet);
        TextView cuetTitleEva = view.findViewById(R.id.cuesTitleEvaDet);
        TextView cuestPregEva = view.findViewById(R.id.cuestAnswerEvaDet);
        TextView asigEva = view.findViewById(R.id.asigEva);
        TextView paraEva = view.findViewById(R.id.parEva);

        Evaluacion evaluacion = operacionesEvaluacion.obtenerEva(idEvaluacion);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        if (evaluacion != null){
            nombreEva.setText(evaluacion.getNombreEvaluacion());
            fechaEva.setText(evaluacion.getFechaEvaluacion());
            tipoEva.setText(evaluacion.getTipo());
            bimestreEva.setText(evaluacion.getBimestre());

            if (!evaluacion.getObservacion().isEmpty()){
                observacionEva.setText(evaluacion.getObservacion());
            }else {
                String mensaje = "Sin Observaciones";
                observacionEva.setText(mensaje);
            }

            if (evaluacion.getCuestionarioID() != -1){
                OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());
                long idCues = evaluacion.getCuestionarioID();

                Cuestionario cuestionario = operacionesCuestionario.obtenerCuestionario(idCues);
                cuetTitleEva.setText(cuestionario.getNombreCuestionario());
                cuestPregEva.setText(cuestionario.getPreguntas());
            }else {
                String mensaje1 = "No agrego ningún Cuestionario";
                cuetTitleEva.setText(mensaje1);
                cuestPregEva.setText("");
            }

            if (evaluacion.getParaleloID() != null){
                OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
                OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

                Paralelo paralelo = operacionesParalelo.obtenerPar(evaluacion.getParaleloID());
                Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());

                asigEva.setText(asignatura.getNombreAsignatura());
                paraEva.setText(paralelo.getNombreParalelo());
            }else {
                String mensaje = "Sin Asignar";
                asigEva.setText(mensaje);
                paraEva.setText(" -- ");
            }

        }else {
            Toast.makeText(getContext(),"No se encontro la Tarea",Toast.LENGTH_LONG).show();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
}
