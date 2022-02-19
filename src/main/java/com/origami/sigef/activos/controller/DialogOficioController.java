/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.OficioService;
import com.origami.sigef.common.entities.Oficio;
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
@Named(value = "dialogOficioController")
@ViewScoped
public class DialogOficioController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DialogOficioController() {
    }

    private LazyModel<Oficio> lazyModelOficio;

    @Inject
    private OficioService oficioService;

    @PostConstruct
    public void initView() {
        lazyModelOficio = new LazyModel<>(Oficio.class);
        lazyModelOficio.getFilterss().put("estado", true);
    }

    public void close(Oficio oficio) {
        PrimeFaces.current().dialog().closeDynamic(oficio);
    }

    public LazyModel<Oficio> getLazyModelOficio() {
        return lazyModelOficio;
    }

    public void setLazyModelOficio(LazyModel<Oficio> lazyModelOficio) {
        this.lazyModelOficio = lazyModelOficio;
    }

}
