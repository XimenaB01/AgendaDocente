package com.utpl.agendadocente.features.docente.crear_docente;

import androidx.annotation.NonNull;
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
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.features.docente.IDocente;
import com.utpl.agendadocente.features.docente.ValidarCorreo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.util.Utilidades;

import java.util.Objects;

public class CrearDocenteActivity extends DialogFragment {

    private static IDocente.DocenteCreateListener docenteCreateListener;
    private IDocente.DocenteCreateListener listener;

    private TextInputEditText nombres;
    private TextInputEditText apellidos;
    private TextInputEditText cedula;
    private TextInputEditText correo;

    public CrearDocenteActivity(){
        //Required constructor
    }

    public static CrearDocenteActivity newInstance(String title, IDocente.DocenteCreateListener listener){
        docenteCreateListener = listener;
        CrearDocenteActivity crearDoc = new CrearDocenteActivity();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        crearDoc.setArguments(bundle);

        crearDoc.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        return crearDoc;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (docenteCreateListener != context){
                listener = (IDocente.DocenteCreateListener) getTargetFragment();
            }
        }catch (ClassCastException e ){
            throw new ClassCastException(requireActivity().toString() + " must implements DocenteCreateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_crear_docente, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarD);
        nombres = view.findViewById(R.id.nomDoc);
        apellidos = view.findViewById(R.id.apeDoc);
        cedula = view.findViewById(R.id.cedDoc);
        correo = view.findViewById(R.id.corrDoc);

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

        toolbar.inflateMenu(R.menu.guardar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                validarDatosDocente();
                return true;
            }
        });
        return view;
    }

    private void validarDatosDocente(){
        ValidarCorreo validarCorreo = new ValidarCorreo();

        String nomDoc = Objects.requireNonNull(nombres.getText()).toString();
        String apeDoc = Objects.requireNonNull(apellidos.getText()).toString();
        String cedDoc = Objects.requireNonNull(cedula.getText()).toString();
        String corDoc = Objects.requireNonNull(correo.getText()).toString();

        Docente docente = new Docente();

        if (!nomDoc.isEmpty() && !apeDoc.isEmpty()){
            if (cedDoc.length() == 10){
                if (validarCorreo.validar(corDoc)){
                    docente.setNombreDocente(nomDoc);
                    docente.setApellidoDocente(apeDoc);
                    docente.setEmail(corDoc);
                    docente.setCedula(cedDoc);
                    OperacionesDocente operacionesDocente = (OperacionesDocente) OperacionesFactory.getOperacionDocente(getContext());
                    long insercion = operacionesDocente.insertarDocente(docente);
                    actualizarListaDocentes(insercion, docente);
                }else {
                    Toast.makeText(getContext(),"Ingrese correctamente su correo",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getContext(),"El número de cedula es incorrecto",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(), "Llene los campos nombres y apellidos", Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarListaDocentes(long insercion, Docente docente) {
        if (insercion > 0) {
            docente.setIdDocente((int) insercion);
            if (docenteCreateListener != null){
                docenteCreateListener.onCrearDocente(docente);
            }else {
                listener.onCrearDocente(docente);
            }
            Objects.requireNonNull(getDialog()).dismiss();
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
}
