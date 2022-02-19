/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.EscalaSalarial;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "dialogServidorReferencia")
@ViewScoped
public class DialigoServidorReferenciaBeans implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private LazyModel<Servidor> servidorMostrar;
    private Servidor servidor;

    @PostConstruct
    public void inicializate() {
        servidor = new Servidor();
        servidorMostrar = new LazyModel<>(Servidor.class);
        servidorMostrar.setDistinct(false);
        servidorMostrar.getFilterss().put("distributivo:notEqual", null);
        servidorMostrar.getSorteds().put("persona.apellido", "ASC");

    }

    public void closeSer(Servidor s) {
        PrimeFaces.current().dialog().closeDynamic(s);
    }

    public void closeEsc(EscalaSalarial e) {
        PrimeFaces.current().dialog().closeDynamic(e);
    }

    public LazyModel<Servidor> getServidorMostrar() {
        return servidorMostrar;
    }

    public void setServidorMostrar(LazyModel<Servidor> servidorMostrar) {
        this.servidorMostrar = servidorMostrar;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }
}
