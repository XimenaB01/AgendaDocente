package com.utpl.agendadocente.Features.Horario.PresentarHorario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesHorario;
import com.utpl.agendadocente.Entidades.Horario;
import com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario.CuestionarioListarActivity;
import com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion.EvaluacionListarActivity;
import com.utpl.agendadocente.Features.Horario.CrearHorario.HorarioCrearActivity;
import com.utpl.agendadocente.Features.Horario.CrearHorario.HorarioCrearListener;
import com.utpl.agendadocente.Features.Periodo.PresentarPeriodo.PeriodoListarActivity;
import com.utpl.agendadocente.Features.Tarea.PresentarTarea.TareaListarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class HorarioListarActivity extends AppCompatActivity implements HorarioCrearListener {

    private OperacionesHorario operacionesHorario = new OperacionesHorario(this);

    RecyclerView listHorRV;
    TextView ListaHorarioVacia;

    List<Horario> listaHorario = new ArrayList<>();
    HorarioListaRecycleViewAdapter horarioListaRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_horario);

        listHorRV = findViewById(R.id.listaHor);
        ListaHorarioVacia = findViewById(R.id.emptyListHTextView);

        listaHorario.addAll(operacionesHorario.ListarHor());
        horarioListaRecycleViewAdapter = new HorarioListaRecycleViewAdapter(this, listaHorario);
        listHorRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        listHorRV.setAdapter(horarioListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = findViewById(R.id.horarioFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHorarioCreateDialog();
            }
        });
    }

    @Override
    public void onCrearHorario(Horario horario) {
        listaHorario.add(horario);
        horarioListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaHorario.isEmpty()) {
            ListaHorarioVacia.setVisibility(View.VISIBLE);
        }else{
            ListaHorarioVacia.setVisibility(View.GONE);
        }
    }

    private void openHorarioCreateDialog() {
        HorarioCrearActivity horarioCrearActivity = HorarioCrearActivity.newInstance("Nuevo Horario", this);
        horarioCrearActivity.setCancelable(false);
        horarioCrearActivity.show(getSupportFragmentManager(),utilidades.CREAR);
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
            case R.id.itemPeriodo:
                intent = new Intent(HorarioListarActivity.this, PeriodoListarActivity.class);
                finish();
                break;
            case R.id.itemCuestionario:
                intent = new Intent(HorarioListarActivity.this, CuestionarioListarActivity.class);
                finish();
                break;
            case R.id.itemTarea:
                intent = new Intent(HorarioListarActivity.this, TareaListarActivity.class);
                finish();
                break;
            case R.id.itemEvaluacion:
                intent = new Intent(HorarioListarActivity.this, EvaluacionListarActivity.class);
                finish();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
