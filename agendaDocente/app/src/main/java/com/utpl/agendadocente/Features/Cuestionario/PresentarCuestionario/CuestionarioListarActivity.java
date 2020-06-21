package com.utpl.agendadocente.Features.Cuestionario.PresentarCuestionario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utpl.agendadocente.DataBase.OperacionesCuestionario;
import com.utpl.agendadocente.Entidades.Cuestionario;
import com.utpl.agendadocente.Features.Cuestionario.CrearCuestionario.CuestionarioCrearActivity;
import com.utpl.agendadocente.Features.Cuestionario.CrearCuestionario.CuestionarioCrearListener;
import com.utpl.agendadocente.Features.Evaluacion.PresentarEvaluacion.EvaluacionListarActivity;
import com.utpl.agendadocente.Features.Horario.PresentarHorario.HorarioListarActivity;
import com.utpl.agendadocente.Features.Periodo.PresentarPeriodo.PeriodoListarActivity;
import com.utpl.agendadocente.Features.Tarea.PresentarTarea.TareaListarActivity;
import com.utpl.agendadocente.R;
import com.utpl.agendadocente.Utilidades.utilidades;

import java.util.ArrayList;
import java.util.List;


public class CuestionarioListarActivity extends AppCompatActivity implements CuestionarioCrearListener {

    private OperacionesCuestionario operacionesCuestionario = new OperacionesCuestionario(this);

    private List<Cuestionario> listaCuestionario = new ArrayList<>();

    private TextView ListaCuestionarioVacia;
    private CuestionarioListaRecycleViewAdapter cuestionarioListaRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cuestionario);

        RecyclerView lisCuestRV = findViewById(R.id.RVCuestionario);
        ListaCuestionarioVacia = findViewById(R.id.emptyListCTextView);

        listaCuestionario = operacionesCuestionario.ListarCuest();

        cuestionarioListaRecycleViewAdapter = new CuestionarioListaRecycleViewAdapter(this, listaCuestionario);
        lisCuestRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lisCuestRV.setAdapter(cuestionarioListaRecycleViewAdapter);

        viewVisibility();

        FloatingActionButton floatingActionButton = findViewById(R.id.cuestionarioFAB);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCuestionarioCreateDialog();
            }
        });
    }

    public void viewVisibility() {
        if (listaCuestionario.isEmpty()) {
            ListaCuestionarioVacia.setVisibility(View.VISIBLE);
        }else{
            ListaCuestionarioVacia.setVisibility(View.GONE);
        }
    }

    private void openCuestionarioCreateDialog() {
        CuestionarioCrearActivity crearCuestionario = CuestionarioCrearActivity.newInstance("Nuevo Cuestionario", this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        crearCuestionario.show(fragmentTransaction, utilidades.CREAR);
    }

    @Override
    public void onCrearCuestionario(Cuestionario cuestionario) {
        listaCuestionario.add(cuestionario);
        cuestionarioListaRecycleViewAdapter.notifyDataSetChanged();
        viewVisibility();
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
                intent = new Intent(CuestionarioListarActivity.this, HorarioListarActivity.class);
                finish();
                break;
            case R.id.itemPeriodo:
                intent = new Intent(CuestionarioListarActivity.this, PeriodoListarActivity.class);
                finish();
                break;
            case R.id.itemTarea:
                intent = new Intent(CuestionarioListarActivity.this, TareaListarActivity.class);
                finish();
                break;
            case R.id.itemEvaluacion:
                intent = new Intent(CuestionarioListarActivity.this, EvaluacionListarActivity.class);
                finish();
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}