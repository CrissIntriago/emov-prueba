/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Models;

import java.util.Date;
import java.util.Objects;


/**
 *
 * @author Administrator
 */
public class Evento {

    private Long id;
    private String titulo;
    private Date dataInicio;
    private Date dataFim;
    private boolean diaInteiro;
    private TipoEvento tipoEvento;

    public Evento() {
        this.tipoEvento = TipoEvento.PENDIENTE;
        this.titulo = "";
        this.diaInteiro = false;
    }

    public Evento(Long id, String titulo, Date dataInicio, Date dataFim, boolean diaInteiro, TipoEvento tipoEvento) {
        this.id = id;
        this.titulo = titulo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.diaInteiro = diaInteiro;
        this.tipoEvento = tipoEvento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isDiaInteiro() {
        return diaInteiro;
    }

    public void setDiaInteiro(boolean diaInteiro) {
        this.diaInteiro = diaInteiro;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Evento other = (Evento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
