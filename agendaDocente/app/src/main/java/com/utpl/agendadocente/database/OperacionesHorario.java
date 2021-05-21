package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.features.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesHorario implements OperacionesInterfaz.OperacionHorario{

	private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;
    public OperacionesHorario(Context context) {
        this.context = context;
    }

	public long insertarHorario(Horario horario){
        long operacion= 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForHorario(horario);

        try {
            operacion = db.insert(Utilidades.TABLA_HORARIO_ACADEMICO, Utilidades.CAMPO_ID_HOR,contentValues);
        }
        catch (SQLiteException e)
        {
            Toast.makeText(context, "Inserción fallida!! ", Toast.LENGTH_LONG).show();
        }finally{
            db.close();
        }
        return operacion;
    }

    public List<Horario> listarHorario(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        List<Horario> listaHorario = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_HORARIO_ACADEMICO,null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Horario hor = getHorarioForCursor(cursor);
                    listaHorario.add(hor);
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
        return listaHorario;
    }

    public Horario obtenerHorario(long idHorario){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        Horario horario = null;
        Cursor cursor = null;

        try {
            cursor = db.query(Utilidades.TABLA_HORARIO_ACADEMICO, null,
                    Utilidades.CAMPO_ID_HOR + parametro,new String[]{String.valueOf(idHorario)},
                    null,null,null);
            if (cursor.moveToFirst()){
                horario = getHorarioForCursor(cursor);
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.close();
        }
        return horario;
    }

    public long eliminarHorario(long codigo){
        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();
        try {
            operacion = db.delete(Utilidades.TABLA_HORARIO_ACADEMICO,
                    Utilidades.CAMPO_ID_HOR + parametro,
                    new String[]{String.valueOf(codigo)});
        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarHorario(Horario horario){

        long operacion = 0;
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForHorario(horario);

        try{
            operacion = db.update(Utilidades.TABLA_HORARIO_ACADEMICO,contentValues,
                    Utilidades.CAMPO_ID_HOR + parametro,
                    new String[]{String.valueOf(horario.getIdHorario())});
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return operacion;

    }

    public boolean horarioRepetido(Horario horario){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        boolean operacion = false;
        String operadorAnd = "' AND ";

        String query = "SELECT * FROM " + Utilidades.TABLA_HORARIO_ACADEMICO + " WHERE "
                + Utilidades.CAMPO_AULA + " = '" + horario.getAula() + operadorAnd
                + Utilidades.CAMPO_DIA + " = '" + horario.getDia() + operadorAnd
                + Utilidades.CAMPO_HOR_ENT + " = '" + horario.getHoraEntrada() + operadorAnd
                + Utilidades.CAMPO_HOR_SAL + " = '" + horario.getHoraSalida() + "'";

        Cursor cursor =db.rawQuery(query,null);
        try {
            while (cursor.moveToNext()){
                String a = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_AULA));
                String h1 = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HOR_ENT));
                String h2 = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HOR_SAL));

                if (horario.getAula().equals(a) && horario.getHoraEntrada().equals(h1) && horario.getHoraSalida().equals(h2)){
                    operacion = true;
                }
            }
        }catch (SQLiteException e){
            Log.e("e", Objects.requireNonNull(e.getMessage()));
        }finally {
            if ((cursor != null) && (cursor.getCount() > 0)){
                cursor.close();
            }
            db.close();
        }
        return !operacion;
    }

    private ContentValues getContentValuesForHorario(Horario horario){

        ContentValues contentValues = new ContentValues();

        contentValues.put(Utilidades.CAMPO_AULA,horario.getAula());
        contentValues.put(Utilidades.CAMPO_DIA,horario.getDia());
        contentValues.put(Utilidades.CAMPO_HOR_ENT,horario.getHoraEntrada());
        contentValues.put(Utilidades.CAMPO_HOR_SAL,horario.getHoraSalida());

        return contentValues;
    }

    private Horario getHorarioForCursor(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_HOR));
        String aula = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_AULA));
        String dia = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_DIA));
        String horaEntrada = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HOR_ENT));
        String horaSalida = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_HOR_SAL));
        return new Horario(id,aula,dia,horaEntrada,horaSalida);
    }
}
