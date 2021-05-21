package com.utpl.agendadocente.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.features.flyweight.OperacionesInterfaz;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.util.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OperacionesParalelo implements OperacionesInterfaz.OperacionParalelo{

    private Context context;
    private String parametro = " = ? ";
    private ConexionSQLiteHelper conexionDB;

    public OperacionesParalelo(Context context) {
        this.context = context;
    }

    public long insertarParalelo(Paralelo paralelo, List<Integer> listaIdDoc){

        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForParalelo(paralelo);

        if (!existeParalelo(paralelo.getNombreParalelo(),paralelo.getAsignaturaID())){
            operacion = db.insertOrThrow(Utilidades.TABLA_PARALELO, null, contentValues);

            int id = (int) operacion;
            if (operacion > 0){
                for (int i = 0; i < listaIdDoc.size(); i++){
                    crearDocentesAsignados(id, listaIdDoc.get(i));
                }
            }
        }
        db.close();
        return operacion;

    }

    private boolean existeParalelo(String nombreParalelo, int idAsignatura){
        SQLiteDatabase db = conexionDB.getReadableDatabase();
        boolean existe = false;
        Cursor cursor = db.query(Utilidades.TABLA_PARALELO,null,
                Utilidades.CAMPO_NOM_PAR + " = ? AND "+ Utilidades.CAMPO_ASI_ID + parametro,new String[]{nombreParalelo,String.valueOf(idAsignatura)},
                null,null,null);
        if (cursor.getCount()>0){
            existe = true;
        }
        cursor.close();
        return (existe);
    }

    public List<Paralelo> listarParalelo(){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        List<Paralelo> listaParalelo = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor =  db.query(Utilidades.TABLA_PARALELO, null, null, null, null, null, null, null);
            if(cursor!=null && cursor.moveToFirst()){
                do{
                    Paralelo par = getParaleloForCursor(cursor);
                    listaParalelo.add(par);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            Toast.makeText(context, "Operacion fallida", Toast.LENGTH_SHORT).show();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaParalelo;
    }

    public Paralelo obtenerParalelo(long idParalelo){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Paralelo paralelo = null;
        Cursor cursor = null;

        try {
            cursor = db.query(Utilidades.TABLA_PARALELO, null,
                    Utilidades.CAMPO_ID_PAR + parametro,new String[]{String.valueOf(idParalelo)},
                    null,null,null);

            if (cursor.moveToFirst()){
                paralelo = getParaleloForCursor(cursor);
            }
        }catch (Exception e){
            Log.e("OpFail",e.getMessage()+"");
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return paralelo;
    }

    public long eliminarParalelo(long idPar){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(Utilidades.TABLA_PARALELO,
                    Utilidades.CAMPO_ID_PAR + parametro,
                    new String[]{String.valueOf(idPar)});

            eliminarDocentesAsignados(idPar);

        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long modificarParalelo(Paralelo paralelo, List<Integer> idsDoc){

        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = getContentValuesForParalelo(paralelo);

        try {
            operacion = db.update(Utilidades.TABLA_PARALELO, contentValues,
                    Utilidades.CAMPO_ID_PAR + parametro,
                    new String[]{String.valueOf(paralelo.getIdParalelo())});

            if (operacion > 0 ){
                eliminarDocentesAsignados(paralelo.getIdParalelo());

                for (int i = 0; i < idsDoc.size(); i++){
                    crearDocentesAsignados(paralelo.getIdParalelo(),idsDoc.get(i));
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

    private void crearDocentesAsignados(int idPar, int idDocente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_PARALELO_ID_FK3,idPar);
        contentValues.put(Utilidades.CAMPO_DOCENTE_ID_FK,idDocente);

        try {
            db.insertOrThrow(Utilidades.TABLA_PARALELO_DOCENTE,null, contentValues);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            db.close();
        }
    }

    public List<Docente> obtenerDocentesAsignadosParalelo(long idParalelo){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT * FROM " + Utilidades.TABLA_DOCENTE + " as D JOIN "
                + Utilidades.TABLA_PARALELO_DOCENTE + " as PD ON D."+ Utilidades.CAMPO_ID_DOC + " = PD."+ Utilidades.CAMPO_DOCENTE_ID_FK
                + " WHERE PD."+ Utilidades.CAMPO_PARALELO_ID_FK3 + " = " + idParalelo;

        List<Docente> listaDocentes = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()){
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_DOC));
                    String docNom = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_DOC));
                    String docApel = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_APE_DOC));

                    Docente docente = new Docente(id, docNom, docApel, null, null);
                    listaDocentes.add(docente);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            Log.e("errorDocAsig", Objects.requireNonNull(e.getMessage()));
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return listaDocentes;
    }

    private void eliminarDocentesAsignados(long idParalelo){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            db.delete(Utilidades.TABLA_PARALELO_DOCENTE, Utilidades.CAMPO_PARALELO_ID_FK3 + parametro ,
                    new String[]{String.valueOf(idParalelo)});
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
    }

    private ContentValues getContentValuesForParalelo(Paralelo paralelo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.CAMPO_NOM_PAR,paralelo.getNombreParalelo());
        contentValues.put(Utilidades.CAMPO_NUM_ES,paralelo.getNumEstudiantes());
        contentValues.put(Utilidades.CAMPO_ASI_ID,paralelo.getAsignaturaID());
        contentValues.put(Utilidades.CAMPO_HOR_ID,paralelo.getHoraioID());
        contentValues.put(Utilidades.CAMPO_PER_ID,paralelo.getPeriodoID());
        return contentValues;
    }

    private Paralelo getParaleloForCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID_PAR));
        String nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOM_PAR));
        int numEstudiantes = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_NUM_ES));
        Integer idHorario = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_HOR_ID));
        Integer idAsignatura = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ASI_ID));
        Integer idPeriodo = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_PER_ID));
        return new Paralelo(id,nombre,numEstudiantes,idHorario,idAsignatura,idPeriodo);
    }

}


        
        