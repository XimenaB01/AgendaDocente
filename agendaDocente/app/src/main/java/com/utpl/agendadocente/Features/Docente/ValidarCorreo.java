package com.utpl.agendadocente.Features.Docente;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidarCorreo {

    public boolean validar (String Email){
        boolean correo = false;

        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher matcher = pattern.matcher(Email);
        if (matcher.find()){
            correo = true;
        }
        return correo;
    }
}
