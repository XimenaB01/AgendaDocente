package com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion;

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
import com.utpl.agendadocente.DataBase.OperacionesEvaluacion;
import com.utpl.agendadocente.Entidades.Evaluacion;
import com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario.CuestionarioListarActivity;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearActivity;
import com.utpl.agendadocente.Features.Evaluacion.CrearEvaluacion.EvaluacionCrearListener;
import com.utpl.agendadocente.Features.Horario.PresentarHorario.HorarioListarActivity;
import com.utpl.agendadocente.Features.Periodo.PresentarPeriodo.PeriodoListarActivity;
import com.utpl.agendadocente.Features.Tarea.PresentarTarea.TareaListarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class EvaluacionListarActivity extends AppCompatActivity implements EvaluacionCrearListener {

    private OperacionesEvaluacion operacionesEvaluacion = new OperacionesEvaluacion(this);

    RecyclerView listEvaRV;
    TextView ListaPeriodoVacia;

    List<Evaluacion> listaEvaluacion = new ArrayList<>();
    EvaluacionListaRecycleViewAdapter evaluacionListaRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_evaluacion);

        listEvaRV = findViewById(R.id.listaEva);
        ListaPeriodoVacia = findViewById(R.id.emptyListETextView);

        listaEvaluacion.addAll(operacionesEvaluacion.ListarEva());

        evaluacionListaRecycleViewAdapter = new EvaluacionListaRecycleViewAdapter(this, listaEvaluacion);
        listEvaRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listEvaRV.setAdapter(evaluacionListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = findViewById(R.id.evaluacionFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEvaluacionCreateDialog();
            }
        });
    }

    @Override
    public void onCrearEvaluacion(Evaluacion evaluacion) {
        listaEvaluacion.add(evaluacion);
        evaluacionListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaEvaluacion.isEmpty()) {
            ListaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            ListaPeriodoVacia.setVisibility(View.GONE);
        }
    }

    private void openEvaluacionCreateDialog (){
        EvaluacionCrearActivity evaluacionCrearActivity = EvaluacionCrearActivity.newInstance("Nueva Evaluación", this);
        evaluacionCrearActivity.show(getSupportFragmentManager(), utilidades.CREAR);
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
                intent = new Intent(EvaluacionListarActivity.this, HorarioListarActivity.class);
                finish();
                break;
            case R.id.itemPeriodo:
                intent = new Intent(EvaluacionListarActivity.this, PeriodoListarActivity.class);
                finish();
                break;
            case R.id.itemCuestionario:
                intent = new Intent(EvaluacionListarActivity.this, CuestionarioListarActivity.class);
                finish();
                break;
            case R.id.itemTarea:
                intent = new Intent(EvaluacionListarActivity.this, TareaListarActivity.class);
                finish();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
