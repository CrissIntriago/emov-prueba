/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.OrdenCompraService;
import com.origami.sigef.common.entities.OrdenCompra;
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
@Named(value = "dialogOrdenCompraController")
@ViewScoped
public class DialogOrdenCompraController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DialogOrdenCompraController() {
    }
    private LazyModel<OrdenCompra> lazyModelOrden;

    @Inject
    private OrdenCompraService ordenCompraService;

    @PostConstruct
    public void initView() {
        lazyModelOrden = new LazyModel<>(OrdenCompra.class);
        lazyModelOrden.getFilterss().put("estado", true);
    }

    public void close(OrdenCompra orden) {
        PrimeFaces.current().dialog().closeDynamic(orden);
    }

    public LazyModel<OrdenCompra> getLazyModelOrden() {
        return lazyModelOrden;
    }

    public void setLazyModelOrden(LazyModel<OrdenCompra> lazyModelOrden) {
        this.lazyModelOrden = lazyModelOrden;
    }

}
