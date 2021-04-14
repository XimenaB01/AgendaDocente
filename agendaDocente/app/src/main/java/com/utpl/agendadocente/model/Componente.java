package com.utpl.agendadocente.model;

public class Componente {
    private Integer id;
    private String nombreComponente;
    private String valor;
    private Integer idAsig;

    public Componente() {
    }

    public Componente(Integer id, String componente, String valor, Integer idAsig) {
        this.id = id;
        nombreComponente = componente;
        this.valor = valor;
        this.idAsig = idAsig;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponente() {
        return nombreComponente;
    }

    public void setComponente(String componente) {
        nombreComponente = componente;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getIdAsig() {
        return idAsig;
    }

    public void setIdAsig(Integer idAsig) {
        this.idAsig = idAsig;
    }
}
