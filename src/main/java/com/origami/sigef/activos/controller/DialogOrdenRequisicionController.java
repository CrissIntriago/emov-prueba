/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.common.entities.OrdenRequisicion;
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
@Named(value = "dialogOrdenRequisicionController")
@ViewScoped
public class DialogOrdenRequisicionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private LazyModel<OrdenRequisicion> lazyRequisicion;

    @Inject
    private OrdenRequisicionService ordenRequisicionService;

    @PostConstruct
    public void InitView() {
        lazyRequisicion = new LazyModel<>(OrdenRequisicion.class);
        lazyRequisicion.getFilterss().put("estado", true);
        lazyRequisicion.getFilterss().put("estadoSolicitud", "APROBADO");
    }

    public void close(OrdenRequisicion ord) {
        PrimeFaces.current().dialog().closeDynamic(ord);
    }

    public LazyModel<OrdenRequisicion> getLazyRequisicion() {
        return lazyRequisicion;
    }

    public void setLazyRequisicion(LazyModel<OrdenRequisicion> lazyRequisicion) {
        this.lazyRequisicion = lazyRequisicion;
    }

}
