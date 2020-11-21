package com.utpl.agendadocente.ui.paralelo.PresentarParalelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.utpl.agendadocente.DataBase.OperacionesAsignatura;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.DataBase.OperacionesParalelo;
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.Asignatura;
import com.utpl.agendadocente.Entidades.Docente;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Entidades.Paralelo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion.Evaluaciones;
import com.utpl.agendadocente.ui.tarea.PresentarTarea.Tareas;

import java.util.List;

public class DetalleParaleloActivity extends AppCompatActivity {

    TextView campoParalelo, campoAlumnos, campoNomPer;
    TextView campoNomDoc, campoNomHor;
    TextView campoNomAsig, campoCarAsig;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabItemT, tabItemE;
    PagerController pagerController;

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
        campoNomHor = findViewById(R.id.campoNomHor);
        campoNomPer = findViewById(R.id.campoNomPer);

        tabLayout = findViewById(R.id.tabActividades);
        viewPager = findViewById(R.id.viewPager);
        tabItemT = findViewById(R.id.tabItemTarea);
        tabItemE = findViewById(R.id.tabItemEvaluacion);

        pagerController = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerController);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);

                if (tab.getPosition() == 0){
                    pagerController.notifyDataSetChanged();
                }else if (tab.getPosition() == 1){
                    pagerController.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado != null){
            Paralelo paralelo = (Paralelo) objetoEnviado.getSerializable("paralelo");

            if (paralelo != null){
                String mensaje = "Agregar ";
                String Paralelo = paralelo.getNombreParalelo();
                campoParalelo.setText(Paralelo);

                String numEstudiantes = Integer.toString(paralelo.getNum_estudiantes());
                campoAlumnos.setText(numEstudiantes);

                //Docente
                List<Docente> listadocentes = operacionesParalelo.obtenerDocentesAsignadosParalelo(paralelo.getId_paralelo());
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

                Tareas.newInstance(paralelo.getId_paralelo());
                Evaluaciones.newInstance(paralelo.getId_paralelo());

            }
        }
    }
}
