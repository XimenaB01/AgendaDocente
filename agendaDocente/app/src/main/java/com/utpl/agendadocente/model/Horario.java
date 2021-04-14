package com.utpl.agendadocente.model;

import com.utpl.agendadocente.ui.horario.IHorario;

public class Horario implements IHorario.Time {
    private Integer idHorario;
    private String aula;
    private String dia;
    private String horaEntrada;
    private String horaSalida;

    public Horario() {
    }

    public Horario(Integer idHorario, String aula, String dia, String horaEntrada, String horaSalida) {
        this.idHorario = idHorario;
        this.aula = aula;
        this.dia = dia;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public Horario(String dia) {
        this.dia = dia;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }


    @Override
    public Horario write() {
        return new Horario(idHorario, aula, dia, horaEntrada, horaSalida);
    }
}
