package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Componente;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesComponente implements OperacionesInterfaz.OperacionComponente {

    private Context context;
    private ConexionSQLiteHelper conexionDB;

    public OperacionesComponente(Context context) {
        this.context = context;
    }

    public void insertarComponente(Componente componente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForComponente(componente);

        try {
            db.insertOrThrow(Utilidades.TABLA_COMPONENTES, null,contentValues);
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }finally{
            db.close();
        }

    }

    public List<Componente> obtenerComponentes(Integer idAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Componente> componenteList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor =  db.query(Utilidades.TABLA_COMPONENTES, null, Utilidades.CAMPO_ID_ASIG_FK + " = ?", new String[]{String.valueOf(idAsig)},
                    null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Componente componente = getComponenteForCursor(cursor);
                    componenteList.add(componente);
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

        return componenteList;
    }

    public void actualizarComponente(Componente componente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForComponente(componente);

        try {
            db.update(Utilidades.TABLA_COMPONENTES, contentValues,
                    Utilidades.CAMPO_NOM_COMP + " = ? AND " + Utilidades.CAMPO_ID_ASIG_FK + " = ?",
                    new String[]{String.valueOf(componente.getComponente()), String.valueOf(componente.getIdAsig())});
        }
        catch (SQLiteException e){
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }finally{
            db.close();
        }
    }

    public void eliminarComponente(Componente componente){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        Log.e("a", "aquiiiiiiiiiiiii");

        try {
            db.delete(Utilidades.TABLA_COMPONENTES,
                    Utilidades.CAMPO_NOM_COMP + " = ? AND " + Utilidades.CAMPO_ID_ASIG_FK + " = ? "
                    ,new String[]{String.valueOf(componente.getComponente()),String.valueOf(componente.getIdAsig())});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
    }

    private ContentValues getContentValuesForComponente(Componente componente){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_NOM_COMP,componente.getComponente());
        contentValues.put(Utilidades.CAMPO_VAL_COMP,componente.getValor());
        contentValues.put(Utilidades.CAMPO_ID_ASIG_FK,componente.getIdAsig());
        return contentValues;
    }

    private Componente getComponenteForCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_COMP));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_COMP));
        String valor = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_VAL_COMP));
        int idAsig = cursor.getShort(cursor.getColumnIndex(Utilidades.CAMPO_ID_ASIG_FK));
        return new Componente(id,nombre,valor, idAsig);
    }
}
