package com.utpl.agendadocente.observer;

import com.utpl.agendadocente.ui.tarea.ITarea;

import java.util.ArrayList;
import java.util.List;

public class SimpleSubject implements Subject{

    private List<Observer> observers;
    private Integer id;
    private String estado;
    private ITarea.ActualizarTareaListener listener;

    public SimpleSubject(){
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers){
            observer.update(id,estado, listener);
        }
    }

    public void setValues(Integer id, String estado, ITarea.ActualizarTareaListener lis){
        this.id = id;
        this.estado = estado;
        this.listener = lis;
        notifyObservers();
    }
}
