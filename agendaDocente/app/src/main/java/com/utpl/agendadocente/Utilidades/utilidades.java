package com.utpl.agendadocente.Utilidades;

public class utilidades {
    public static final String DATABASE_NAME = "agendadb.db";
    public static final int DATABASE_VERSION = 1;

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
    public static final String CAMPO_NIVEL="nivel";
    public static final String CAMPO_DESCRIPCION="descripcionAsignatura";
    public static final String CAMPO_CARRERA="carrera";

    public static final String CREAR_TABLA_ASIGNATURA = "create table "+TABLA_ASIGNATURA+" ("
            + CAMPO_ID_ASI+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CAMPO_NOM_ASI+" TEXT NOT NULL UNIQUE, "
            + CAMPO_AREA+" TEXT, "
            + CAMPO_CREDITOS+" TEXT, "
            + CAMPO_HORARIO+" TEXT, "
            + CAMPO_DESCRIPCION+" TEXT, "
            + CAMPO_NIVEL+" TEXT, "
            + CAMPO_CARRERA+" TEXT "
            + ")";

    //Constantes campos tabla Docente
    public static final String TABLA_DOCENTE="Docente";
    public static final String CAMPO_ID_DOC="id_docente";
    public static final String CAMPO_NOM_DOC="nombreDocente";
    public static final String CAMPO_APE_DOC="apellidoDocente";
    public static final String CAMPO_CEDULA="cedula";
    public static final String CAMPO_CORREO="email";

    public static final String CREAR_TABLA_DOCENTE = "create table "+TABLA_DOCENTE+" ("
            + CAMPO_ID_DOC+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOM_DOC+" TEXT NOT NULL, "
            + CAMPO_APE_DOC+" TEXT, "
            + CAMPO_CEDULA+" TEXT NOT NULL UNIQUE, "
            + CAMPO_CORREO+" TEXT "
            + ")";

    //Constantes campos tabla Tarea
    public static final String TABLA_TAREA="Tarea";
    public static final String CAMPO_ID_TAR="id_tarea";
    public static final String CAMPO_NOM_TAR="nombreTarea";
    public static final String CAMPO_DES_TAR="descripcionTarea";
    public static final String CAMPO_FEC_TAR="fechaTarea";
    public static final String CAMPO_OBS_TAR="observacionTarea";

    public static final String CREAR_TABLA_TAREA = "create table "+TABLA_TAREA+" ("
            + CAMPO_ID_TAR+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOM_TAR+" TEXT NOT NULL UNIQUE, "
            + CAMPO_DES_TAR+" TEXT, "
            + CAMPO_FEC_TAR+" TEXT,"
            + CAMPO_OBS_TAR+" TEXT"
            + ")";

    //Constantes campos tabla Evaluacion
    public static final String TABLA_EVALUACION="Evaluacion";
    public static final String CAMPO_ID_EVA="id_evaluacion";
    public static final String CAMPO_NOM_EVA="nombreEvaluacion";
    public static final String CAMPO_TIPO="tipo";
    public static final String CAMPO_FEC_EVA="fecha";
    public static final String CAMPO_BIM_EVA="bimestre";
    public static final String CAMPO_OBS_EVA="observacionEvaluacion";
    public static final String CAMPO_CUES_ID="cuestionarioID";

    public static final String CREAR_TABLA_EVALUACION = "create table "+TABLA_EVALUACION+"("
            + CAMPO_ID_EVA+" INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOM_EVA+" TEXT NOT NULL UNIQUE, "
            + CAMPO_TIPO+" TEXT, "
            + CAMPO_FEC_EVA+" TEXT, "
            + CAMPO_BIM_EVA+" TEXT, "
            + CAMPO_OBS_EVA+" TEXT, "
            + CAMPO_CUES_ID+" INTEGER"
            + ")";

    //Constantes campos tabla Cuestionario
    public static final String TABLA_CUESTIONARIO="Cuestionario";
    public static final String CAMPO_ID_CUES="id_cuestionario";
    public static final String CAMPO_NOM_CUES="nombreCuestionario";
    public static final String CAMPO_PRE="preguntas";

    public static final String CREAR_TABLA_CUESTIONARIO = "create table "+TABLA_CUESTIONARIO+" ("
            + CAMPO_ID_CUES+" INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOM_CUES+" TEXT NOT NULL UNIQUE, "
            + CAMPO_PRE+" TEXT"
            + ")";

    //Constantes campos tabla periodo
    public static final String TABLA_PERIODO="Periodo";
    public static final String CAMPO_ID_PER="id_periodo";
    public static final String CAMPO_FECH_INI="fechaInicio";
    public static final String CAMPO_FECH_FIN="fechaFin";

    public static final String CREAR_TABLA_PERIODO = "create table "+TABLA_PERIODO+" ("
            + CAMPO_ID_PER+" INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_FECH_INI+" TEXT, "
            + CAMPO_FECH_FIN+" TEXT "
            + ")";

    //Constantes campos tabla Horario academico
    public static final String TABLA_HORARIO_ACADEMICO="HorarioAcademico";
    public static final String CAMPO_ID_HOR="id_horario";
    public static final String CAMPO_AULA="aula";
    public static final String CAMPO_HOR_ENT="horaEntrada";
    public static final String CAMPO_HOR_SAL="horaSalida";

    public static final String CREAR_TABLA_HORARIOACADEMICO = "create table "+TABLA_HORARIO_ACADEMICO+" ("
            + CAMPO_ID_HOR+" INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_AULA+" TEXT, "
            + CAMPO_HOR_ENT+" TEXT, "
            + CAMPO_HOR_SAL+" TEXT "
            + ")";

    //Constantes campos tabla Paralelo
    public static final String TABLA_PARALELO="Paralelo";
    public static final String CAMPO_NOM_PAR="nombreParalelo";
    public static final String CAMPO_NUM_ES="num_estudiantes";
    public static final String CAMPO_HOR_ID="horarioID";
    public static final String CAMPO_ASI_ID="asignaturaID";
    public static final String CAMPO_PER_ID="periodoID";

    public static final String CREAR_TABLA_PARALELO = "create table "+TABLA_PARALELO+"("
            + CAMPO_NOM_PAR+" TEXT NOT NULL, "
            + CAMPO_NUM_ES +" TEXT, "
            + CAMPO_HOR_ID +" INTEGER, "
            + CAMPO_ASI_ID +" INTEGER NOT NULL, "
            + CAMPO_PER_ID +" INTEGER , "
            + " PRIMARY KEY ( " + CAMPO_NOM_PAR +" , "+ CAMPO_ASI_ID + " )"
            + ")";

    //Constantes tabla Paralelo_Docente
    public static final String TABLA_PARALELO_DOCENTE="Paralelo_Docente";
    public static final String CAMPO_PARALELO_NOM_FK = "paralelo_nombre_fk";
    public static final String CAMPO_PARALELO_ASIG_FK = "paralelo_asig_fk";
    public static final String CAMPO_DOCENTE_ID_FK = "docente_id_fk";
    private static final String CAMPO_PARALELO_SUB_CONSTRAINT = "paralelo_sub_unique";
    public static final String CREAR_TABLA_PARALELO_DOCENTE ="create table " + TABLA_PARALELO_DOCENTE + " ("
            + CAMPO_PARALELO_NOM_FK + " TEXT NOT NULL, "
            + CAMPO_PARALELO_ASIG_FK + " INTEGER NOT NULL, "
            + CAMPO_DOCENTE_ID_FK + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + CAMPO_PARALELO_NOM_FK +", "+ CAMPO_PARALELO_ASIG_FK + ") REFERENCES " + TABLA_PARALELO + " (" + CAMPO_NOM_PAR + ", "+ CAMPO_ASI_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "FOREIGN KEY (" + CAMPO_DOCENTE_ID_FK + ") REFERENCES "+ TABLA_DOCENTE + " (" + CAMPO_ID_DOC + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "CONSTRAINT " + CAMPO_PARALELO_SUB_CONSTRAINT + " UNIQUE (" + CAMPO_PARALELO_NOM_FK +", "+ CAMPO_PARALELO_ASIG_FK+ ", " + CAMPO_DOCENTE_ID_FK + ")"
            +" )";

    //Constantes tabla Paralelo_Tarea
    public static final String TABLA_PARALELO_TAREA = "Paralelo_Tarea";
    public static final String CAMPO_PARALELO_NOM_FK2 = "paralelo_nombre_fk";
    public static final String CAMPO_PARALELO_ASIG_FK2 = "paralelo_asig_fk";
    public static final String CAMPO_TAREA_ID_FK = "tarea_id_fk";
    private static final String CAMPO_PARALELO_SUB_CONSTRAINT1 = "paralelo_sub_unique";
    public static final String CREAR_TABLA_PARALELO_TAREA ="create table " + TABLA_PARALELO_TAREA + " ("
            + CAMPO_PARALELO_NOM_FK2 + " TEXT NOT NULL, "
            + CAMPO_PARALELO_ASIG_FK2 +" INTEGER NOT NULL,"
            + CAMPO_TAREA_ID_FK + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + CAMPO_PARALELO_NOM_FK2+", "+ CAMPO_PARALELO_ASIG_FK2 + ") REFERENCES " + TABLA_PARALELO + " (" + CAMPO_NOM_PAR + ", " + CAMPO_ASI_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "FOREIGN KEY (" + CAMPO_TAREA_ID_FK + ") REFERENCES "+ TABLA_TAREA + " (" + CAMPO_ID_TAR + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "CONSTRAINT " + CAMPO_PARALELO_SUB_CONSTRAINT1 + " UNIQUE (" + CAMPO_PARALELO_NOM_FK2 + ", "+ CAMPO_PARALELO_ASIG_FK2 +", " + CAMPO_TAREA_ID_FK + ")"
            +" )";

    //Constantes tabla Paralelo_Evaluacion
    public static final String TABLA_PARALELO_EVALUACION = "Paralelo_Evaluacion";
    public static final String CAMPO_PARALELO_NOM_FK3 = "paralelo_nombre_fk";
    public static final String CAMPO_PARALELO_ASIG_FK3 = "paralelo_asig_fk";
    public static final String CAMPO_EVALUACION_ID_FK = "evaluacion_id_fk";
    private static final String CAMPO_PARALELO_SUB_CONSTRAINT2 = "paralelo_sub_unique";
    public static final String CREAR_TABLA_PARALELO_EVALUACION ="create table " + TABLA_PARALELO_EVALUACION + " ("
            + CAMPO_PARALELO_NOM_FK3 + " TEXT NOT NULL, "
            + CAMPO_PARALELO_ASIG_FK3 +" INTEGER NOT NULL,"
            + CAMPO_EVALUACION_ID_FK + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + CAMPO_PARALELO_NOM_FK3 + ", " + CAMPO_PARALELO_ASIG_FK3 + ") REFERENCES " + TABLA_PARALELO + " (" + CAMPO_NOM_PAR + ", " + CAMPO_ASI_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "FOREIGN KEY (" + CAMPO_EVALUACION_ID_FK + ") REFERENCES "+ TABLA_EVALUACION + " (" + CAMPO_ID_EVA + ") ON UPDATE CASCADE ON DELETE CASCADE, "
            + "CONSTRAINT " + CAMPO_PARALELO_SUB_CONSTRAINT2 + " UNIQUE (" + CAMPO_PARALELO_NOM_FK3 + ", "+ CAMPO_PARALELO_ASIG_FK3 +", " +CAMPO_EVALUACION_ID_FK + ")"
            +" )";
}
