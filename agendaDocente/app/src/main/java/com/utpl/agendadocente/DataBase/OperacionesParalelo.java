package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Model.Docente;
import com.utpl.agendadocente.Model.Paralelo;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesParalelo {

    private Context context;

    public OperacionesParalelo(Context context) {
        this.context = context;
    }

    private ConexionSQLiteHelper conexionDB;

    public long InsertarPar(Paralelo paralelo, List<Integer> ListIdDoc){

        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_PAR,paralelo.getNombreParalelo());
        contentValues.put(utilidades.CAMPO_NUM_ES,paralelo.getNum_estudiantes());
        contentValues.put(utilidades.CAMPO_ASI_ID,paralelo.getAsignaturaID());
        contentValues.put(utilidades.CAMPO_HOR_ID,paralelo.getHoraioID());
        contentValues.put(utilidades.CAMPO_PER_ID,paralelo.getPeriodoID());

        if (!existeParalelo(paralelo.getNombreParalelo(),paralelo.getAsignaturaID())){
            operacion = db.insertOrThrow(utilidades.TABLA_PARALELO, null, contentValues);

            int Id = (int) operacion;
            if (operacion > 0){
                for (int i = 0; i < ListIdDoc.size(); i++){
                    CrearDocentesAsignados(Id, ListIdDoc.get(i));
                }
            }
        }
        db.close();
        return operacion;

    }

    private boolean existeParalelo(String NomPar, int IdAsig){
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        boolean existe = false;
        Cursor cursor = db.query(utilidades.TABLA_PARALELO,null,
                utilidades.CAMPO_NOM_PAR + " = ? AND "+utilidades.CAMPO_ASI_ID + " = ?",new String[]{NomPar,String.valueOf(IdAsig)},
                null,null,null);
        if (cursor.getCount()>0){
            existe = true;
        }
        cursor.close();
        return (existe);
    }

    public List<Paralelo> ListarPar(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Paralelo> listaPar = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(utilidades.TABLA_PARALELO, null, null, null, null, null, null, null);
            if(cursor!=null){
                if (cursor.moveToFirst()){
                    do{
                        Paralelo par = new Paralelo();
                        par.setId_paralelo(cursor.getInt(0));
                        par.setNombreParalelo(cursor.getString(1));
                        par.setNum_estudiantes(cursor.getInt(2));
                        par.setHoraioID(cursor.getInt(3));
                        par.setAsignaturaID(cursor.getInt(4));
                        par.setPeriodoID(cursor.getInt(5));
                        listaPar.add(par);
                    }while (cursor.moveToNext());
                }
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaPar;
    }

    public Paralelo obtenerPar(long IdPar){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Paralelo par = new Paralelo();
        Cursor cursor = null;

        try {
            cursor = db.query(utilidades.TABLA_PARALELO, null,
                    utilidades.CAMPO_ID_PAR + " = ? ",new String[]{String.valueOf(IdPar)},
                    null,null,null);

            if (cursor.moveToFirst()){
                par.setId_paralelo(cursor.getInt(0));
                par.setNombreParalelo(cursor.getString(1));
                par.setNum_estudiantes(cursor.getInt(2));
                par.setHoraioID(cursor.getInt(3));
                par.setAsignaturaID(cursor.getInt(4));
                par.setPeriodoID(cursor.getInt(5));
            }
        }catch (Exception e){
            Log.e("OpFail",e.getMessage()+"");
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return par;
    }

    public long eliminarPar (long IdPar){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(utilidades.TABLA_PARALELO,
                    utilidades.CAMPO_ID_PAR + " = ? ",
                    new String[]{String.valueOf(IdPar)});

            eliminarDocentesAsignados(IdPar);

        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarPar(Paralelo paralelo, List<Integer> IdsDoc){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_PAR,paralelo.getNombreParalelo());
        contentValues.put(utilidades.CAMPO_NUM_ES,paralelo.getNum_estudiantes());
        contentValues.put(utilidades.CAMPO_HOR_ID,paralelo.getHoraioID());
        contentValues.put(utilidades.CAMPO_ASI_ID,paralelo.getAsignaturaID());
        contentValues.put(utilidades.CAMPO_PER_ID,paralelo.getPeriodoID());

        try {
            operacion = db.update(utilidades.TABLA_PARALELO, contentValues,
                    utilidades.CAMPO_ID_PAR + " = ? ",
                    new String[]{String.valueOf(paralelo.getId_paralelo())});

            if (operacion > 0 ){
                eliminarDocentesAsignados(paralelo.getId_paralelo());

                for (int i = 0; i < IdsDoc.size(); i++){
                    Log.e("idD",IdsDoc.get(i)+"");
                    CrearDocentesAsignados(paralelo.getId_paralelo(),IdsDoc.get(i));
                }
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
        return operacion;
    }

    //PARALELO_DOCENTE

    private void CrearDocentesAsignados(int IdPar, int IdDocente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_PARALELO_ID_FK3,IdPar);
        contentValues.put(utilidades.CAMPO_DOCENTE_ID_FK,IdDocente);

        try {
            db.insertOrThrow(utilidades.TABLA_PARALELO_DOCENTE,null, contentValues);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            db.close();
        }
    }

    public List<Docente> obtenerDocentesAsignadosParalelo(long IdPar){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT * FROM " + utilidades.TABLA_DOCENTE + " as D JOIN "
                + utilidades.TABLA_PARALELO_DOCENTE + " as PD ON D."+utilidades.CAMPO_ID_DOC + " = PD."+utilidades.CAMPO_DOCENTE_ID_FK
                + " WHERE PD."+utilidades.CAMPO_PARALELO_ID_FK3 + " = " + IdPar;

        List<Docente> ListaDocentes = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_DOC));
                    String docNom = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_DOC));
                    String docApel = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_APE_DOC));

                    Docente docente = new Docente(id, docNom, docApel, null, null);
                    ListaDocentes.add(docente);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            Log.e("errorDocAsig", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return ListaDocentes;
    }

    private void eliminarDocentesAsignados(long IdPar){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            db.delete(utilidades.TABLA_PARALELO_DOCENTE,utilidades.CAMPO_PARALELO_ID_FK3 + " = ? " ,
                    new String[]{String.valueOf(IdPar)});
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
    }

}


        
        