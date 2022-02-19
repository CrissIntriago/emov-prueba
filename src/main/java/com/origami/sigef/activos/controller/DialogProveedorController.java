/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.ProveedorService;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "dialogProveedorController")
@ViewScoped
public class DialogProveedorController implements Serializable {

    private static final long serialVersionUID = 1L;

    public DialogProveedorController() {
    }
    private LazyModel<Proveedor> lazyProveedor;

    @Inject
    private ProveedorService proveedorService;

    @PostConstruct
    public void initView() {
        lazyProveedor = new LazyModel<>(Proveedor.class);
        lazyProveedor.getFilterss().put("estado", true);
    }

    public void close(Proveedor pro) {
        PrimeFaces.current().dialog().closeDynamic(pro);
    }

    public LazyModel<Proveedor> getLazyProveedor() {
        return lazyProveedor;
    }

    public void setLazyProveedor(LazyModel<Proveedor> lazyProveedor) {
        this.lazyProveedor = lazyProveedor;
    }

}
