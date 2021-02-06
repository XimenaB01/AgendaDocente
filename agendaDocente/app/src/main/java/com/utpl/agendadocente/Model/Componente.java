package com.utpl.agendadocente.Model;

public class Componente {
    private Integer Id;
    private String NombreComponente;
    private String Valor;
    private Integer IdAsig;

    public Componente() {
    }

    public Componente(Integer id, String componente, String valor, Integer idAsig) {
        Id = id;
        NombreComponente = componente;
        Valor = valor;
        IdAsig = idAsig;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getComponente() {
        return NombreComponente;
    }

    public void setComponente(String componente) {
        NombreComponente = componente;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public Integer getIdAsig() {
        return IdAsig;
    }

    public void setIdAsig(Integer idAsig) {
        IdAsig = idAsig;
    }
}
