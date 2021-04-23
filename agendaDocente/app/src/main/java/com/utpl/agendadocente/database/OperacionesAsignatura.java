package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesAsignatura implements OperacionesInterfaz.OperacionAsignatura{

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesAsignatura(Context context) {
        this.context = context;
    }

    public long insertarAsignatura(Asignatura asignatura){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        ContentValues values = contentValues(asignatura);

        try {
            operacion = db.insertOrThrow(Utilidades.TABLA_ASIGNATURA, null,values);
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
            Toast.makeText(context, "Ya existe ese nombre de Asignatura!!", Toast.LENGTH_LONG).show();
        }finally{
            db.close();
        }
        return operacion;
    }

    public List<Asignatura> listarAsignatura(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Asignatura> listaAsig = new ArrayList<>();
        Cursor cursor = null;

        try {

            cursor =  db.query(Utilidades.TABLA_ASIGNATURA,null, null, null, null, null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Asignatura asig = getAsignaturaForCursor(cursor);
                    listaAsig.add(asig);
                }while (cursor.moveToNext());
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

        Asignatura asig = null;
        Cursor cursor = null;

        try {
            cursor = db.query(Utilidades.TABLA_ASIGNATURA, null,
                    Utilidades.CAMPO_ID_ASI + parametro,new String[]{String.valueOf(idAsignatura)},
                    null,null,null);

            if (cursor.moveToFirst()){
                asig = getAsignaturaForCursor(cursor);
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

    public void modificarAsignatura(Asignatura asignatura){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues values = contentValues(asignatura);

        try{
            db.update(Utilidades.TABLA_ASIGNATURA,values,
                    Utilidades.CAMPO_ID_ASI + parametro,
                    new String[]{String.valueOf(asignatura.getIdAsignatura())});
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Asignatura!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }

    }

    public long eliminarAsignatura(long codigo){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        try {
            operacion = db.delete(Utilidades.TABLA_ASIGNATURA,
                    Utilidades.CAMPO_ID_ASI + parametro
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

        contentValues.put(Utilidades.CAMPO_NOM_ASI,asignatura.getNombreAsignatura());
        contentValues.put(Utilidades.CAMPO_AREA,asignatura.getArea());
        contentValues.put(Utilidades.CAMPO_CREDITOS,asignatura.getCreditos());
        contentValues.put(Utilidades.CAMPO_HORARIO,asignatura.getHorario());
        contentValues.put(Utilidades.CAMPO_CARRERA,asignatura.getCarrera());

        return contentValues;
    }

    private Asignatura getAsignaturaForCursor(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_ASI));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_ASI));
        String creditos = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_CREDITOS));
        String horas = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HORARIO));
        String carrera = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_CARRERA));
        String area = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_AREA));
        return new Asignatura(id, nombre,area,creditos,carrera,horas);
    }

}
