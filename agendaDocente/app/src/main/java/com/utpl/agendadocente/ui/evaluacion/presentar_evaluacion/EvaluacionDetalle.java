package com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion;

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

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.flyweight.PruebasFactory;

public class EvaluacionDetalle extends DialogFragment {

    private static Evaluacion evaluacion;
    private TextView asigEva;
    private TextView paraEva;
    private TextView cuetTitleEva;
    private TextView cuestPregEva;
    private TextView observacionEva;
    public EvaluacionDetalle(){
        //Required constructor
    }

    public static EvaluacionDetalle newInstance(Evaluacion eva, String bimestre){
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacion = (Evaluacion) PruebasFactory.getPrueba(bimestre);
        evaluacion = eva;
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

        Toolbar toolbar = view.findViewById(R.id.toolbarDE);
        TextView nombreEva = view.findViewById(R.id.nomEvaDet);
        TextView fechaEva = view.findViewById(R.id.fechEvaDet);
        TextView tipoEva = view.findViewById(R.id.tipEvaDet);
        TextView bimestreEva = view.findViewById(R.id.bimEvaDet);
        observacionEva = view.findViewById(R.id.ObsEvaDet);
        cuetTitleEva = view.findViewById(R.id.cuesTitleEvaDet);
        cuestPregEva = view.findViewById(R.id.cuestAnswerEvaDet);
        asigEva = view.findViewById(R.id.asigEva);
        paraEva = view.findViewById(R.id.parEva);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (evaluacion != null){
            nombreEva.setText(evaluacion.getNombreEvaluacion());
            fechaEva.setText(evaluacion.getFechaEvaluacion());
            tipoEva.setText(evaluacion.getTipo());
            bimestreEva.setText(evaluacion.getBimestre());
            obtenerObservacion();
            obtenerCuestionarioAsignado();
            obtenerParaleloAsignado();

        }else {
            Toast.makeText(getContext(),"No se encontro la Tarea",Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void obtenerObservacion() {
        if (!evaluacion.getObservacion().isEmpty()){
            observacionEva.setText(evaluacion.getObservacion());
        }else {
            String mensaje = "Sin Observaciones";
            observacionEva.setText(mensaje);
        }
    }

    private void obtenerCuestionarioAsignado() {
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
    }

    private void obtenerParaleloAsignado(){
        if (evaluacion.getParaleloID() != null){
            OperacionesParalelo operacionesParalelo = new OperacionesParalelo(getContext());
            OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(getContext());

            Paralelo paralelo = operacionesParalelo.obtenerParalelo(evaluacion.getParaleloID());
            Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());

            asigEva.setText(asignatura.getNombreAsignatura());
            paraEva.setText(paralelo.getNombreParalelo());
        }else {
            String mensaje = "Sin Asignar";
            asigEva.setText(mensaje);
            paraEva.setText(" -- ");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        presentarDialog(dialog);
    }

    public void presentarDialog(Dialog dialog){
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }
}
