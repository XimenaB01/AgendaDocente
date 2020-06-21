package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class OperacionesDocente {

    private Context context;

    public OperacionesDocente(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public long InsertarDocente(Docente docente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion= 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_DOC,docente.getNombreDocente());
        contentValues.put(utilidades.CAMPO_APE_DOC,docente.getApellidoDocente());
        contentValues.put(utilidades.CAMPO_CEDULA,docente.getCedula());
        contentValues.put(utilidades.CAMPO_CORREO,docente.getEmail());
        try {
            operacion = db.insertOrThrow(utilidades.TABLA_DOCENTE, null,contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Ya existe ese Nº de Cédula!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;
    }

    public List<Docente> listarDoc(){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Docente> listaDoc = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor =  db.query(utilidades.TABLA_DOCENTE, null, null, null, null, null, null, null);

            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        Docente doc = new Docente();
                        doc.setId_docente(cursor.getInt(0));
                        doc.setNombreDocente(cursor.getString(1));
                        doc.setApellidoDocente(cursor.getString(2));
                        doc.setCedula(cursor.getString(3));
                        doc.setEmail(cursor.getString(4));
                        listaDoc.add(doc);
                    }while (cursor.moveToNext());
                }
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
        return listaDoc;
    }

    public Docente obtenerdocente(long idDocente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Docente doc = null;
        Cursor cursor = null;
        try {
            cursor = db.query(utilidades.TABLA_DOCENTE, null,
                    utilidades.CAMPO_ID_DOC + " = ? ",new String[]{String.valueOf(idDocente)},
                    null,null,null);
            if (cursor.moveToFirst()){

                int id = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_DOC));
                String nombre = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_DOC));
                String apellido = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_APE_DOC));
                String cedula = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_CEDULA));
                String correo = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_CORREO));

                doc = new Docente(id, nombre, apellido, correo, cedula);

            }
        }catch (Exception e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return doc;
    }

    public long eliminarDoc (long codigo){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        try {
            operacion = db.delete(utilidades.TABLA_DOCENTE,
                    utilidades.CAMPO_ID_DOC + " = ? ",
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarDoc (Docente docente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_DOC,docente.getNombreDocente());
        contentValues.put(utilidades.CAMPO_APE_DOC,docente.getApellidoDocente());
        contentValues.put(utilidades.CAMPO_CEDULA,docente.getCedula());
        contentValues.put(utilidades.CAMPO_CORREO,docente.getEmail());

        try{
            operacion = db.update(utilidades.TABLA_DOCENTE,contentValues,
                    utilidades.CAMPO_ID_DOC + " = ? ",
                    new String[]{String.valueOf(docente.getId_docente())});
        } catch (SQLiteException e){
            Toast.makeText(context, "Ya existe ese Nº de Cédula!!", Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }
}
