package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesHorario {

	private Context context;

    public OperacionesHorario(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

	public long InsertarHor(Horario horario){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_AULA,horario.getAula());
        contentValues.put(utilidades.CAMPO_HOR_ENT,horario.getHora_entrada());
        contentValues.put(utilidades.CAMPO_HOR_SAL,horario.getHora_salida());

        try {
            operacion = db.insert(utilidades.TABLA_HORARIO_ACADEMICO, utilidades.CAMPO_ID_HOR,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Inserción fallida!! ", Toast.LENGTH_LONG).show();
        }finally{
            db.close();
        }
        return operacion;
    }

    public List<Horario> ListarHor(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Horario> listaHor = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_HORARIO_ACADEMICO,null, null, null, null, null, null, null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Horario hor = new Horario();
                        hor.setId_horario(cursor.getInt(0));
                        hor.setAula((cursor.getString(1)));
                        hor.setHora_entrada(cursor.getString(2));
                        hor.setHora_salida(cursor.getString(3));

                        listaHor.add(hor);
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
        return listaHor;
    }

    public Horario obtenerHor(long idHorario){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        Horario hor = new Horario();
        Cursor cursor = null;

        try {
            cursor = db.query(utilidades.TABLA_HORARIO_ACADEMICO, null,
                    utilidades.CAMPO_ID_HOR + " = ?",new String[]{String.valueOf(idHorario)},
                    null,null,null);
            if (cursor.moveToFirst()){
                hor.setId_horario(cursor.getInt(0));
                hor.setAula(cursor.getString(1));
                hor.setHora_entrada(cursor.getString(2));
                hor.setHora_salida(cursor.getString(3));
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return hor;
    }

    public long eliminarHor (long codigo){
        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(utilidades.TABLA_HORARIO_ACADEMICO,
                    utilidades.CAMPO_ID_HOR + " = ? ",
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarHor (Horario horario){

        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_AULA,horario.getAula());
        contentValues.put(utilidades.CAMPO_HOR_ENT,horario.getHora_entrada());
        contentValues.put(utilidades.CAMPO_HOR_SAL,horario.getHora_salida());

        try{
            operacion = db.update(utilidades.TABLA_HORARIO_ACADEMICO,contentValues,
                    utilidades.CAMPO_ID_HOR + " = ? ",
                    new String[]{String.valueOf(horario.getId_horario())});
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }

    public boolean HorarioRepetido(String aula, String Hor1, String Hor2){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        boolean operacion = false;

        String query = "SELECT * FROM " + utilidades.TABLA_HORARIO_ACADEMICO + " WHERE "
                + utilidades.CAMPO_AULA + " = '" + aula + "' AND "
                + utilidades.CAMPO_HOR_ENT + " = '" + Hor1 + "' AND "
                + utilidades.CAMPO_HOR_SAL + " = '" + Hor2 + "'";

        Cursor cursor =db.rawQuery(query,null);
        try {
            while (cursor.moveToNext()){
                String a = cursor.getString(1);
                String h1 = cursor.getString(2);
                String h2 = cursor.getString( 3);

                if (aula.equals(a) && Hor1.equals(h1) && Hor2.equals(h2)){
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
