package com.utpl.agendadocente.util;

public class Utilidades {

    private Utilidades(){
        //Requiered constructor
    }

    public static final String DATABASE_NAME = "agendadb.db";
    public static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE = "create table ";
    private static final String TEXT_NOT_NULL_UNIQUE = " TEXT NOT NULL UNIQUE, ";
    private static final String TEXT_COMA = " TEXT, ";
    private static final String TEXT = " TEXT ";
    private static final String INTEGER_PK = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
    private static final String TEXT_NOT_NULL = " TEXT NOT NULL, ";
    private static final String INTEGER_NOT_NULL = " INTEGER NOT NULL, ";
    private static final String INTEGER = " INTEGER, ";
    private static final String FOREIGN_KEY = " FOREIGN KEY (";
    private static final String REFERENCE = ") REFERENCES ";
    private static final String UPDATE_CASCADE = ") ON UPDATE CASCADE ON DELETE CASCADE ";

    //Otras Constantes
    public static final String TITULO = "title";
    public static final String CREAR = "crear";
    public static final String ACTUALIZAR = "actualizar";
    public static final String REPLICAR = "replicar";

    //Constantes campos tabla Asignatura
    public static final String TABLA_ASIGNATURA="Asignatura";
    public static final String CAMPO_ID_ASI="id_asignatura";
    public static final String CAMPO_NOM_ASI="nombreAsignatura";
    public static final String CAMPO_AREA="area";
    public static final String CAMPO_CREDITOS="creditos";
    public static final String CAMPO_HORARIO="Horario";
    public static final String CAMPO_CARRERA="carrera";

    public static final String CREAR_TABLA_ASIGNATURA = CREATE_TABLE + TABLA_ASIGNATURA + " ("
            + CAMPO_ID_ASI + INTEGER_PK
            + CAMPO_NOM_ASI + TEXT_NOT_NULL_UNIQUE
            + CAMPO_AREA + TEXT_COMA
            + CAMPO_CREDITOS + TEXT_COMA
            + CAMPO_HORARIO + TEXT_COMA
            + CAMPO_CARRERA + TEXT
            + ")";

    //Costantes tabla Componentes

    public static final String TABLA_COMPONENTES = "Componentes";
    public static final String CAMPO_ID_COMP = "id_compontente";
    public static final String CAMPO_NOM_COMP = "nombreComponente";
    public static final String CAMPO_VAL_COMP = "valorComponente";
    public static final String CAMPO_ID_ASIG_FK = "id_asignatura";

    public static final String CREAR_TABLA_COMPONENTES = CREATE_TABLE + TABLA_COMPONENTES +" ("
            + CAMPO_ID_COMP + INTEGER_PK
            + CAMPO_NOM_COMP + TEXT_NOT_NULL
            + CAMPO_VAL_COMP + TEXT_COMA
            + CAMPO_ID_ASIG_FK + INTEGER_NOT_NULL
            + FOREIGN_KEY + CAMPO_ID_ASIG_FK + REFERENCE + TABLA_ASIGNATURA + " (" + CAMPO_ID_ASI + UPDATE_CASCADE
            + ")";

    //Constantes campos tabla Docente
    public static final String TABLA_DOCENTE = "Docente";
    public static final String CAMPO_ID_DOC = "id_docente";
    public static final String CAMPO_NOM_DOC = "nombreDocente";
    public static final String CAMPO_APE_DOC = "apellidoDocente";
    public static final String CAMPO_CEDULA = "cedula";
    public static final String CAMPO_CORREO = "email";

    public static final String CREAR_TABLA_DOCENTE = CREATE_TABLE + TABLA_DOCENTE+" ("
            + CAMPO_ID_DOC + INTEGER_PK
            + CAMPO_NOM_DOC + TEXT_NOT_NULL
            + CAMPO_APE_DOC + TEXT_COMA
            + CAMPO_CEDULA + TEXT_NOT_NULL_UNIQUE
            + CAMPO_CORREO + TEXT
            + ")";

    //Constantes campos tabla Cuestionario
    public static final String TABLA_CUESTIONARIO = "Cuestionario";
    public static final String CAMPO_ID_CUES = "id_cuestionario";
    public static final String CAMPO_NOM_CUES = "nombreCuestionario";
    public static final String CAMPO_PRE = "preguntas";

    public static final String CREAR_TABLA_CUESTIONARIO = CREATE_TABLE + TABLA_CUESTIONARIO + " ("
            + CAMPO_ID_CUES + INTEGER_PK
            + CAMPO_NOM_CUES + TEXT_NOT_NULL_UNIQUE
            + CAMPO_PRE + TEXT
            + ")";

    //Constantes campos tabla periodo
    public static final String TABLA_PERIODO="Periodo";
    public static final String CAMPO_ID_PER="id_periodo";
    public static final String CAMPO_FECH_INI="fechaInicio";
    public static final String CAMPO_FECH_FIN="fechaFin";

    public static final String CREAR_TABLA_PERIODO = CREATE_TABLE + TABLA_PERIODO + " ("
            + CAMPO_ID_PER + INTEGER_PK
            + CAMPO_FECH_INI + TEXT_COMA
            + CAMPO_FECH_FIN + TEXT
            + ")";

    //Constantes campos tabla Horario academico
    public static final String TABLA_HORARIO_ACADEMICO="HorarioAcademico";
    public static final String CAMPO_ID_HOR="id_horario";
    public static final String CAMPO_AULA="aula";
    public static final String CAMPO_DIA="dia";
    public static final String CAMPO_HOR_ENT="horaEntrada";
    public static final String CAMPO_HOR_SAL="horaSalida";

    public static final String CREAR_TABLA_HORARIOACADEMICO = CREATE_TABLE + TABLA_HORARIO_ACADEMICO+" ("
            + CAMPO_ID_HOR + INTEGER_PK
            + CAMPO_AULA + TEXT_COMA
            + CAMPO_DIA + TEXT_COMA
            + CAMPO_HOR_ENT + TEXT_COMA
            + CAMPO_HOR_SAL + TEXT
            + ")";

    //Constantes campos tabla Paralelo
    public static final String TABLA_PARALELO="Paralelo";
    public static final String CAMPO_ID_PAR="idParalelo";
    public static final String CAMPO_NOM_PAR="nombreParalelo";
    public static final String CAMPO_NUM_ES="num_estudiantes";
    public static final String CAMPO_HOR_ID="horarioID";
    public static final String CAMPO_ASI_ID="asignaturaID";
    public static final String CAMPO_PER_ID="periodoID";

    public static final String CREAR_TABLA_PARALELO = CREATE_TABLE + TABLA_PARALELO+"("
            + CAMPO_ID_PAR + INTEGER_PK
            + CAMPO_NOM_PAR + TEXT_NOT_NULL
            + CAMPO_NUM_ES + TEXT_COMA
            + CAMPO_HOR_ID  + INTEGER
            + CAMPO_ASI_ID + INTEGER_NOT_NULL
            + CAMPO_PER_ID + " INTEGER"
            + ")";

    //Constantes tabla Paralelo_Docente
    public static final String TABLA_PARALELO_DOCENTE="Paralelo_Docente";
    public static final String CAMPO_PARALELO_ID_FK3 = "paralelo_id_fk";
    public static final String CAMPO_DOCENTE_ID_FK = "docente_id_fk";
    private static final String CAMPO_PARALELO_SUB_CONSTRAINT = "paralelo_sub_unique";
    public static final String CREAR_TABLA_PARALELO_DOCENTE = CREATE_TABLE + TABLA_PARALELO_DOCENTE + " ("
            + CAMPO_PARALELO_ID_FK3 + INTEGER_NOT_NULL
            + CAMPO_DOCENTE_ID_FK + INTEGER_NOT_NULL
            + FOREIGN_KEY + CAMPO_PARALELO_ID_FK3 + REFERENCE + TABLA_PARALELO + " (" + CAMPO_ID_PAR + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + FOREIGN_KEY + CAMPO_DOCENTE_ID_FK + REFERENCE + TABLA_DOCENTE + " (" + CAMPO_ID_DOC + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "CONSTRAINT " + CAMPO_PARALELO_SUB_CONSTRAINT + " UNIQUE (" + CAMPO_PARALELO_ID_FK3 + ", " + CAMPO_DOCENTE_ID_FK + ")"
            +" )";

    //Constantes campos tabla Tarea
    public static final String TABLA_TAREA="Tarea";
    public static final String CAMPO_ID_TAR="id_tarea";
    public static final String CAMPO_NOM_TAR="nombreTarea";
    public static final String CAMPO_DES_TAR="descripcionTarea";
    public static final String CAMPO_FEC_TAR="fechaTarea";
    public static final String CAMPO_OBS_TAR="observacionTarea";
    public static final String CAMPO_EST_TAR="estadoTarea";
    public static final String CAMPO_PARALELO_ID_FK1="paralelo_id_fk";
    private static final String CAMPO_TAREA_SUB_CONSTRAINT = "tarea_sub_unique";

    public static final String CREAR_TABLA_TAREA = CREATE_TABLE + TABLA_TAREA+" ("
            + CAMPO_ID_TAR + INTEGER_PK
            + CAMPO_NOM_TAR + TEXT_NOT_NULL
            + CAMPO_DES_TAR + TEXT_COMA
            + CAMPO_FEC_TAR + TEXT_COMA
            + CAMPO_OBS_TAR + TEXT_COMA
            + CAMPO_EST_TAR + TEXT_COMA
            + CAMPO_PARALELO_ID_FK1 + INTEGER
            + " CONSTRAINT " + CAMPO_TAREA_SUB_CONSTRAINT + FOREIGN_KEY + CAMPO_PARALELO_ID_FK1 + REFERENCE + TABLA_PARALELO + " (" + CAMPO_ID_PAR + UPDATE_CASCADE
            + ")";

    //Constantes campos tabla Evaluacion
    public static final String TABLA_EVALUACION="Evaluacion";
    public static final String CAMPO_ID_EVA="id_evaluacion";
    public static final String CAMPO_NOM_EVA="nombreEvaluacion";
    public static final String CAMPO_TIPO_EVA="tipo";
    public static final String CAMPO_FEC_EVA="fecha";
    public static final String CAMPO_BIM_EVA="bimestre";
    public static final String CAMPO_OBS_EVA="observacionEvaluacion";
    public static final String CAMPO_CUES_ID="cuestionarioID";
    public static final String CAMPO_PARALELO_ID_FK2="paralelo_id_fk";
    private static final String CAMPO_EVALUACION_SUB_CONSTRAINT = "evaluacion_sub_unique";

    public static final String CREAR_TABLA_EVALUACION = CREATE_TABLE + TABLA_EVALUACION + "("
            + CAMPO_ID_EVA + INTEGER_PK
            + CAMPO_NOM_EVA + TEXT_NOT_NULL
            + CAMPO_TIPO_EVA + TEXT_COMA
            + CAMPO_FEC_EVA + TEXT_COMA
            + CAMPO_BIM_EVA + TEXT_COMA
            + CAMPO_OBS_EVA + TEXT_COMA
            + CAMPO_CUES_ID + INTEGER
            + CAMPO_PARALELO_ID_FK2 + INTEGER
            + " CONSTRAINT " + CAMPO_EVALUACION_SUB_CONSTRAINT + FOREIGN_KEY + CAMPO_PARALELO_ID_FK2 + REFERENCE + TABLA_PARALELO + " (" + CAMPO_ID_PAR + UPDATE_CASCADE
            + ")";
}
