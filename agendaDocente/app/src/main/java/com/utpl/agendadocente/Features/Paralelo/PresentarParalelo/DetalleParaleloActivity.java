package com.utpl.agendadocente.Features.Paralelo.PresentarParalelo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.R;

import java.util.List;

public class DetalleParaleloActivity extends AppCompatActivity {

    TextView campoParalelo, campoAlumnos;
    TextView campoNomDoc;
    TextView campoNomAsig, campoCarAsig;
    TextView campoNomTar;
    TextView campoNomEva;
    TextView campoNomHor;
    TextView campoNomPer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paralelo);

        OperacionesAsignatura operacionesAsignatura = new OperacionesAsignatura(this);
        OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(this);
        OperacionesParalelo operacionesParalelo = new OperacionesParalelo(this);
        OperacionesHorario operacionesHorario = new OperacionesHorario(this);

        campoParalelo = findViewById(R.id.campoNombreParalelo);
        campoAlumnos = findViewById(R.id.campoNumAlumnos);

        campoNomDoc = findViewById(R.id.campoNombreDocente);

        campoNomAsig = findViewById(R.id.campoNomAsi);
        campoCarAsig = findViewById(R.id.campoCarAsi);

        campoNomTar = findViewById(R.id.campoNomTar);

        campoNomEva = findViewById(R.id.campoNomEva);

        campoNomHor = findViewById(R.id.campoNomHor);
        campoNomPer = findViewById(R.id.campoNomPer);

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado != null){
            Paralelo paralelo = (Paralelo) objetoEnviado.getSerializable("paralelo");

            if (paralelo != null){
                String mensaje = "Agregar ";
                String Paralelo = paralelo.getNombreParalelo();
                campoParalelo.setText(Paralelo);

                String numEstudiantes = Integer.toString(paralelo.getNum_estudiantes());
                campoAlumnos.setText(numEstudiantes);

                String NomParalelo = paralelo.getNombreParalelo();
                int IdAsignatura = paralelo.getAsignaturaID();

                //Docente
                List<Docente> listadocentes = operacionesParalelo.obtenerDocentesAsignadosParalelo(NomParalelo, IdAsignatura);
                if (listadocentes.size() != 0){
                    StringBuilder docentes = new StringBuilder();
                    for (int i = 0; i < listadocentes.size(); i++){
                        docentes.append(String.format("%s %s",listadocentes.get(i).getNombreDocente(),listadocentes.get(i).getApellidoDocente()));
                        if (i != listadocentes.size()-1){
                            docentes.append("\n");
                        }
                    }
                    campoNomDoc.setText(docentes);
                }else {
                    campoNomDoc.setText(String.format("%s Docente(s)",mensaje));
                }


                //Asignatura
                Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());
                if (asignatura != null){
                    campoNomAsig.setText(asignatura.getNombreAsignatura());
                    campoCarAsig.setText(asignatura.getCarrera());
                } else {
                    Toast.makeText(this, "La asignatura ya no existe", Toast.LENGTH_LONG).show();
                }

                //Horario
                if (paralelo.getHoraioID() != -1){
                    Horario horario = operacionesHorario.obtenerHor(paralelo.getHoraioID());
                    if (horario != null){
                        campoNomHor.setText(String.format("%s - %s", horario.getHora_entrada(), horario.getHora_salida()));
                    }else {
                        Toast.makeText(this, "El Horario ya no existe", Toast.LENGTH_LONG).show();
                    }
                }else {
                    campoNomHor.setText(String.format("%s Horario",mensaje));
                }

                //Periodo
                if (paralelo.getPeriodoID() != -1){
                    PeriodoAcademico periodo = operacionesPeriodo.obtenerPer(paralelo.getPeriodoID());
                    if (periodo != null){
                        campoNomPer.setText(String.format("%s - %s",periodo.getFechaInicio(),periodo.getFechaFin()));
                    }else {
                        Toast.makeText(this, "La Periodo ya no existe", Toast.LENGTH_LONG).show();
                    }
                }else {
                    campoNomPer.setText(String.format("%s Periodo",mensaje));
                }

                //Tarea
                List<Tarea> listaTarea = operacionesParalelo.obtenerTareasAsignadasParalelo(NomParalelo, IdAsignatura);
                if (listaTarea.size() != 0){
                    StringBuilder Tareas = new StringBuilder();
                    for (int i = 0; i < listaTarea.size(); i++) {
                        Tareas.append(listaTarea.get(i).getNombreTarea());
                        if (i != listaTarea.size() - 1) {
                            Tareas.append("\n");
                        }
                    }
                    campoNomTar.setText(Tareas);
                }else {
                    campoNomTar.setText(String.format("%s Tarea(s)",mensaje));
                }


                //Evaluación
                List<Evaluacion> listaEvaluacion = operacionesParalelo.obtenerEvaluacionesAsignadasParalalelo(NomParalelo, IdAsignatura);
                if (listaEvaluacion.size() != 0){
                    StringBuilder Evaluaciones = new StringBuilder();
                    for (int i = 0; i < listaEvaluacion.size(); i++){
                        Evaluaciones.append(listaEvaluacion.get(i).getNombreEvaluacion());
                        if (i != listaEvaluacion.size()-1){
                            Evaluaciones.append("\n");
                        }
                    }
                    campoNomEva.setText(Evaluaciones);
                }else {
                    campoNomEva.setText(String.format("%s Evaluación(es)",mensaje));
                }
            }
        }
    }
}
