package com.utpl.agendadocente.Features;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.utpl.agendadocente.Features.Asignatura.PresentarAsignatura.AsignaturaPager;
import com.utpl.agendadocente.Features.Docente.PresentarDocente.DocentePager;
import com.utpl.agendadocente.Features.Paralelo.PresentarParalelo.ParaleloPager;

public class PagerController extends FragmentPagerAdapter {
    private int numOfTabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ParaleloPager();
            case 1:
                return new DocentePager();
            case 2:
                return new AsignaturaPager();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
