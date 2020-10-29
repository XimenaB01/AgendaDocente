package com.utpl.agendadocente.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.DocenteAsignado;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.EvaluacionAsignada;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Entidades.TareaAsignada;
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

    public long InsertarPar(Paralelo paralelo, List<Integer> ListIdDoc, List<Integer> ListIdTar, List<Integer> ListIdEva){

        long operacion= 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_NOM_PAR,paralelo.getNombreParalelo());
        contentValues.put(utilidades.CAMPO_NUM_ES,paralelo.getNum_estudiantes());
        contentValues.put(utilidades.CAMPO_ASI_ID,paralelo.getAsignaturaID());
        contentValues.put(utilidades.CAMPO_HOR_ID,paralelo.getHoraioID());
        contentValues.put(utilidades.CAMPO_PER_ID,paralelo.getPeriodoID());

        if (!existePrimaryKey(paralelo.getNombreParalelo(),paralelo.getAsignaturaID())){
            operacion = db.insert(utilidades.TABLA_PARALELO, null, contentValues);

            if (operacion > 0){
                for (int i = 0; i < ListIdDoc.size(); i++){
                    CrearDocentesAsignados(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(), ListIdDoc.get(i));
                }
                for (int i = 0; i < ListIdTar.size(); i++){
                    CrearTareasAsignadas(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(), ListIdTar.get(i));
                }
                for (int i = 0; i < ListIdEva.size(); i++){
                    CrearEvaluacionesAsignadas(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(), ListIdEva.get(i));
                }
            }
        }
        db.close();
        return operacion;

    }

    private boolean existePrimaryKey(String NomPar, int IdAsig){
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
                        par.setNombreParalelo(cursor.getString(0));
                        par.setNum_estudiantes(cursor.getInt(1));
                        par.setHoraioID(cursor.getInt(2));
                        par.setAsignaturaID(cursor.getInt(3));
                        par.setPeriodoID(cursor.getInt(4));
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

    public Paralelo obtenerPar(String NomPar, Integer IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        Paralelo par = new Paralelo();
        Cursor cursor = null;

        try {
            cursor = db.query(utilidades.TABLA_PARALELO, null,
                    utilidades.CAMPO_NOM_PAR + " = ? AND "+utilidades.CAMPO_ASI_ID + " = ?",new String[]{NomPar,String.valueOf(IdAsig)},
                    null,null,null);

            if (cursor.moveToFirst()){
                par.setNombreParalelo(cursor.getString(0));
                par.setNum_estudiantes(cursor.getInt(1));
                par.setHoraioID(cursor.getInt(2));
                par.setAsignaturaID(cursor.getInt(3));
                par.setPeriodoID(cursor.getInt(4));
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

    public long eliminarPar (String NomPar, Integer IdAsig){
        long operacion = 0;

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            operacion = db.delete(utilidades.TABLA_PARALELO,
                    utilidades.CAMPO_NOM_PAR + " = ? AND " + utilidades.CAMPO_ASI_ID + " = ?",
                    new String[]{NomPar,String.valueOf(IdAsig)});

            eliminarDocentesAsignados(NomPar, IdAsig);
            eliminarTareasAsignados(NomPar, IdAsig);
            eliminarEvaluacionesAsignados(NomPar, IdAsig);

        }catch (SQLiteException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }finally {
            db.close();
        }
        return operacion;
    }

    public long ModificarPar(String NomPar, Integer IdAsig,Paralelo paralelo, List<Integer> IdsDoc,  List<Integer> IdsTar,  List<Integer> IdsEva){

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
                    utilidades.CAMPO_NOM_PAR + " = ? AND " + utilidades.CAMPO_ASI_ID + " = ?",
                    new String[]{NomPar,String.valueOf(IdAsig)});

            if (operacion > 0 ){
                eliminarDocentesAsignados(paralelo.getNombreParalelo(), paralelo.getAsignaturaID());
                eliminarTareasAsignados(paralelo.getNombreParalelo(), paralelo.getAsignaturaID());
                eliminarEvaluacionesAsignados(paralelo.getNombreParalelo(), paralelo.getAsignaturaID());

                for (int i = 0; i < IdsDoc.size(); i++){
                    CrearDocentesAsignados(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(),IdsDoc.get(i));
                }

                for (int i = 0; i < IdsTar.size(); i++){
                    CrearTareasAsignadas(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(),IdsTar.get(i));
                }

                for (int i = 0; i < IdsEva.size(); i++){
                    CrearEvaluacionesAsignadas(paralelo.getNombreParalelo(), paralelo.getAsignaturaID(),IdsEva.get(i));
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

    private void CrearDocentesAsignados(String NomPAr , int IdAsig, int IdDocente){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_PARALELO_NOM_FK,NomPAr);
        contentValues.put(utilidades.CAMPO_PARALELO_ASIG_FK,IdAsig);
        contentValues.put(utilidades.CAMPO_DOCENTE_ID_FK,IdDocente);

        try {
            db.insertOrThrow(utilidades.TABLA_PARALELO_DOCENTE,null, contentValues);
        }catch (SQLiteException e){
            e.getCause();
        }finally {
            db.close();
        }
    }

    public List<Docente> obtenerDocentesAsignadosParalelo(String NomPAr , int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT * FROM " + utilidades.TABLA_DOCENTE + " as D JOIN "
                + utilidades.TABLA_PARALELO_DOCENTE + " as PD ON D."+utilidades.CAMPO_ID_DOC + " = PD."+utilidades.CAMPO_DOCENTE_ID_FK
                + " WHERE PD."+utilidades.CAMPO_PARALELO_NOM_FK + " = '"+ NomPAr + "' AND "
                + "PD."+utilidades.CAMPO_PARALELO_ASIG_FK + " = " + IdAsig;

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
//Método docente por estado
    /*public List<DocenteAsignado> obtenerDocentesPorEstado(String NomPar, int IdAsig) {
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT D."+utilidades.CAMPO_ID_DOC +" , D."+utilidades.CAMPO_NOM_DOC + " , D."+utilidades.CAMPO_APE_DOC + " , PD."+utilidades.CAMPO_PARALELO_NOM_FK +" , PD."+utilidades.CAMPO_PARALELO_ASIG_FK
                + " FROM " + utilidades.TABLA_DOCENTE + " as D LEFT JOIN " + utilidades.TABLA_PARALELO_DOCENTE + " as PD ON D."+utilidades.CAMPO_ID_DOC
                + " = PD."+utilidades.CAMPO_DOCENTE_ID_FK + " AND PD."+utilidades.CAMPO_PARALELO_NOM_FK + " = '" + NomPar + "' AND PD."+utilidades.CAMPO_PARALELO_ASIG_FK + " = " + IdAsig;

        List<DocenteAsignado> docenteAsignados = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    int idDoc = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_DOC));
                    String nomDoc = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_DOC));
                    String apelDoc = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_APE_DOC));

                    boolean estado = false;

                    if ( cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ASIG_FK))> 0){
                        estado = true;
                    }

                    DocenteAsignado DocAsig = new DocenteAsignado(idDoc,nomDoc,apelDoc,null,null, estado);
                    docenteAsignados.add(DocAsig);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            Log.e("ErrorDocEs",e.getMessage()+"");
        }finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return docenteAsignados;
    }*/

    private void eliminarDocentesAsignados(String NomPar, int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            db.delete(utilidades.TABLA_PARALELO_DOCENTE,utilidades.CAMPO_PARALELO_NOM_FK + " = ? AND " + utilidades.CAMPO_PARALELO_ASIG_FK + " = ?" ,
                    new String[]{NomPar, String.valueOf(IdAsig)});
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
    }

    // PARALELO TAREA

    public void CrearTareasAsignadas(String NomPar, int IdAsig, Integer IdTarea) {

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_PARALELO_NOM_FK2,NomPar);
        contentValues.put(utilidades.CAMPO_PARALELO_ASIG_FK2,IdAsig);
        contentValues.put(utilidades.CAMPO_TAREA_ID_FK,IdTarea);

        try {
            db.insertOrThrow(utilidades.TABLA_PARALELO_TAREA,null, contentValues);
        }catch (SQLiteException e){
            Log.e("Tarea",e.getMessage()+"");
        }finally {
            db.close();
        }
    }

    private void eliminarTareasAsignados(String NomPar, int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            db.delete(utilidades.TABLA_PARALELO_TAREA,utilidades.CAMPO_PARALELO_NOM_FK2 + " = ? AND " + utilidades.CAMPO_PARALELO_ASIG_FK2 + " = ?",
                    new String[]{NomPar,String.valueOf(IdAsig)});
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
    }

    public List<Tarea> obtenerTareasAsignadasParalelo( String NomPar, int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT * FROM " + utilidades.TABLA_TAREA + " as T JOIN "
                + utilidades.TABLA_PARALELO_TAREA + " as PT ON T."+utilidades.CAMPO_ID_TAR + " = PT."+utilidades.CAMPO_TAREA_ID_FK
                + " WHERE PT."+utilidades.CAMPO_PARALELO_NOM_FK2 + " = '" + NomPar +"' AND PT."+utilidades.CAMPO_PARALELO_ASIG_FK2 + " = " + IdAsig;

        List<Tarea> ListaTareas = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()){
                do {
                    int idTar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_TAR));
                    String tarNom = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_TAR));

                    Tarea tarea = new Tarea(idTar, tarNom, null, null, null, null);
                    ListaTareas.add(tarea);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return ListaTareas;
    }
//comentar
    /*public List<TareaAsignada> obtenerTareasPorEstado(String NomPar, int IdAsig){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query ="SELECT T."+utilidades.CAMPO_ID_TAR +", T."+utilidades.CAMPO_NOM_TAR + ", PT."+utilidades.CAMPO_PARALELO_NOM_FK2 + ", PT."+utilidades.CAMPO_PARALELO_ASIG_FK2
                + " FROM " + utilidades.TABLA_TAREA + " as T LEFT JOIN " + utilidades.TABLA_PARALELO_TAREA + " as PT ON T."+utilidades.CAMPO_ID_TAR
                + " = PT."+utilidades.CAMPO_TAREA_ID_FK + " AND PT."+utilidades.CAMPO_PARALELO_NOM_FK2 + " = '" + NomPar +"' AND PT."+utilidades.CAMPO_PARALELO_ASIG_FK2 +" = " + IdAsig;

        List<TareaAsignada> tareaAsignadas = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {

                    int idTar = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_TAR));
                    String nomTar = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_TAR));

                    boolean estado = false;

                    if ( cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ASIG_FK2))> 0){
                        estado = true;
                    }

                    TareaAsignada TarAsig = new TareaAsignada(idTar,nomTar,null,null,null, null, estado);
                    tareaAsignadas.add(TarAsig);

                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return tareaAsignadas;
    }*/

    // PARALELO EVALUACION

    private void CrearEvaluacionesAsignadas(String NomPar,int IdAsig, Integer IdEvaluacion){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(utilidades.CAMPO_PARALELO_NOM_FK3,NomPar);
        contentValues.put(utilidades.CAMPO_PARALELO_ASIG_FK3,IdAsig);
        contentValues.put(utilidades.CAMPO_EVALUACION_ID_FK,IdEvaluacion);

        try {
            db.insertOrThrow(utilidades.TABLA_PARALELO_EVALUACION,null, contentValues);
        }catch (SQLiteException e){
            Log.e("Evaluacion",e.getMessage()+"");
        }finally {
            db.close();
        }
    }


    private void eliminarEvaluacionesAsignados(String NomPar, int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getWritableDatabase();

        try {
            db.delete(utilidades.TABLA_PARALELO_EVALUACION,utilidades.CAMPO_PARALELO_NOM_FK3 + " = ? AND " + utilidades.CAMPO_PARALELO_ASIG_FK3 + " = ?" ,
                    new String[]{NomPar,String.valueOf(IdAsig)});
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            db.close();
        }
    }


    public List<Evaluacion> obtenerEvaluacionesAsignadasParalalelo(String NomPar, int IdAsig){

        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT * FROM " + utilidades.TABLA_EVALUACION + " as E JOIN "
                + utilidades.TABLA_PARALELO_EVALUACION + " as PE ON E."+utilidades.CAMPO_ID_EVA + " = PE."+utilidades.CAMPO_EVALUACION_ID_FK
                + " WHERE PE."+utilidades.CAMPO_PARALELO_NOM_FK3 + " = '" + NomPar +"' AND PE."+utilidades.CAMPO_PARALELO_ASIG_FK3 + " = " + IdAsig ;

        List<Evaluacion> ListaEvaluacion = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()){
                do {
                    int evaId = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_EVA));
                    String evaNom = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_EVA));

                    Evaluacion evaluacion = new Evaluacion(evaId, evaNom, null, null, null, null, null);
                    ListaEvaluacion.add(evaluacion);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            if (cursor!=null)
                cursor.close();
            db.close();
        }
        return ListaEvaluacion;
    }
//comentar
    public List<EvaluacionAsignada> obtenerEvaluacionesPorEstado(String NomPar, int IdAsig){
        conexionDB = ConexionSQLiteHelper.getInstance(context);
        SQLiteDatabase db = conexionDB.getReadableDatabase();

        String query = "SELECT E."+utilidades.CAMPO_ID_EVA +" , E."+utilidades.CAMPO_NOM_EVA + " , PE."+utilidades.CAMPO_PARALELO_NOM_FK3 + " , PE."+utilidades.CAMPO_PARALELO_ASIG_FK3
                + " FROM " + utilidades.TABLA_EVALUACION + " as E LEFT JOIN " + utilidades.TABLA_PARALELO_EVALUACION + " as PE ON E."+utilidades.CAMPO_ID_EVA
                + " = PE."+utilidades.CAMPO_EVALUACION_ID_FK + " AND PE."+utilidades.CAMPO_PARALELO_NOM_FK3 + " = '" + NomPar + "' AND PE."+utilidades.CAMPO_PARALELO_ASIG_FK3 + " = " + IdAsig;


        List<EvaluacionAsignada> evaluacionAsigada = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do {
                    int idEva = cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_ID_EVA));
                    String nomEva = cursor.getString(cursor.getColumnIndex(utilidades.CAMPO_NOM_EVA));

                    boolean estado = false;

                    if ( cursor.getInt(cursor.getColumnIndex(utilidades.CAMPO_PARALELO_ASIG_FK3))> 0){
                        estado = true;
                    }

                    EvaluacionAsignada EvaAsig = new EvaluacionAsignada(idEva,nomEva,null,null,null,null,null, estado);
                    evaluacionAsigada.add(EvaAsig);
                }while (cursor.moveToNext());
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return evaluacionAsigada;

    }

}


        
        