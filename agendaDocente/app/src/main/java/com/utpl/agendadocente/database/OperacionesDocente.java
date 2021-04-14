package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class OperacionesDocente {

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesDocente(Context context) {
        this.context = context;
    }

    public long insertarDocente(Docente docente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion= 0;

        ContentValues contentValues = getContentValuesForDocente(docente);
        try {
            operacion = db.insertOrThrow(Utilidades.TABLA_DOCENTE, null,contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Ya existe ese Nº de Cédula!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Docente> listarDocente(){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Docente> listaDocente = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor =  db.query(Utilidades.TABLA_DOCENTE, null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Docente doc = getDocenteForCursor(cursor);
                    listaDocente.add(doc);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e)
        {
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return listaDocente;
    }

    public Docente obtenerdocente(long idDocente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Docente docente = null;
        Cursor cursor = null;
        try {
            cursor = db.query(Utilidades.TABLA_DOCENTE, null,
                    Utilidades.CAMPO_ID_DOC + parametro,new String[]{String.valueOf(idDocente)},
                    null,null,null);
            if (cursor.moveToFirst()){
                docente = getDocenteForCursor(cursor);
            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return docente;
    }

    public long eliminarDocente(long codigo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        try {
            operacion = db.delete(Utilidades.TABLA_DOCENTE,
                    Utilidades.CAMPO_ID_DOC + parametro,
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarDocente(Docente docente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        ContentValues contentValues = getContentValuesForDocente(docente);

        try{
            operacion = db.update(Utilidades.TABLA_DOCENTE,contentValues,
                    Utilidades.CAMPO_ID_DOC + parametro,
                    new String[]{String.valueOf(docente.getIdDocente())});
        } catch (SQLiteException e){
            Toast.makeText(context, "Ya existe ese Nº de Cédula!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }

    private ContentValues getContentValuesForDocente(Docente docente){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Utilidades.CAMPO_NOM_DOC,docente.getNombreDocente());
        contentValues.put(Utilidades.CAMPO_APE_DOC,docente.getApellidoDocente());
        contentValues.put(Utilidades.CAMPO_CEDULA,docente.getCedula());
        contentValues.put(Utilidades.CAMPO_CORREO,docente.getEmail());

        return contentValues;
    }

    private Docente getDocenteForCursor(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_DOC));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_DOC));
        String apellido = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_APE_DOC));
        String cedula = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_CEDULA));
        String correo = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_CORREO));
        return new Docente(id, nombre, apellido, correo, cedula);
    }

}
