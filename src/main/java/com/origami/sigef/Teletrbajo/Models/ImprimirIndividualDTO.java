/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Models;

import com.origami.sigef.Teletrbajo.Entity.Teletrabajo;
import java.io.Serializable;

/**
 *
 * @author DEVELOPER
 */
public class ImprimirIndividualDTO implements Serializable {

    private Long id;
    private Teletrabajo data;
    private String herramientaUtilizads;
    private String nombreCompleto;
    private String cargo;
    private String requiriente;

    public ImprimirIndividualDTO() {
    }

    public ImprimirIndividualDTO(Teletrabajo data, String herramientaUtilizads, String nombreCompleto, String cargo,String requiriente) {
        this.data = data;
        this.herramientaUtilizads = herramientaUtilizads;
        this.nombreCompleto = nombreCompleto;
        this.cargo = cargo;
        this.requiriente=requiriente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teletrabajo getData() {
        return data;
    }

    public void setData(Teletrabajo data) {
        this.data = data;
    }

    public String getHerramientaUtilizads() {
        return herramientaUtilizads;
    }

    public void setHerramientaUtilizads(String herramientaUtilizads) {
        this.herramientaUtilizads = herramientaUtilizads;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getRequiriente() {
        return requiriente;
    }

    public void setRequiriente(String requiriente) {
        this.requiriente = requiriente;
    }
    
}
