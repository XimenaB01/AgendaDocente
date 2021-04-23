package com.utpl.agendadocente.flyweight;

import android.content.Context;

import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesComponente;
import com.utpl.agendadocente.database.OperacionesCuestionario;
import com.utpl.agendadocente.database.OperacionesDocente;
import com.utpl.agendadocente.database.OperacionesEvaluacion;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.database.OperacionesTarea;

import java.util.HashMap;

public class OperacionesFactory {

    private static final HashMap<Context, OperacionesHorario> operacionHorarioMap = new HashMap<>();
    private static final HashMap<Context, OperacionesAsignatura> operacionAsignaturaMap = new HashMap<>();
    private static final HashMap<Context, OperacionesDocente> operacionDocenteMap = new HashMap<>();
    private static final HashMap<Context, OperacionesPeriodo> operacionPeriodoMap = new HashMap<>();
    private static final HashMap<Context, OperacionesParalelo> operacionParaleloMap = new HashMap<>();
    private static final HashMap<Context, OperacionesCuestionario> operacionCuestionarioMap = new HashMap<>();
    private static final HashMap<Context, OperacionesTarea> operacionTareaMap = new HashMap<>();
    private static final HashMap<Context, OperacionesEvaluacion> operacionEvaluacionMap = new HashMap<>();
    private static final HashMap<Context, OperacionesComponente> operacionComponenteMap = new HashMap<>();

    private OperacionesFactory(){
        //Requires constructor
    }

    public static OperacionesInterfaz.OperacionHorario getOperacionHorario(Context context){
        OperacionesHorario operacionesHorario = operacionHorarioMap.get(context);
        if (operacionesHorario == null) {
            operacionesHorario = new OperacionesHorario(context);
            operacionHorarioMap.put(context,operacionesHorario);
        }
        return operacionesHorario;
    }

    public static OperacionesInterfaz.OperacionDocente getOperacionDocente(Context context){
        OperacionesDocente operacionesDocente = operacionDocenteMap.get(context);
        if (operacionesDocente == null) {
            operacionesDocente = new OperacionesDocente(context);
            operacionDocenteMap.put(context,operacionesDocente);
        }
        return operacionesDocente;
    }

    public static OperacionesInterfaz.OperacionParalelo getOperacionParalelo(Context context){
        OperacionesParalelo operacionesParalelo = operacionParaleloMap.get(context);
        if (operacionesParalelo == null) {
            operacionesParalelo = new OperacionesParalelo(context);
            operacionParaleloMap.put(context,operacionesParalelo);
        }
        return operacionesParalelo;
    }

    public static OperacionesInterfaz.OperacionAsignatura getOperacionAsignatura(Context context){
        OperacionesAsignatura operacionesAsignatura = operacionAsignaturaMap.get(context);
        if (operacionesAsignatura == null) {
            operacionesAsignatura = new OperacionesAsignatura(context);
            operacionAsignaturaMap.put(context,operacionesAsignatura);
        }
        return operacionesAsignatura;
    }

    public static OperacionesInterfaz.OperacionPeriodo getOperacionPeriodo(Context context){
        OperacionesPeriodo operacionesPeriodo = operacionPeriodoMap.get(context);
        if (operacionesPeriodo == null) {
            operacionesPeriodo = new OperacionesPeriodo(context);
            operacionPeriodoMap.put(context,operacionesPeriodo);
        }
        return operacionesPeriodo;
    }

    public static OperacionesInterfaz.OperacionTarea getOperacionTarea(Context context){
        OperacionesTarea operacionesTarea = operacionTareaMap.get(context);
        if (operacionesTarea == null) {
            operacionesTarea = new OperacionesTarea(context);
            operacionTareaMap.put(context,operacionesTarea);
        }
        return operacionesTarea;
    }

    public static OperacionesInterfaz.OperacionCuestionario getOperacionCuestionario(Context context){
        OperacionesCuestionario operacionesCuestionario = operacionCuestionarioMap.get(context);
        if (operacionesCuestionario == null) {
            operacionesCuestionario = new OperacionesCuestionario(context);
            operacionCuestionarioMap.put(context,operacionesCuestionario);
        }
        return operacionesCuestionario;
    }

    public static OperacionesInterfaz.OperacionEvaluacion getOperacionEvaluacion(Context context){
        OperacionesEvaluacion operacionesEvaluacion = operacionEvaluacionMap.get(context);
        if (operacionesEvaluacion == null) {
            operacionesEvaluacion = new OperacionesEvaluacion(context);
            operacionEvaluacionMap.put(context,operacionesEvaluacion);
        }
        return operacionesEvaluacion;
    }

    public static OperacionesInterfaz.OperacionComponente getOperacionComponente(Context context){
        OperacionesComponente operacionesComponente = operacionComponenteMap.get(context);
        if (operacionesComponente == null) {
            operacionesComponente = new OperacionesComponente(context);
            operacionComponenteMap.put(context,operacionesComponente);
        }
        return operacionesComponente;
    }
}
