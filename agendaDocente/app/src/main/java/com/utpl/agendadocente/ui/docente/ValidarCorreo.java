package com.utpl.agendadocente.ui.docente;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidarCorreo {

    public boolean validar (String email){
        boolean correo = false;

        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()){
            correo = true;
        }
        return correo;
    }
}
