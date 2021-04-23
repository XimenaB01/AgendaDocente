package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesPeriodo implements OperacionesInterfaz.OperacionPeriodo{

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesPeriodo(Context context) {
        this.context = context;
    }

    public long insertarPeriodo(PeriodoAcademico periodo){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValues(periodo);

        try {
            operacion = db.insert(Utilidades.TABLA_PERIODO, Utilidades.CAMPO_ID_PER,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Inserción fallida: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<PeriodoAcademico> listarPeriodo(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<PeriodoAcademico> listaPer = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_PERIODO,null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    PeriodoAcademico per = getPeriodoAcademicoForCursor(cursor);

                    listaPer.add(per);
                }while (cursor.moveToNext());
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

    public PeriodoAcademico obtenerPeriodo(long idPeriodo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        PeriodoAcademico periodo = null;
        Cursor cursor = null;
        try {
            cursor = db.query(Utilidades.TABLA_PERIODO, null,
                    Utilidades.CAMPO_ID_PER + parametro,new String[]{String.valueOf(idPeriodo)},
                    null,null,null);
            if (cursor.moveToFirst()){
                periodo = getPeriodoAcademicoForCursor(cursor);
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return periodo;
    }

    public long eliminarPeriodo(long codigo){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(Utilidades.TABLA_PERIODO,
                    Utilidades.CAMPO_ID_PER + parametro,
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarPeriodo(PeriodoAcademico periodo){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValues(periodo);

        try{
            operacion = db.update(Utilidades.TABLA_PERIODO,contentValues,
                    Utilidades.CAMPO_ID_PER + parametro,
                    new String[]{String.valueOf(periodo.getIdPeriodo())});
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }


    public boolean periodoRepetido(String fechaInicio, String fechaFin){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        boolean operacion = false;

        String query = "SELECT * FROM " + Utilidades.TABLA_PERIODO + " WHERE "
                + Utilidades.CAMPO_FECH_INI + " = '" + fechaInicio + "' AND "
                + Utilidades.CAMPO_FECH_FIN + " = '" + fechaFin + "'";

        Cursor cursor = db.rawQuery(query,null);
        try {
            while (cursor.moveToNext()){
                String fechaI = cursor.getString(1);
                String fechaF = cursor.getString( 2);
                if (fechaInicio.equals(fechaI) && fechaFin.equals(fechaF)){
                    operacion = true;
                }
            }
        }catch (SQLiteException e){
            Log.e("e", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor.getCount()>0){
                cursor.close();
            }
            db.close();
        }
        return !operacion;
    }

    private ContentValues getContentValues(PeriodoAcademico periodo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_FECH_INI,periodo.getFechaInicio());
        contentValues.put(Utilidades.CAMPO_FECH_FIN,periodo.getFechaFin());
        return contentValues;
    }

    private PeriodoAcademico getPeriodoAcademicoForCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_PER));
        String fechaInicio = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FECH_INI));
        String fechaFin = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_FECH_FIN));
        return new PeriodoAcademico(id,fechaInicio,fechaFin);
    }
}
