package com.utpl.agendadocente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario.CuestionarioListarActivity;
import com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion.EvaluacionListarActivity;
import com.utpl.agendadocente.Features.Horario.PresentarHorario.HorarioListarActivity;
import com.utpl.agendadocente.Features.PagerController;
import com.utpl.agendadocente.Features.Periodo.PresentarPeriodo.PeriodoListarActivity;
import com.utpl.agendadocente.Features.Tarea.PresentarTarea.TareaListarActivity;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tabItem1, tabItem2, tabItem3;
    PagerController pagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabItem1 = findViewById(R.id.idParalelos);
        tabItem2 = findViewById(R.id. idDocentes);
        tabItem3 = findViewById(R.id.idAsignaturas);

        pagerController = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerController);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);

                if (tab.getPosition()==0){
                    pagerController.notifyDataSetChanged();
                }else if (tab.getPosition()==1){
                    pagerController.notifyDataSetChanged();
                }else if (tab.getPosition()==2){
                    pagerController.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    //Método para mostrar y ocultar el menúItem
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    //Método para asignar las funciones correspondientes a las opciones
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = null;
        //De acuerdo al item selecionado va a una Activity diferente
        switch (item.getItemId()){
            case R.id.itemHorario:
                intent = new Intent(MainActivity.this, HorarioListarActivity.class);
                break;
            case R.id.itemPeriodo:
                intent = new Intent(MainActivity.this, PeriodoListarActivity.class);
                break;
            case R.id.itemCuestionario:
                intent = new Intent(MainActivity.this, CuestionarioListarActivity.class);
                break;
            case R.id.itemTarea:
                intent = new Intent(MainActivity.this, TareaListarActivity.class);
                break;
            case R.id.itemEvaluacion:
                intent = new Intent(MainActivity.this, EvaluacionListarActivity.class);
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
