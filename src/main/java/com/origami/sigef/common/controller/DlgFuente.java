/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.contabilidad.lazy.FuenteFinanciamientoLazy;
import com.origami.sigef.contabilidad.service.FuenteFinanciamientoService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author kriiz
 */
@Named("dlgFuente")
@ViewScoped
public class DlgFuente implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private FuenteFinanciamientoService fuenteService;

    private FuenteFinanciamientoLazy lazy;

    @PostConstruct
    public void initView() {
        this.lazy = new FuenteFinanciamientoLazy();
    }

    public void seleccionar(FuenteFinanciamiento fuente) {
        PrimeFaces.current().dialog().closeDynamic(fuente);
    }

    public FuenteFinanciamientoService getFuenteService() {
        return fuenteService;
    }

    public void setFuenteService(FuenteFinanciamientoService fuenteService) {
        this.fuenteService = fuenteService;
    }

    public FuenteFinanciamientoLazy getLazy() {
        return lazy;
    }

    public void setLazy(FuenteFinanciamientoLazy lazy) {
        this.lazy = lazy;
    }

}
