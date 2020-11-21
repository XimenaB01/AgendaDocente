package com.utpl.agendadocente.ui.paralelo.PresentarParalelo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.utpl.agendadocente.ui.evaluacion.PresentarEvaluacion.Evaluaciones;
import com.utpl.agendadocente.ui.tarea.PresentarTarea.Tareas;

public class PagerController extends FragmentPagerAdapter {
    private int numOfTab;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTab = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Tareas();
            case 1:
                return new Evaluaciones();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
