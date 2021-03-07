package com.utpl.agendadocente.Model;

import com.utpl.agendadocente.ui.horario.IHorario;

public class Horario implements IHorario.Time {
    private Integer id_horario;
    private String aula;
    private String dia;
    private String hora_entrada;
    private String hora_salida;

    public Horario() {
    }

    public Horario(Integer id_horario, String aula, String dia, String hora_entrada, String hora_salida) {
        this.id_horario = id_horario;
        this.aula = aula;
        this.dia = dia;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
    }

    public Horario(String dia) {
        this.dia = dia;
    }

    public Integer getId_horario() {
        return id_horario;
    }

    public void setId_horario(Integer id_horario) {
        this.id_horario = id_horario;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }


    @Override
    public Horario write() {
        return new Horario(id_horario, aula, dia, hora_entrada, hora_salida);
    }
}
