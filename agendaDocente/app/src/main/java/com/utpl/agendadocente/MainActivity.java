package com.utpl.agendadocente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.utpl.agendadocente.ui.asignatura.presentar_asignatura.AsignaturaFragment;
import com.utpl.agendadocente.ui.cuestionario.presentar_cuestionario.CuestionarioFragment;
import com.utpl.agendadocente.ui.docente.presentar_docente.DocenteFragment;
import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionFragment;
import com.utpl.agendadocente.ui.horario.presentar_horario.HorarioFragment;
import com.utpl.agendadocente.ui.paralelo.presentar_paralelo.ParaleloFragment;
import com.utpl.agendadocente.ui.periodo.presentar_periodo.PeriodoFragment;
import com.utpl.agendadocente.ui.tarea.presentar_tarea.TareaFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    AppBarConfiguration mAppBarConfiguration;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //cargar fragment principal en la actividad
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.nav_host_fragment, new ParaleloFragment());
        fragmentTransaction.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        //De acuerdo al item selecionado va a una Activity diferente
        switch (item.getItemId()){
            case R.id.nav_home:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new ParaleloFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Paralelos");
                break;
            case R.id.nav_docente:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new DocenteFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Docentes");
                break;
            case R.id.nav_asignatura:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new AsignaturaFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Asignaturas");
                break;
            case R.id.nav_tarea:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new TareaFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("TareasParalelo");
                break;
            case R.id.nav_evaluacion:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new EvaluacionFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("EvaluacionesParalelo");
                break;
            case R.id.nav_quiz:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new CuestionarioFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Cuestionarios");
                break;
            case R.id.nav_horario:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new HorarioFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Horarios");
                break;
            case R.id.nav_periodo:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new PeriodoFragment());
                fragmentTransaction.commit();
                toolbar.setTitle("Periodos");
                break;
            default:
                //Ignore this part
                break;
        }
        return false;
    }

}
