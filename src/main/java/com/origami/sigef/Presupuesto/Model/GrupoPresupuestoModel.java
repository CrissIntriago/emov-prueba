/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Model;

import java.io.Serializable;

/**
 *
 * @author Criss Intriago
 */
public class GrupoPresupuestoModel implements Serializable {

    private Integer titulo;
    private Integer naturaleza;
    private String grupo;

    public GrupoPresupuestoModel() {
    }

    public GrupoPresupuestoModel(Integer titulo, Integer naturaleza, String grupo) {
        this.titulo = titulo;
        this.naturaleza = naturaleza;
        this.grupo = grupo;
    }

    public Integer getTitulo() {
        return titulo;
    }

    public void setTitulo(Integer titulo) {
        this.titulo = titulo;
    }

    public Integer getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(Integer naturaleza) {
        this.naturaleza = naturaleza;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

}
