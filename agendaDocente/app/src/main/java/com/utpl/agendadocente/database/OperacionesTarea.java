package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Tarea;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesTarea implements OperacionesInterfaz.OperacionTarea{

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesTarea(Context context) {
        this.context = context;
    }

    public long insertarTarea(Tarea tarea){

        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForTarea(tarea);

        try {
            operacion = db.insert(Utilidades.TABLA_TAREA, Utilidades.CAMPO_ID_TAR,contentValues);
        }catch (SQLiteException e) {
            Toast.makeText(context, "La Tarea ya existe!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Tarea> listarTarea(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Tarea> listaTarea = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_TAREA,null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Tarea tar = getTareaForCursor(cursor);
                    listaTarea.add(tar);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaTarea;
    }

    public List<Tarea> obtenerTareasId(long idParalelo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Tarea> listaTarea = new ArrayList<>();
        String query = "SELECT * FROM " + Utilidades.TABLA_TAREA + " WHERE " + Utilidades.CAMPO_PARALELO_ID_FK1 + " = " + idParalelo;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    Tarea tarea = getTareaForCursor(cursor);
                    listaTarea.add(tarea);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("ETarId", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }

        return listaTarea;
    }

    public Tarea obtenerTarea(long idTarea){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Tarea tarea = null;
        Cursor cursor = null;
        try{
            cursor = db.query(Utilidades.TABLA_TAREA, null,
                    Utilidades.CAMPO_ID_TAR + parametro,new String[]{String.valueOf(idTarea)},
                    null,null,null);
            if (cursor.moveToFirst()){
                tarea = getTareaForCursor(cursor);
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return tarea;
    }

    public long eliminarTarea(long codigo){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(Utilidades.TABLA_TAREA,
                    Utilidades.CAMPO_ID_TAR + parametro,
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarTarea(Tarea tarea){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForTarea(tarea);

        try{
            operacion = db.update(Utilidades.TABLA_TAREA,contentValues,
                    Utilidades.CAMPO_ID_TAR + parametro,
                    new String[]{String.valueOf(tarea.getIdTarea())});

        } catch (SQLiteException e){
            Log.e("error",e+"");
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    private ContentValues getContentValuesForTarea(Tarea tarea){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_NOM_TAR,tarea.getNombreTarea());
        contentValues.put(Utilidades.CAMPO_DES_TAR,tarea.getDescripcionTarea());
        contentValues.put(Utilidades.CAMPO_FEC_TAR,tarea.getFechaTarea());
        contentValues.put(Utilidades.CAMPO_OBS_TAR,tarea.getObservacionTarea());
        contentValues.put(Utilidades.CAMPO_EST_TAR,tarea.getEstadoTarea());
        contentValues.put(Utilidades.CAMPO_PARALELO_ID_FK1,tarea.getParaleloId());

        return contentValues;

    }

    private Tarea getTareaForCursor (Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_TAR));
        String tarNom = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_TAR));
        String tarDes = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_DES_TAR));
        String tarFec = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FEC_TAR));
        String tarObs = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_OBS_TAR));
        String tarEst = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_EST_TAR));
        Integer tarIdPar = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_PARALELO_ID_FK1));
        return new Tarea(id, tarNom, tarDes, tarFec, tarObs, tarEst, tarIdPar);
    }
}
