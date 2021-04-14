package com.utpl.agendadocente.ui.paralelo.presentar_paralelo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.utpl.agendadocente.database.OperacionesAsignatura;
import com.utpl.agendadocente.database.OperacionesHorario;
import com.utpl.agendadocente.database.OperacionesParalelo;
import com.utpl.agendadocente.database.OperacionesPeriodo;
import com.utpl.agendadocente.model.Asignatura;
import com.utpl.agendadocente.model.Docente;
import com.utpl.agendadocente.model.Horario;
import com.utpl.agendadocente.model.Paralelo;
import com.utpl.agendadocente.model.PeriodoAcademico;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionesParalelo;
import com.utpl.agendadocente.ui.tarea.presentar_tarea.TareasParalelo;

import java.util.List;

public class DetalleParaleloActivity extends AppCompatActivity {

    TextView campoParalelo;
    TextView campoAlumnos;
    TextView campoNomPer;
    TextView campoNomDoc;
    TextView campoNomHor;
    TextView campoNomAsig;
    TextView campoCarAsig;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabItemT;
    TabItem tabItemE;
    PagerController pagerController;

    private String mensaje = "Agregar ";
    private Paralelo paralelo = null;
    private OperacionesParalelo operacionesParalelo;
    private OperacionesAsignatura operacionesAsignatura;
    private OperacionesPeriodo operacionesPeriodo;
    private OperacionesHorario operacionesHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paralelo);

        operacionesAsignatura = new OperacionesAsignatura(this);
        operacionesPeriodo = new OperacionesPeriodo(this);
        operacionesParalelo = new OperacionesParalelo(this);
        operacionesHorario = new OperacionesHorario(this);

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
                    viewPager.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_surface));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Ignore this part
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Ignore this part
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado != null){
            paralelo = (Paralelo) objetoEnviado.getSerializable("paralelo");

            if (paralelo != null){

                String paraleloTexto = paralelo.getNombreParalelo();
                campoParalelo.setText(paraleloTexto);

                String numEstudiantes = Integer.toString(paralelo.getNumEstudiantes());
                campoAlumnos.setText(numEstudiantes);

                obtenerDocenteAsignados();
                obtenerAsignaturaAsignada();
                obtenerHorarioAsignado();
                obtenerPeriodoAsignada();

                TareasParalelo.newInstance(paralelo.getIdParalelo());
                EvaluacionesParalelo.newInstance(paralelo.getIdParalelo());
            }
        }
    }

    private void obtenerPeriodoAsignada() {
        //Periodo
        if (paralelo.getPeriodoID() != -1){
            PeriodoAcademico periodo = operacionesPeriodo.obtenerPeriodo(paralelo.getPeriodoID());
            if (periodo != null){
                campoNomPer.setText(String.format("%s - %s",periodo.getFechaInicio(),periodo.getFechaFin()));
            }else {
                Toast.makeText(this, "La Periodo ya no existe", Toast.LENGTH_LONG).show();
            }
        }else {
            campoNomPer.setText(String.format("%s Periodo",mensaje));
        }
    }

    private void obtenerDocenteAsignados(){
        //Docente
        List<Docente> listadocentes = operacionesParalelo.obtenerDocentesAsignadosParalelo(paralelo.getIdParalelo());
        if (!listadocentes.isEmpty()){
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
    }
    private void obtenerAsignaturaAsignada(){
        //Asignatura
        Asignatura asignatura = operacionesAsignatura.obtenerAsignatura(paralelo.getAsignaturaID());
        if (asignatura != null){
            campoNomAsig.setText(asignatura.getNombreAsignatura());
            campoCarAsig.setText(asignatura.getCarrera());
        } else {
            Toast.makeText(this, "La Asignatura ya no existe", Toast.LENGTH_LONG).show();
        }

    }
    private void obtenerHorarioAsignado(){
        //Horario
        if (paralelo.getHoraioID() != -1){
            Horario horario = operacionesHorario.obtenerHorario(paralelo.getHoraioID());
            if (horario != null){
                campoNomHor.setText(String.format("%s Aula:%s De:%s A:%s", horario.getDia(), horario.getAula(), horario.getHoraEntrada(), horario.getHoraSalida()));
            }else {
                Toast.makeText(this, "El Horario ya no existe", Toast.LENGTH_LONG).show();
            }
        }else {
            campoNomHor.setText(String.format("%s Horario",mensaje));
        }
    }
}

