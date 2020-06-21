package com.utpl.agendadocente.Features.Periodo.PresentarPeriodo;

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
import com.utpl.agendadocente.DataBase.OperacionesPeriodo;
import com.utpl.agendadocente.Entidades.PeriodoAcademico;
import com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario.CuestionarioListarActivity;
import com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion.EvaluacionListarActivity;
import com.utpl.agendadocente.Features.Horario.PresentarHorario.HorarioListarActivity;
import com.utpl.agendadocente.Features.Periodo.CrearPeriodo.PeriodoCrearActivity;
import com.utpl.agendadocente.Features.Periodo.CrearPeriodo.PeriodoCreateListener;
import com.utpl.agendadocente.Features.Tarea.PresentarTarea.TareaListarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;

public class PeriodoListarActivity extends AppCompatActivity implements PeriodoCreateListener {

    private OperacionesPeriodo operacionesPeriodo = new OperacionesPeriodo(this);

    RecyclerView listPerRV;
    TextView ListaPeriodoVacia;

    List<PeriodoAcademico> listaPeriodo = new ArrayList<>();
    PeriodoListaRecycleViewAdapter periodoListaRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_periodo);

        listPerRV = findViewById(R.id.listaPer);
        ListaPeriodoVacia = findViewById(R.id.emptyListPTextView);

        listaPeriodo.addAll(operacionesPeriodo.ListarPer());

        periodoListaRecycleViewAdapter = new PeriodoListaRecycleViewAdapter(this, listaPeriodo);
        listPerRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listPerRV.setAdapter(periodoListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = findViewById(R.id.periodoFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPeriodoCreateDialog();
            }
        });
    }

    private void openPeriodoCreateDialog() {
        PeriodoCrearActivity crearPeriodo = PeriodoCrearActivity.newInstance("Nuevo Periodo",this);
        crearPeriodo.setCancelable(false);
        crearPeriodo.show(getSupportFragmentManager(), utilidades.CREAR);
    }

    @Override
    public void onCrearPeriodo(PeriodoAcademico periodo) {
        listaPeriodo.add(periodo);
        periodoListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (listaPeriodo.isEmpty()) {
            ListaPeriodoVacia.setVisibility(View.VISIBLE);
        }else{
            ListaPeriodoVacia.setVisibility(View.GONE);
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
                intent = new Intent(PeriodoListarActivity.this, HorarioListarActivity.class);
                finish();
                break;
            case R.id.itemCuestionario:
                intent = new Intent(PeriodoListarActivity.this, CuestionarioListarActivity.class);
                finish();
                break;
            case R.id.itemTarea:
                intent = new Intent(PeriodoListarActivity.this, TareaListarActivity.class);
                finish();
                break;
            case R.id.itemEvaluacion:
                intent = new Intent(PeriodoListarActivity.this, EvaluacionListarActivity.class);
                finish();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
