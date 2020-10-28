package com.utpl.agendadocente.Features.Tarea.PresentarTarea;

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
import com.utpl.agendadocente.DataBase.OperacionesTarea;
import com.utpl.agendadocente.Entidades.Tarea;
import com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario.CuestionarioListarActivity;
import com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion.EvaluacionListarActivity;
import com.utpl.agendadocente.Features.Horario.PresentarHorario.HorarioListarActivity;
import com.utpl.agendadocente.Features.Periodo.PresentarPeriodo.PeriodoListarActivity;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearActivity;
import com.utpl.agendadocente.Features.Tarea.CrearTarea.TareaCrearListener;
import com.utpl.agendadocente.MainActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class TareaListarActivity extends AppCompatActivity implements TareaCrearListener {

    private OperacionesTarea operacionesTarea = new OperacionesTarea(this);

    RecyclerView listTarRV;
    TextView ListaTareaVacia;

    List<Tarea> listaTarea = new ArrayList<>();
    TareaListaRecycleViewAdapter tareaListaRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_tareas);

        listTarRV = findViewById(R.id.listaTar);
        ListaTareaVacia = findViewById(R.id.emptyListTTextView);

        listaTarea.addAll(operacionesTarea.ListarTar());

        tareaListaRecycleViewAdapter = new TareaListaRecycleViewAdapter(this, listaTarea);
        listTarRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTarRV.setAdapter(tareaListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = findViewById(R.id.tareaFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTareaCreateDialog();
            }
        });
    }

    private void openTareaCreateDialog() {
        TareaCrearActivity crearTarea = TareaCrearActivity.newInstance("Nueva Tarea",this);
        crearTarea.show(getSupportFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearTarea(Tarea tarea) {
        listaTarea.add(tarea);
        tareaListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaTarea.isEmpty()) {
            ListaTareaVacia.setVisibility(View.VISIBLE);
        }else{
            ListaTareaVacia.setVisibility(View.GONE);
        }
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
                intent = new Intent(TareaListarActivity.this, HorarioListarActivity.class);
                finish();
                break;
            case R.id.itemPeriodo:
                intent = new Intent(TareaListarActivity.this, PeriodoListarActivity.class);
                finish();
                break;
            case R.id.itemCuestionario:
                intent = new Intent(TareaListarActivity.this, CuestionarioListarActivity.class);
                finish();
                break;
            case R.id.itemEvaluacion:
                intent = new Intent(TareaListarActivity.this, EvaluacionListarActivity.class);
                finish();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}