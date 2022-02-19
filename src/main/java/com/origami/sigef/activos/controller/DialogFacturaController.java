/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.common.entities.Factura;
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
@Named(value = "dialogFacturaController")
@ViewScoped
public class DialogFacturaController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DialogFacturaController() {
    }
    private LazyModel<Factura> lazyModelFactura;

    @Inject
    private FacturaService facturaService;

    @PostConstruct
    public void initView() {
        lazyModelFactura = new LazyModel<>(Factura.class);
        lazyModelFactura.getFilterss().put("estado", true);
    }

    public void close(Factura factura) {
        PrimeFaces.current().dialog().closeDynamic(factura);
    }

    public LazyModel<Factura> getLazyModelFactura() {
        return lazyModelFactura;
    }

    public void setLazyModelFactura(LazyModel<Factura> lazyModelFactura) {
        this.lazyModelFactura = lazyModelFactura;
    }

}
