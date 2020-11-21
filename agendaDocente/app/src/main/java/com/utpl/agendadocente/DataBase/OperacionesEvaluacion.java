package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesEvaluacion {

	private Context context;

    public OperacionesEvaluacion(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

     public long InsertarEva(Evaluacion evaluacion){
        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_EVA,evaluacion.getNombreEvaluacion());
        contentValues.put(utilidades.CAMPO_TIPO,evaluacion.getTipo());
        contentValues.put(utilidades.CAMPO_FEC_EVA,evaluacion.getFechaEvaluacion());
        contentValues.put(utilidades.CAMPO_BIM_EVA,evaluacion.getBimestre());
        contentValues.put(utilidades.CAMPO_OBS_EVA,evaluacion.getObservacion());
        contentValues.put(utilidades.CAMPO_CUES_ID,evaluacion.getCuestionarioID());
        contentValues.put(utilidades.CAMPO_PARALELO_ID_FK2,evaluacion.getParaleloID());

        try {
            operacion = db.insert(utilidades.TABLA_EVALUACION, utilidades.CAMPO_ID_EVA,contentValues);
        }catch (SQLiteException e) {
            Toast.makeText(context, "Ya existe ese nombre de Evaluación!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Evaluacion> ListarEva(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Evaluacion> listaEva = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_EVALUACION, null, null, null, null, null, null, null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Evaluacion eva = new Evaluacion();
                        eva.setId_evaluacion(cursor.getInt(0));
                        eva.setNombreEvaluacion(cursor.getString(1));
                        eva.setTipo(cursor.getString(2));
                        eva.setFechaEvaluacion(cursor.getString(3));
                        eva.setBimestre(cursor.getString(4));
                        eva.setObservacion(cursor.getString(5));
                        eva.setCuestionarioID(cursor.getInt(6));

                        listaEva.add(eva);
                    }while (cursor.moveToNext());
                }
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
        return listaEva;
    }

    public Evaluacion obtenerEva(long idEva){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Evaluacion eva = new Evaluacion();
        Cursor cursor = null;
        try {
            cursor = db.query(utilidades.TABLA_EVALUACION, null,
                    utilidades.CAMPO_ID_EVA + " = ? ",new String[]{String.valueOf(idEva)},
                    null,null,null);
            if (cursor.moveToFirst()){
                eva.setId_evaluacion(cursor.getInt(0));
                eva.setNombreEvaluacion(cursor.getString(1));
                eva.setTipo(cursor.getString(2));
                eva.setFechaEvaluacion(cursor.getString(3));
                eva.setBimestre(cursor.getString(4));
                eva.setObservacion(cursor.getString(5));
                eva.setCuestionarioID(cursor.getInt(6));
                eva.setParaleloID(cursor.getInt(7));
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return eva;
    }

    public long eliminarEva (long codigo){
        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(utilidades.TABLA_EVALUACION,
                    utilidades.CAMPO_ID_EVA + " = ? ",
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarEva (Evaluacion evaluacion){

        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_EVA,evaluacion.getNombreEvaluacion());
        contentValues.put(utilidades.CAMPO_TIPO,evaluacion.getTipo());
        contentValues.put(utilidades.CAMPO_FEC_EVA,evaluacion.getFechaEvaluacion());
        contentValues.put(utilidades.CAMPO_BIM_EVA,evaluacion.getBimestre());
        contentValues.put(utilidades.CAMPO_OBS_EVA,evaluacion.getObservacion());
        contentValues.put(utilidades.CAMPO_CUES_ID,evaluacion.getCuestionarioID());
        contentValues.put(utilidades.CAMPO_PARALELO_ID_FK2, evaluacion.getParaleloID());

        try{
            operacion = db.update(utilidades.TABLA_EVALUACION,contentValues,
                    utilidades.CAMPO_ID_EVA + " = ? ",
                    new String[]{String.valueOf(evaluacion.getId_evaluacion())});
        } catch (SQLiteException e){
            Toast.makeText(context, "Ya existe ese nombre de Evaluación!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }

    public List<Evaluacion> obtenerEvaluacionesId(long IdParalelo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Evaluacion> list = new ArrayList<>();
        String query = "SELECT * FROM " + utilidades.TABLA_EVALUACION + " WHERE " + utilidades.CAMPO_PARALELO_ID_FK2 + " = " + IdParalelo;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_EVA));
                    String evaNom = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_EVA));
                    String evaTip = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_TIPO));
                    String evaFec = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_FEC_EVA));
                    String evaObs = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_OBS_EVA));
                    String evaBim = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_BIM_EVA));
                    Integer evaIdCues = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_CUES_ID));
                    Integer evaIdPar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ID_FK2));

                    Evaluacion evaluacion = new Evaluacion(id, evaNom, evaTip, evaBim, evaFec, evaObs, evaIdPar, evaIdCues);
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
}
