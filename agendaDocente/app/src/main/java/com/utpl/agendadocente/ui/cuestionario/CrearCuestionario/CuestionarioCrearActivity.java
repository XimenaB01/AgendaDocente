package com.utpl.agendadocente.ui.cuestionario.CrearCuestionario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.Model.Cuestionario;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;
import com.utpl.agendadocente.ui.cuestionario.ICuestionario;

import java.util.Objects;

public class CuestionarioCrearActivity extends DialogFragment {

    private static ICuestionario.CuestionarioCrearListener cuestionarioCrearListener;
    private ICuestionario.CuestionarioCrearListener listener;

    private TextInputEditText tvNombre, tvPreguntas;

    private OperacionesCuestionario operacionesCuestionario;

    private String nombreC = "";
    private String preguntasC = "";

    public CuestionarioCrearActivity(){}

    public static CuestionarioCrearActivity newInstance (String title, ICuestionario.CuestionarioCrearListener listener){
        if (listener != null){
            cuestionarioCrearListener = listener;
        }
        CuestionarioCrearActivity crearCuest = new CuestionarioCrearActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        crearCuest.setArguments(bundle);
        crearCuest.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        return crearCuest;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (cuestionarioCrearListener != context){
                listener = (ICuestionario.CuestionarioCrearListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements CuestionarioCrearListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_cuestionario, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarC);
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        tvNombre = view.findViewById(R.id.txtCuest);
        tvPreguntas = view.findViewById(R.id.txtPreg);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                nombreC =  Objects.requireNonNull(tvNombre.getText()).toString();
                preguntasC = Objects.requireNonNull(tvPreguntas.getText()).toString();

                Cuestionario cuest = new Cuestionario();

                if(!nombreC.isEmpty()){
                    cuest.setNombreCuestionario(nombreC);
                    cuest.setPreguntas(preguntasC);

                    operacionesCuestionario = new OperacionesCuestionario(getContext());

                    long insercion = operacionesCuestionario.InsertarCuest(cuest);

                    if (insercion > 0){
                        int inser = (int)insercion;
                        cuest.setId_cuestionario(inser);
                        if (cuestionarioCrearListener != null) {
                            cuestionarioCrearListener.onCrearCuestionario(cuest);
                            dismiss();
                        }else {
                            listener.onCrearCuestionario(cuest);
                            dismiss();
                        }
                    }
                }else {
                    Toast.makeText(getContext(),"Agregue el nombre del cuestionario",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

}
