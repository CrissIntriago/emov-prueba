/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CertAdeudarDTO implements Serializable {

    private String name;
    private List<CertificadoAdeudarDeta> lista;

    public CertAdeudarDTO() {
    }

    public CertAdeudarDTO(String name, List<CertificadoAdeudarDeta> lista) {
        this.name = name;
        this.lista = lista;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CertificadoAdeudarDeta> getLista() {
        return lista;
    }

    public void setLista(List<CertificadoAdeudarDeta> lista) {
        this.lista = lista;
    }

}
