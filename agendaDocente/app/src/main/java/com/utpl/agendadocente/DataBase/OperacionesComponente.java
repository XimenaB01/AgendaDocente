package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Model.Componente;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesComponente {

    private Context context;

    public OperacionesComponente(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public void InsertarComponente(Componente componente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_COMP,componente.getComponente());
        contentValues.put(utilidades.CAMPO_VAL_COMP,componente.getValor());
        contentValues.put(utilidades.CAMPO_ID_ASIG_FK,componente.getIdAsig());

        try {
            db.insertOrThrow(utilidades.TABLA_COMPONENTES, null,contentValues);
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }finally{
            db.close();
        }

    }

    public List<Componente> obtenerComponentes(Integer IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Componente> componenteList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor =  db.query(utilidades.TABLA_COMPONENTES, null, utilidades.CAMPO_ID_ASIG_FK + " = ?", new String[]{String.valueOf(IdAsig)},
                    null,null,null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Componente componente = new Componente();
                        componente.setId(cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_COMP)));
                        componente.setComponente(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_COMP)));
                        componente.setValor(cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_VAL_COMP)));
                        componenteList.add(componente);
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

        return componenteList;
    }

    public void ActualizarComponente(Componente componente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_COMP,componente.getComponente());
        contentValues.put(utilidades.CAMPO_VAL_COMP,componente.getValor());
        contentValues.put(utilidades.CAMPO_ID_ASIG_FK,componente.getIdAsig());

        try {
            db.update(utilidades.TABLA_COMPONENTES, contentValues,
                    utilidades.CAMPO_NOM_COMP + " = ? AND " + utilidades.CAMPO_ID_ASIG_FK + " = ?",
                    new String[]{String.valueOf(componente.getComponente()), String.valueOf(componente.getIdAsig())});
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }finally{
            db.close();
        }
    }

    public long EliminarComponente (Componente componente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        long operacion = 0;

        try {
            operacion = db.delete(utilidades.TABLA_COMPONENTES,
                    utilidades.CAMPO_NOM_COMP + " = ? AND " + utilidades.CAMPO_ID_ASIG_FK + " = ? "
                    ,new String[]{String.valueOf(componente.getComponente()),String.valueOf(componente.getIdAsig())});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }


}
