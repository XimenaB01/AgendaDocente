package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Model.Tarea;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesTarea {

    private Context context;

    public OperacionesTarea(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public long InsertarTar(Tarea tarea){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_TAR,tarea.getNombreTarea());
        contentValues.put(utilidades.CAMPO_DES_TAR,tarea.getDescripcionTarea());
        contentValues.put(utilidades.CAMPO_FEC_TAR,tarea.getFechaTarea());
        contentValues.put(utilidades.CAMPO_OBS_TAR,tarea.getObservacionTarea());
        contentValues.put(utilidades.CAMPO_EST_TAR,tarea.getEstadoTarea());
        contentValues.put(utilidades.CAMPO_PARALELO_ID_FK1,tarea.getParaleloId());

        try {
            operacion = db.insert(utilidades.TABLA_TAREA, utilidades.CAMPO_ID_TAR,contentValues);
        }catch (SQLiteException e) {
            Toast.makeText(context, "La Tarea ya existe!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Tarea> ListarTar(){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Tarea> listaTar = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_TAREA,null, null, null, null, null, null, null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Tarea tar = new Tarea();
                        tar.setId_tarea(cursor.getInt(0));
                        tar.setNombreTarea(cursor.getString(1));
                        tar.setDescripcionTarea(cursor.getString(2));
                        tar.setFechaTarea(cursor.getString(3));
                        tar.setObservacionTarea(cursor.getString(4));
                        tar.setEstadoTarea(cursor.getString(5));

                        listaTar.add(tar);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaTar;
    }

    public List<Tarea> obtenerTareasId(long IdParalelo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Tarea> list = new ArrayList<>();
        String query = "SELECT * FROM " + utilidades.TABLA_TAREA + " WHERE " + utilidades.CAMPO_PARALELO_ID_FK1 + " = " + IdParalelo;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_TAR));
                    String tarNom = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_TAR));
                    String tarDes = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_DES_TAR));
                    String tarFec = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_FEC_TAR));
                    String tarObs = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_OBS_TAR));
                    String tarEst = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_EST_TAR));
                    Integer tarIdPar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ID_FK1));

                    Tarea tarea = new Tarea(id, tarNom, tarDes, tarFec, tarObs, tarEst, tarIdPar);
                    list.add(tarea);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.e("ETarId", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }

        return list;
    }

    public Tarea obtenerTar(long idTarea){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Tarea tar = new Tarea();
        Cursor cursor = null;
        try{
            cursor = db.query(utilidades.TABLA_TAREA, null,
                    utilidades.CAMPO_ID_TAR + " = ? ",new String[]{String.valueOf(idTarea)},
                    null,null,null);
            if (cursor.moveToFirst()){
                tar.setId_tarea(cursor.getInt(0));
                tar.setNombreTarea(cursor.getString(1));
                tar.setDescripcionTarea(cursor.getString(2));
                tar.setFechaTarea(cursor.getString(3));
                tar.setObservacionTarea(cursor.getString(4));
                tar.setEstadoTarea(cursor.getString(5));
                tar.setParaleloId(cursor.getInt(6));
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return tar;
    }

    public long eliminarTar (long codigo){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(utilidades.TABLA_TAREA,
                    utilidades.CAMPO_ID_TAR + " = ? ",
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarTar (Tarea tarea){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_TAR,tarea.getNombreTarea());
        contentValues.put(utilidades.CAMPO_DES_TAR,tarea.getDescripcionTarea());
        contentValues.put(utilidades.CAMPO_FEC_TAR,tarea.getFechaTarea());
        contentValues.put(utilidades.CAMPO_OBS_TAR,tarea.getObservacionTarea());
        contentValues.put(utilidades.CAMPO_EST_TAR,tarea.getEstadoTarea());
        contentValues.put(utilidades.CAMPO_PARALELO_ID_FK1,tarea.getParaleloId());

        try{
            operacion = db.update(utilidades.TABLA_TAREA,contentValues,
                    utilidades.CAMPO_ID_TAR + " = ? ",
                    new String[]{String.valueOf(tarea.getId_tarea())});

        } catch (SQLiteException e){
            Log.e("error",e+"");
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }
}
