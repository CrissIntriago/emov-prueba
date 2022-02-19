/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.contabilidad.lazy.CatalogoPresupuestoLazy;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
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
@Named("dlgItem")
@ViewScoped
public class DlgItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private CatalogoPresupuestoService itemService;

    private CatalogoPresupuestoLazy lazy;

    @PostConstruct
    public void initView() {
        this.lazy = new CatalogoPresupuestoLazy();
    }

    public void seleccionar(CatalogoPresupuesto item) {
        PrimeFaces.current().dialog().closeDynamic(item);
    }

    public CatalogoPresupuestoService getItemService() {
        return itemService;
    }

    public void setItemService(CatalogoPresupuestoService itemService) {
        this.itemService = itemService;
    }

    public CatalogoPresupuestoLazy getLazy() {
        return lazy;
    }

    public void setLazy(CatalogoPresupuestoLazy lazy) {
        this.lazy = lazy;
    }

}
