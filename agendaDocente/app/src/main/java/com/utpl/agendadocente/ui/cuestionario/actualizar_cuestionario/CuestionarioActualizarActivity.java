package com.utpl.agendadocente.ui.cuestionario.actualizar_cuestionario;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.util.Utilidades;
import com.utpl.agendadocente.ui.cuestionario.ICuestionario;

import java.util.Objects;

public class CuestionarioActualizarActivity extends DialogFragment {

    private static long idCuestionario;
    private static int cuestionarioItemPosition;
    private static ICuestionario.ActualizarCuestionarioListener actualizarCuestionarioListener;

    private Cuestionario cuestionario;

    private TextInputEditText tvNombreAct;
    private TextInputEditText tvPreguntasAct;

    private String nombreCuestAct = "";
    private String preguntasCuestAct = "";

    private OperacionesCuestionario operacionesCuestionario;

    public CuestionarioActualizarActivity(){
        //Required constructor
    }

    public static CuestionarioActualizarActivity newInstance(long id, int position, ICuestionario.ActualizarCuestionarioListener listener){
        idCuestionario = id;
        cuestionarioItemPosition = position;
        actualizarCuestionarioListener = listener;
        CuestionarioActualizarActivity cuestionarioActualizarActivity = new CuestionarioActualizarActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Editar Cuestionario");
        cuestionarioActualizarActivity.setArguments(bundle);

        cuestionarioActualizarActivity.setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
        return cuestionarioActualizarActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_actualizar_cuestionario,container,false);

        operacionesCuestionario = new OperacionesCuestionario(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarC);
        tvNombreAct = view.findViewById(R.id.txtCuestAct);
        tvPreguntasAct = view.findViewById(R.id.txtPregAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        cuestionario = operacionesCuestionario.obtenerCuestionario(idCuestionario);

        if (cuestionario!=null){
            tvNombreAct.setText(cuestionario.getNombreCuestionario());
            tvPreguntasAct.setText(cuestionario.getPreguntas());

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    nombreCuestAct = Objects.requireNonNull(tvNombreAct.getText()).toString();
                    preguntasCuestAct = Objects.requireNonNull(tvPreguntasAct.getText()).toString();

                    if (!nombreCuestAct.isEmpty()){
                        cuestionario.setNombreCuestionario(nombreCuestAct);
                        cuestionario.setPreguntas(preguntasCuestAct);
                        guardarCuestionario();

                    }else {
                        Toast.makeText(getContext(), "Agregue el nombre", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        }
        return view;
    }

    private void guardarCuestionario(){

        long insertion = operacionesCuestionario.modificarCuestionario(cuestionario);

        if (insertion>0){
            actualizarCuestionarioListener.onActualizarCuestionario(cuestionario, cuestionarioItemPosition);
            dismiss();
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
