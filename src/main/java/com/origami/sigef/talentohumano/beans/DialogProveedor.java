/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jesus
 */
@Named(value = "dialogProveedor")
@ViewScoped
public class DialogProveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    private LazyModel<Proveedor> lazy;

    @PostConstruct
    public void init() {
        lazy = new LazyModel<>(Proveedor.class, "id", "ASC");
        lazy.getFilterss().put("estado", true);
    }

    public void close(Proveedor p) {
        PrimeFaces.current().dialog().closeDynamic(p);
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LazyModel<Proveedor> getLazy() {
        return lazy;
    }
    
    public void setLazy(LazyModel<Proveedor> lazy) {
        this.lazy = lazy;
    }
//</editor-fold>

}
