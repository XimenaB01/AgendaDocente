package com.utpl.agendadocente.features.docente.actualizar_docente;

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
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.features.flyweight.OperacionesFactory;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.features.docente.IDocente;
import com.utpl.agendadocente.features.docente.ValidarCorreo;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.features.evaluacion.presentar_evaluacion.EvaluacionDetalle;
import com.utpl.agendadocente.util.Utilidades;

import java.util.Objects;

public class ActualizarDocente extends DialogFragment {

    private static long idDocent;
    private static int docentItemPosition;
    private static IDocente.ActualizarDocenteListener actualizarDocenteListener;

    private Docente docente;

    private TextInputEditText nomAct;
    private TextInputEditText apelAct;
    private TextInputEditText cedAct;
    private TextInputEditText emailAct;

    private OperacionesDocente operacionesDocente;

    public ActualizarDocente(){
        //Required constructor
    }

    public static ActualizarDocente newInstance(long id, int position, IDocente.ActualizarDocenteListener listener){
        idDocent = id;
        docentItemPosition = position;
        actualizarDocenteListener = listener;
        ActualizarDocente actualizarDocente = new ActualizarDocente();
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

        operacionesDocente = (OperacionesDocente) OperacionesFactory.getOperacionDocente(getContext());

        Toolbar toolbar = view.findViewById(R.id.toolbarDA);
        nomAct = view.findViewById(R.id.nomDocAct);
        apelAct = view.findViewById(R.id.apeDocAct);
        cedAct = view.findViewById(R.id.cedDocAct);
        emailAct = view.findViewById(R.id.corrDocAct);

        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString(Utilidades.TITULO);
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
                    ActualizarDocente.this.guardarDocente();
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

    private void guardarDocente(){
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

                    long insertion = operacionesDocente.modificarDocente(docente);

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
        EvaluacionDetalle evaluacionDetalle = new EvaluacionDetalle();
        evaluacionDetalle.presentarDialog(dialog);
    }
}
