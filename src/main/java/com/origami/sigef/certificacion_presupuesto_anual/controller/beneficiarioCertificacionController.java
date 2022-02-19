/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.activos.lazy.ProveedorLazy;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.talentohumano.Lazy.ServidorLazy;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "beneficiarioCertificacionView")
@ViewScoped
public class beneficiarioCertificacionController implements Serializable {

    private ProveedorLazy proveedorLazy;
    private LazyModel<Proveedor> proveedorLazy1;
    private ServidorLazy servidorLazy;

    @PostConstruct
    public void init() {
        proveedorLazy = new ProveedorLazy();
        servidorLazy = new ServidorLazy();
        proveedorLazy1 = new LazyModel(Proveedor.class);
    }

    public ProveedorLazy getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(ProveedorLazy proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }

    public LazyModel<Proveedor> getProveedorLazy1() {
        return proveedorLazy1;
    }

    public void setProveedorLazy1(LazyModel<Proveedor> proveedorLazy1) {
        this.proveedorLazy1 = proveedorLazy1;
    }

    public ServidorLazy getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(ServidorLazy servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

}
