package com.utpl.agendadocente.DataBase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.utpl.agendadocente.Utilidades.utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper{

    private static ConexionSQLiteHelper SQLiteHelper;

    private ConexionSQLiteHelper(Context context) {
        super(context, utilidades.DATABASE_NAME, null, utilidades.DATABASE_VERSION);
    }

    public static ConexionSQLiteHelper getInstance(Context context){
        if (SQLiteHelper == null){
            synchronized (ConexionSQLiteHelper.class){//synchronized permite que se ejecute un solo hilo
                if (SQLiteHelper == null)
                    SQLiteHelper = new ConexionSQLiteHelper(context);
            }
        }
        return SQLiteHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(utilidades.CREAR_TABLA_ASIGNATURA);
        db.execSQL(utilidades.CREAR_TABLA_DOCENTE);
        db.execSQL(utilidades.CREAR_TABLA_PARALELO);
        db.execSQL(utilidades.CREAR_TABLA_TAREA);
        db.execSQL(utilidades.CREAR_TABLA_EVALUACION);
        db.execSQL(utilidades.CREAR_TABLA_CUESTIONARIO);
        db.execSQL(utilidades.CREAR_TABLA_HORARIOACADEMICO);
        db.execSQL(utilidades.CREAR_TABLA_PERIODO);
        db.execSQL(utilidades.CREAR_TABLA_PARALELO_DOCENTE);
        db.execSQL(utilidades.CREAR_TABLA_PARALELO_TAREA);
        db.execSQL(utilidades.CREAR_TABLA_PARALELO_EVALUACION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_ASIGNATURA);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_DOCENTE);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_PARALELO);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_TAREA);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_EVALUACION);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_CUESTIONARIO);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_HORARIO_ACADEMICO);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_PERIODO);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_PARALELO_DOCENTE);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_PARALELO_TAREA);
        db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_PARALELO_EVALUACION);

        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Habilita la restriccion de Actualizar y eliminar en cascada
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
