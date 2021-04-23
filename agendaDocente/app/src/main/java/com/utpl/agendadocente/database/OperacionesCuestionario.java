package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Cuestionario;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class OperacionesCuestionario implements OperacionesInterfaz.OperacionCuestionario{

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB ;

    public OperacionesCuestionario(Context context) {
        this.context = context;
    }

    public long insertarCuestionario(Cuestionario cuestionario){
        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCuestionario(cuestionario);

        try {
            operacion = db.insertOrThrow(Utilidades.TABLA_CUESTIONARIO, Utilidades.CAMPO_ID_CUES,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Cuestionario!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }


    public List<Cuestionario> listarCuestionario(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Cuestionario> cuestionarioList = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_CUESTIONARIO,null, null, null, null, null, null, null);
            if (cursor!=null && cursor.moveToFirst()){
                do {
                    Cuestionario cues = getCuestionarioForCursor(cursor);
                    cuestionarioList.add(cues);
                }while (cursor.moveToNext());
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
        Cuestionario cuestionario = null;

        try{
            cursor = db.query(Utilidades.TABLA_CUESTIONARIO, null,
                    Utilidades.CAMPO_ID_CUES + parametro,new String[]{String.valueOf(idCuestionario)},
                    null, null, null);

            if (cursor.moveToFirst()){
                cuestionario = getCuestionarioForCursor(cursor);
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

    public long modificarCuestionario(Cuestionario cuestionario){

        long operacion = 0;
        
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForCuestionario(cuestionario);
        try{
            operacion = db.update(Utilidades.TABLA_CUESTIONARIO,contentValues,
                    Utilidades.CAMPO_ID_CUES + parametro,
                    new String[]{String.valueOf(cuestionario.getIdCuestionario())});
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Ya existe ese nombre de Cuestionario!!", Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;

    }

    public long eliminarCuestionario(long codigo){
        long operacion = 0;
        
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(Utilidades.TABLA_CUESTIONARIO,
                    Utilidades.CAMPO_ID_CUES + parametro
                    ,new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    private ContentValues getContentValuesForCuestionario(Cuestionario cuestionario){

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_NOM_CUES,cuestionario.getNombreCuestionario());
        contentValues.put(Utilidades.CAMPO_PRE,cuestionario.getPreguntas());

        return contentValues;
    }

    private Cuestionario getCuestionarioForCursor(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_CUES));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_CUES));
        String preguntas = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_PRE));
        return new Cuestionario(id,nombre,preguntas);
    }
}