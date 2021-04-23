package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Evaluacion;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesEvaluacion implements OperacionesInterfaz.OperacionEvaluacion {

	private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesEvaluacion(Context context) {
        this.context = context;
    }

     public long insertarEvaluacion(Evaluacion evaluacion){
        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForEvaluacion(evaluacion);
        try {
            operacion = db.insert(Utilidades.TABLA_EVALUACION, Utilidades.CAMPO_ID_EVA,contentValues);
        }catch (SQLiteException e) {
            Toast.makeText(context, "Ya existe ese nombre de Evaluación!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Evaluacion> listarEvaluacion(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Evaluacion> listaEvaluacion = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_EVALUACION, null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                    do{
                        Evaluacion eva = getEvaluacionForCursor(cursor);
                        listaEvaluacion.add(eva);
                    }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaEvaluacion;
    }

    public Evaluacion obtenerEvaluacion(long idEva){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Evaluacion evaluacion = null;
        Cursor cursor = null;
        try {
            cursor = db.query(Utilidades.TABLA_EVALUACION, null,
                    Utilidades.CAMPO_ID_EVA + parametro,new String[]{String.valueOf(idEva)},
                    null,null,null);
            if (cursor.moveToFirst()){
                evaluacion = getEvaluacionForCursor(cursor);
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return evaluacion;
    }

    public long eliminarEvaluacion(long codigo){
        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(Utilidades.TABLA_EVALUACION,
                    Utilidades.CAMPO_ID_EVA + parametro,
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarEvaluacion(Evaluacion evaluacion){

        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForEvaluacion(evaluacion);

        try{
            operacion = db.update(Utilidades.TABLA_EVALUACION,contentValues,
                    Utilidades.CAMPO_ID_EVA + " = ? ",
                    new String[]{String.valueOf(evaluacion.getIdEvaluacion())});
        } catch (SQLiteException e){
            Toast.makeText(context, "Ya existe ese nombre de Evaluación!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }

    public List<Evaluacion> obtenerEvaluacionesId(long idParalelo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Evaluacion> list = new ArrayList<>();
        String query = "SELECT * FROM " + Utilidades.TABLA_EVALUACION + " WHERE " + Utilidades.CAMPO_PARALELO_ID_FK2 + " = " + idParalelo;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    Evaluacion evaluacion = getEvaluacionForCursor(cursor);
                    list.add(evaluacion);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("EEvaId", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }

        return list;
    }

    private ContentValues getContentValuesForEvaluacion(Evaluacion evaluacion){

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_NOM_EVA,evaluacion.getNombreEvaluacion());
        contentValues.put(Utilidades.CAMPO_TIPO_EVA,evaluacion.getTipo());
        contentValues.put(Utilidades.CAMPO_FEC_EVA,evaluacion.getFechaEvaluacion());
        contentValues.put(Utilidades.CAMPO_BIM_EVA,evaluacion.getBimestre());
        contentValues.put(Utilidades.CAMPO_OBS_EVA,evaluacion.getObservacion());
        contentValues.put(Utilidades.CAMPO_CUES_ID,evaluacion.getCuestionarioID());
        contentValues.put(Utilidades.CAMPO_PARALELO_ID_FK2, evaluacion.getParaleloID());

        return contentValues;
    }

    private Evaluacion getEvaluacionForCursor(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_EVA));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_EVA));
        String tipo = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_TIPO_EVA));
        String fecha = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FEC_EVA));
        String bimestre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_BIM_EVA));
        String observacion = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_OBS_EVA));
        Integer idCuestionario = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_CUES_ID));
        Integer idParalelo = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_PARALELO_ID_FK2));
        return new Evaluacion(id, nombre, tipo, bimestre, fecha, observacion,idParalelo,idCuestionario);
    }
}
