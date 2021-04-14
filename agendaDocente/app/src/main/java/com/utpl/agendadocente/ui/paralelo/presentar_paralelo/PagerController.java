package com.utpl.agendadocente.ui.paralelo.presentar_paralelo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.utpl.agendadocente.ui.evaluacion.presentar_evaluacion.EvaluacionesParalelo;
import com.utpl.agendadocente.ui.tarea.presentar_tarea.TareasParalelo;

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
                return new TareasParalelo();
            case 1:
                return new EvaluacionesParalelo();
            default:
                return new Fragment();
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
