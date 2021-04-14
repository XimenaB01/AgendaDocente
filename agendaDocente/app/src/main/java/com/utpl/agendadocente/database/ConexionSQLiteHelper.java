package com.utpl.agendadocente.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.utpl.agendadocente.util.Utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper{

    private static ConexionSQLiteHelper sqLiteHelper;

    private ConexionSQLiteHelper(Context context) {
        super(context, Utilidades.DATABASE_NAME, null, Utilidades.DATABASE_VERSION);
    }

    public static ConexionSQLiteHelper getInstance(Context context){
        if (sqLiteHelper == null){
            sqLiteHelper = new ConexionSQLiteHelper(context);
        }
        return sqLiteHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utilidades.CREAR_TABLA_ASIGNATURA);
        db.execSQL(Utilidades.CREAR_TABLA_DOCENTE);
        db.execSQL(Utilidades.CREAR_TABLA_PARALELO);
        db.execSQL(Utilidades.CREAR_TABLA_TAREA);
        db.execSQL(Utilidades.CREAR_TABLA_EVALUACION);
        db.execSQL(Utilidades.CREAR_TABLA_CUESTIONARIO);
        db.execSQL(Utilidades.CREAR_TABLA_HORARIOACADEMICO);
        db.execSQL(Utilidades.CREAR_TABLA_PERIODO);
        db.execSQL(Utilidades.CREAR_TABLA_PARALELO_DOCENTE);
        db.execSQL(Utilidades.CREAR_TABLA_COMPONENTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        String dropTableIfExists = "DROP TABLE IF EXISTS ";

        db.execSQL(dropTableIfExists+ Utilidades.TABLA_ASIGNATURA);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_DOCENTE);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_PARALELO);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_TAREA);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_EVALUACION);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_CUESTIONARIO);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_HORARIO_ACADEMICO);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_PERIODO);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_PARALELO_DOCENTE);
        db.execSQL(dropTableIfExists+ Utilidades.TABLA_COMPONENTES);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Habilita la restriccion de Actualizar y eliminar en cascada
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
