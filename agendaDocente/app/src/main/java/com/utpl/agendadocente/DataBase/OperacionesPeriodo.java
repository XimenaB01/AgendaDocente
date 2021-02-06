package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Model.PeriodoAcademico;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesPeriodo {

    private Context context;

    public OperacionesPeriodo(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public long InsertarPer(PeriodoAcademico periodo){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_FECH_INI,periodo.getFechaInicio());
        contentValues.put(utilidades.CAMPO_FECH_FIN,periodo.getFechaFin());

        try {
            operacion = db.insert(utilidades.TABLA_PERIODO, utilidades.CAMPO_ID_PER,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Inserción fallida: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<PeriodoAcademico> ListarPer(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<PeriodoAcademico> listaPer = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_PERIODO,null, null, null, null, null, null, null);
            if(cursor!=null){
                if (cursor.moveToFirst()){

                    do {
                        PeriodoAcademico per = new PeriodoAcademico();
                        per.setId_periodo(cursor.getInt(0));
                        per.setFechaInicio((cursor.getString(1)));
                        per.setFechaFin(cursor.getString(2));

                        listaPer.add(per);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaPer;
    }

    public PeriodoAcademico obtenerPer(long idPeriodo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        PeriodoAcademico per = new PeriodoAcademico();
        Cursor cursor = null;
        try {
            cursor = db.query(utilidades.TABLA_PERIODO, null,
                    utilidades.CAMPO_ID_PER + " = ? ",new String[]{String.valueOf(idPeriodo)},
                    null,null,null);
            if (cursor.moveToFirst()){
                per.setId_periodo(cursor.getInt(0));
                per.setFechaInicio(cursor.getString(1));
                per.setFechaFin(cursor.getString(2));
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return per;
    }

    public long eliminarPer (long codigo){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(utilidades.TABLA_PERIODO,
                    utilidades.CAMPO_ID_PER + " = ? ",
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarPer (PeriodoAcademico periodo){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_FECH_INI,periodo.getFechaInicio());
        contentValues.put(utilidades.CAMPO_FECH_FIN,periodo.getFechaFin());

        try{
            operacion = db.update(utilidades.TABLA_PERIODO,contentValues,
                    utilidades.CAMPO_ID_PER + " = ? ",
                    new String[]{String.valueOf(periodo.getId_periodo())});
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }


    public boolean PeriodoRepetido(String FechaI, String FechaF){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        boolean operacion = false;

        String query = "SELECT * FROM " + utilidades.TABLA_PERIODO + " WHERE "
                + utilidades.CAMPO_FECH_INI + " = '" + FechaI + "' AND "
                + utilidades.CAMPO_FECH_FIN + " = '" + FechaF + "'";

        Cursor cursor = db.rawQuery(query,null);
        try {
            while (cursor.moveToNext()){
                String fechaI = cursor.getString(1);
                String fechaF = cursor.getString( 2);
                if (FechaI.equals(fechaI) && FechaF.equals(fechaF)){
                    operacion = true;
                }
            }
        }catch (SQLiteException e){
            Log.e("e", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.close();
        }
        return operacion;
    }
}
