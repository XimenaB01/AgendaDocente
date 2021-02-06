package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Model.Asignatura;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesAsignatura {

    private Context context;

    public OperacionesAsignatura(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public long InsertarAsignatura(Asignatura asignatura){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        ContentValues values = contentValues(asignatura);

        try {
            operacion = db.insertOrThrow(utilidades.TABLA_ASIGNATURA, null,values);
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(context, "Ya existe ese nombre de Asignatura!!", Toast.LENGTH_LONG).show();
        }finally{
            db.close();
        }
        return operacion;
    }

    public List<Asignatura> ListarAsig(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Asignatura> listaAsig = new ArrayList<>();
        Cursor cursor = null;

        try {

            cursor =  db.query(utilidades.TABLA_ASIGNATURA,null, null, null, null, null, null, null);

            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Asignatura asig = new Asignatura();
                        asig.setId_asignatura(cursor.getInt(0));
                        asig.setNombreAsignatura(cursor.getString(1));
                        asig.setArea(cursor.getString(2));
                        asig.setCreditos(cursor.getString(3));
                        asig.setHorario(cursor.getString(4));
                        asig.setCarrera(cursor.getString(5));
                        listaAsig.add(asig);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e) {
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return listaAsig;
    }

    public Asignatura obtenerAsignatura(Integer idAsignatura){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Asignatura asig = new Asignatura();
        Cursor cursor = null;

        try {
            cursor = db.query(utilidades.TABLA_ASIGNATURA, null,
                    utilidades.CAMPO_ID_ASI + " = ?",new String[]{String.valueOf(idAsignatura)},
                    null,null,null);

            if (cursor.moveToFirst()){
                asig.setId_asignatura(cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_ASI)));
                asig.setNombreAsignatura(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_ASI)));
                asig.setCreditos(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_CREDITOS)));
                asig.setHorario(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_HORARIO)));
                asig.setCarrera(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_CARRERA)));
                asig.setArea(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_AREA)));
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return asig;
    }

    public long ModificarAsig(Asignatura asignatura){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        ContentValues values = contentValues(asignatura);

        try{
            operacion = db.update(utilidades.TABLA_ASIGNATURA,values,
                    utilidades.CAMPO_ID_ASI + " = ? ",
                    new String[]{String.valueOf(asignatura.getId_asignatura())});
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Asignatura!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;

    }

    public long eliminarAsig (long codigo){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        try {
            operacion = db.delete(utilidades.TABLA_ASIGNATURA,
                    utilidades.CAMPO_ID_ASI + " = ? "
                    ,new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    private ContentValues contentValues(Asignatura asignatura){

        ContentValues contentValues = new ContentValues();

        contentValues.put(utilidades.CAMPO_NOM_ASI,asignatura.getNombreAsignatura());
        contentValues.put(utilidades.CAMPO_AREA,asignatura.getArea());
        contentValues.put(utilidades.CAMPO_CREDITOS,asignatura.getCreditos());
        contentValues.put(utilidades.CAMPO_HORARIO,asignatura.getHorario());
        contentValues.put(utilidades.CAMPO_CARRERA,asignatura.getCarrera());

        return contentValues;
    }

}
