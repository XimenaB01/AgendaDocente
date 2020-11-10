package com.utpl.agendadocente.ui.docente.ActualizarDocente;

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
import com.utpl.agendadocente.DataBase.OperacionesDocente;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.ui.docente.ValidarCorreo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.Objects;

public class actualizarDocente extends DialogFragment {

    private static long idDocent;
    private static int docentItemPosition;
    private static ActualizarDocenteListener actualizarDocenteListener;

    private Docente docente;

    private TextInputEditText nomAct, apelAct, cedAct, emailAct;

    private OperacionesDocente operacionesDocente;

    public actualizarDocente(){
    }

    public static actualizarDocente newInstance(long id, int position, ActualizarDocenteListener listener){
        idDocent = id;
        docentItemPosition = position;
        actualizarDocenteListener = listener;
        actualizarDocente actualizarDocente = new actualizarDocente();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Editar Docente");
        actualizarDocente.setArguments(bundle);

        actualizarDocente.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        return actualizarDocente;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_actualizar_docente,container,false);

        operacionesDocente = new OperacionesDocente(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDA);
        nomAct = view.findViewById(R.id.nomDocAct);
        apelAct = view.findViewById(R.id.apeDocAct);
        cedAct = view.findViewById(R.id.cedDocAct);
        emailAct = view.findViewById(R.id.corrDocAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(utilidades.TITULO);
        }
        toolbar.setTitle(title);

        docente = operacionesDocente.obtenerdocente(idDocent);

        if (docente!=null){
            nomAct.setText(docente.getNombreDocente());
            apelAct.setText(docente.getApellidoDocente());
            cedAct.setText(docente.getCedula());
            emailAct.setText(docente.getEmail());

            toolbar.inflateMenu(R.menu.actualizar);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ActualizarDocente();
                    return true;
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        return view;
    }

    private void ActualizarDocente(){
        ValidarCorreo validarCorreo = new ValidarCorreo();

        String nombre = Objects.requireNonNull(nomAct.getText()).toString();
        String apellido = Objects.requireNonNull(apelAct.getText()).toString();
        String email = Objects.requireNonNull(emailAct.getText()).toString();
        String cedula = Objects.requireNonNull(cedAct.getText()).toString();

        if (!nombre.isEmpty() && !apellido.isEmpty()){
            if (cedula.length() == 10){
                if (validarCorreo.validar(email)){
                    docente.setNombreDocente(nombre);
                    docente.setApellidoDocente(apellido);
                    docente.setCedula(cedula);
                    docente.setEmail(email);

                    long insertion = operacionesDocente.ModificarDoc(docente);

                    if (insertion > 0){
                        actualizarDocenteListener.onActualizarDocente(docente, docentItemPosition);
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                }else {
                    Toast.makeText(getContext(),"Ingrese correctamente su correo",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getContext(),"El número de cedula es incorrecto",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(),"Llene los campos nombres y apellidos",Toast.LENGTH_LONG).show();
        }
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
        }
    }
}
