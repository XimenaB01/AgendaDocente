package com.utpl.agendadocente.ui.cuestionario.presentar_cuestionario;

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

import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;

public class CuestionarioDetalle extends DialogFragment {

    private static long idCuestionario;

    public CuestionarioDetalle(){
        //Required constructor
    }

    public static CuestionarioDetalle newInstance(int id){
        idCuestionario = id;
        CuestionarioDetalle cuestionarioDetalle = new CuestionarioDetalle();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Detalle Cuestionario");
        cuestionarioDetalle.setArguments(bundle);
        return cuestionarioDetalle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_detalle_cuestionario, container, false);

        OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDC);
        TextView nombreCuest = view.findViewById(R.id.nomCuesDet);
        TextView pregCuest = view.findViewById(R.id.pregCuesDet);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
        }
        toolbar.setTitle(title);

        Cuestionario cuestionario = operacionesCuestionario.obtenerCuestionario(idCuestionario);

        if (cuestionario != null){
            nombreCuest.setText(cuestionario.getNombreCuestionario());
            pregCuest.setText(cuestionario.getPreguntas());

        }else {
            Toast.makeText(getContext(),"No se encontro el Cuestionario",Toast.LENGTH_LONG).show();
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
