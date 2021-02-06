package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.utpl.agendadocente.Model.Cuestionario;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class OperacionesCuestionario {

    private Context context;

    public OperacionesCuestionario(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB ;

    public long InsertarCuest(Cuestionario cuestionario){
        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_CUES,cuestionario.getNombreCuestionario());
        contentValues.put(utilidades.CAMPO_PRE,cuestionario.getPreguntas());

        try {
            operacion = db.insertOrThrow(utilidades.TABLA_CUESTIONARIO, utilidades.CAMPO_ID_CUES,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Cuestionario!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }


    public List<Cuestionario> ListarCuest(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Cuestionario> cuestionarioList = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_CUESTIONARIO,null, null, null, null, null, null, null);
            if (cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        Cuestionario cues = new Cuestionario();
                        cues.setId_cuestionario(cursor.getInt(0));
                        cues.setNombreCuestionario(cursor.getString(1));
                        cues.setPreguntas(cursor.getString(2));

                        cuestionarioList.add(cues);
                    }while (cursor.moveToNext());

                }
            }
        }catch (Exception e)
        {
            Toast.makeText(context, "Operación Fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return cuestionarioList;
    }

    public Cuestionario obtenerCuestionario(long idCuestionario){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Cursor cursor = null;
        Cuestionario cuestionario = new Cuestionario();

        try{
            cursor = db.query(utilidades.TABLA_CUESTIONARIO, null,
                    utilidades.CAMPO_ID_CUES + " = ? ",new String[]{String.valueOf(idCuestionario)},
                    null, null, null);

            if (cursor.moveToFirst()){
                cuestionario.setId_cuestionario(cursor.getInt(0));
                cuestionario.setNombreCuestionario(cursor.getString(1));
                cuestionario.setPreguntas(cursor.getString(2));
            }
        }catch (Exception e){
            Toast.makeText(context, "Operación Fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }

        return cuestionario;
    }

    public long ModificarCuestio(Cuestionario cuestionario){

        long operacion = 0;
        
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_CUES,cuestionario.getNombreCuestionario());
        contentValues.put(utilidades.CAMPO_PRE,cuestionario.getPreguntas());
        try{
            operacion = db.update(utilidades.TABLA_CUESTIONARIO,contentValues,
                    utilidades.CAMPO_ID_CUES + " = ? ",
                    new String[]{String.valueOf(cuestionario.getId_cuestionario())});
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Cuestionario!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;

    }

    public long eliminarCues (long codigo){
        long operacion = 0;
        
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(utilidades.TABLA_CUESTIONARIO,
                    utilidades.CAMPO_ID_CUES + " = ? "
                    ,new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }
}