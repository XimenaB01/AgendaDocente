package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Entidades.TareaAsignada;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class OperacionesTarea {

    private Context context;

    public OperacionesTarea(Context context) {
        this.context = context;
    }
    private OperacionesParalelo operacionesParalelo = new OperacionesParalelo(context);

    private ConexionSQLiteHelper conexionDB;

    public long InsertarTar(Tarea tarea, String NomPar, Integer IdAsig){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_TAR,tarea.getNombreTarea());
        contentValues.put(utilidades.CAMPO_DES_TAR,tarea.getDescripcionTarea());
        contentValues.put(utilidades.CAMPO_FEC_TAR,tarea.getFechaTarea());
        contentValues.put(utilidades.CAMPO_OBS_TAR,tarea.getObservacionTarea());
        contentValues.put(utilidades.CAMPO_EST_TAR,tarea.getEstadoTarea());

        try {
            operacion = db.insert(utilidades.TABLA_TAREA, utilidades.CAMPO_ID_TAR,contentValues);
            Integer Id = (int)(operacion);
            operacionesParalelo.CrearTareasAsignadas(NomPar,IdAsig,Id);
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

        try{
            operacion = db.update(utilidades.TABLA_TAREA,contentValues,
                    utilidades.CAMPO_ID_TAR + " = ? ",
                    new String[]{String.valueOf(tarea.getId_tarea())});
        } catch (SQLiteException e){
            Toast.makeText(context, "La Tarea ya existe!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<TareaAsignada> ListarTareas(){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT T."+utilidades.CAMPO_ID_TAR +", T."+utilidades.CAMPO_NOM_TAR + ", PT."+utilidades.CAMPO_PARALELO_NOM_FK2 + ", PT."+utilidades.CAMPO_PARALELO_ASIG_FK2
                + " FROM " + utilidades.TABLA_TAREA + " as T LEFT JOIN " + utilidades.TABLA_PARALELO_TAREA + " as PT ON T."+utilidades.CAMPO_ID_TAR
                + " = PT."+utilidades.CAMPO_TAREA_ID_FK;


        List<TareaAsignada> listaTareas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.rawQuery(query,null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        int idTar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_TAR));
                        String nomTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_TAR));

                        boolean estado = false;

                        int idAsig = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ASIG_FK2));
                        String nomPar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_NOM_FK2));
                        if ( idAsig > 0){
                            estado = true;
                        }
                        TareaAsignada TarAsig = new TareaAsignada(idTar,nomTar,null,null,null, null, estado, idAsig,nomPar);
                        listaTareas.add(TarAsig);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e)
        {
            Log.e("",e.getMessage()+"");
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaTareas;
    }


    public List<TareaAsignada> ListarTareasPorParalelo(long IdTar){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT T."+utilidades.CAMPO_ID_TAR +", T."+utilidades.CAMPO_NOM_TAR + ", T."+utilidades.CAMPO_FEC_TAR
                + ", T."+utilidades.CAMPO_DES_TAR + ", T."+utilidades.CAMPO_OBS_TAR + ", T."+utilidades.CAMPO_EST_TAR
                + ", PT."+utilidades.CAMPO_PARALELO_NOM_FK2 + ", PT."+utilidades.CAMPO_PARALELO_ASIG_FK2
                + " FROM " + utilidades.TABLA_TAREA + " as T LEFT JOIN " + utilidades.TABLA_PARALELO_TAREA + " as PT ON T."+utilidades.CAMPO_ID_TAR
                + " = PT."+utilidades.CAMPO_TAREA_ID_FK + " WHERE T."+utilidades.CAMPO_ID_TAR + " = ?" ;

        List<TareaAsignada> listaTareas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.rawQuery(query,new String[]{String.valueOf(IdTar)});
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        int idTar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_TAR));
                        String nomTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_TAR));
                        String fechTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_FEC_TAR));
                        String descTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_DES_TAR));
                        String obsTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_OBS_TAR));
                        String estaTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_EST_TAR));

                        boolean estado = false;

                        int idAsig = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ASIG_FK2));
                        String nomPar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_NOM_FK2));
                        if ( idAsig > 0){
                            estado = true;
                        }
                        TareaAsignada TarAsig = new TareaAsignada(idTar,nomTar,descTar,fechTar,obsTar, estaTar, estado, idAsig,nomPar);
                        listaTareas.add(TarAsig);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e)
        {
            Log.e("",e.getMessage()+"");
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaTareas;
    }

}
