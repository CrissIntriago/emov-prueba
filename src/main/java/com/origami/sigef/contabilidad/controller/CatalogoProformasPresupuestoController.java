/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.contabilidad.lazy.CatalogoPresupuestoLazy;
import com.origami.sigef.contabilidad.lazy.ProformaPresupuestoPlanificadoLazy;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss
 */
@Named(value = "catalogoProformaPresupuestoView")
@ViewScoped
public class CatalogoProformasPresupuestoController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /* Inject de los services a utilizar en este Bean */
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaPresupuestoService;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;

    /* Declaracion de objetos de varios tipos */
    private ProformaPresupuestoPlanificado proformaPresupuestoPlanificado;
    private CatalogoProformaPresupuesto catalogoProformaSeleccionado;

    /*Declaracion de los objetos de tipo Lazy*/
    private ProformaPresupuestoPlanificadoLazy dlgCatalogoPresupuestoEgresoLazy;
    private CatalogoPresupuestoLazy dlgCatalogoPresupuestoIngresoLazy;

    /*Declaracion de los objetos de tipo Lazy*/
    private LazyModel<ProformaPresupuestoPlanificado> dlgCatalogoPresupuestoEgresoLazyModel;
    private LazyModel<CatalogoProformaPresupuesto> catalogoProformaPresupuestoLazyModel;

    @PostConstruct
    public void init() {
        proformaPresupuestoPlanificado = new ProformaPresupuestoPlanificado();
        dlgCatalogoPresupuestoEgresoLazyModel = new LazyModel<>(ProformaPresupuestoPlanificado.class);

        /*Inicializando objetos de tipo Lazy*/
        catalogoProformaPresupuestoLazyModel = new LazyModel<>(CatalogoProformaPresupuesto.class);
        catalogoProformaPresupuestoLazyModel.getSorteds().put("periodo", "DESC");
        catalogoProformaPresupuestoLazyModel.getFilterss().put("estado", true);
    }

    public void showDlgProformaPresupuesto(CatalogoProformaPresupuesto catalogoProformaPresupuesto) {
        dlgCatalogoPresupuestoIngresoLazy = new CatalogoPresupuestoLazy();
        dlgCatalogoPresupuestoEgresoLazy = new ProformaPresupuestoPlanificadoLazy();
        catalogoProformaSeleccionado = catalogoProformaPresupuesto;
        if (catalogoProformaPresupuesto.getTipoFlujo().equals(true)) {
            /*Muestra una vista del presupuesto de Ingresos*/
            dlgCatalogoPresupuestoIngresoLazy = new CatalogoPresupuestoLazy(catalogoProformaPresupuesto);
            PrimeFaces.current().executeScript("PF('dlgCatalogoPresupuestoIngreso').show()");
            PrimeFaces.current().ajax().update(":formDlgCatalogoPresupuestoIngreso");
            PrimeFaces.current().ajax().update(":catalogoProformaPresupuestoIngresoTable");
        } else if (catalogoProformaPresupuesto.getTipoFlujo().equals(false)) {
            /*Muestra una vista del presupuesto de Egresos*/
            dlgCatalogoPresupuestoEgresoLazy = new ProformaPresupuestoPlanificadoLazy(catalogoProformaPresupuesto);
            PrimeFaces.current().executeScript("PF('dlgCatalogoPresupuestoEgreso').show()");
            PrimeFaces.current().ajax().update(":formDlgCatalogoPresupuestoEgreso");
            PrimeFaces.current().ajax().update(":catalogoProformaPresupuestoEgresoTable");
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public CatalogoProformaPresupuestoService getCatalogoProformaPresupuestoService() {
        return catalogoProformaPresupuestoService;
    }

    public void setCatalogoProformaPresupuestoService(CatalogoProformaPresupuestoService catalogoProformaPresupuestoService) {
        this.catalogoProformaPresupuestoService = catalogoProformaPresupuestoService;
    }

    public ProformaPresupuestoPlanificadoService getProformaPresupuestoPlanificadoService() {
        return proformaPresupuestoPlanificadoService;
    }

    public void setProformaPresupuestoPlanificadoService(ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService) {
        this.proformaPresupuestoPlanificadoService = proformaPresupuestoPlanificadoService;
    }

    public ProformaPresupuestoPlanificado getProformaPresupuestoPlanificado() {
        return proformaPresupuestoPlanificado;
    }

    public void setProformaPresupuestoPlanificado(ProformaPresupuestoPlanificado proformaPresupuestoPlanificado) {
        this.proformaPresupuestoPlanificado = proformaPresupuestoPlanificado;
    }

    public ProformaPresupuestoPlanificadoLazy getDlgCatalogoPresupuestoEgresoLazy() {
        return dlgCatalogoPresupuestoEgresoLazy;
    }

    public void setDlgCatalogoPresupuestoEgresoLazy(ProformaPresupuestoPlanificadoLazy dlgCatalogoPresupuestoEgresoLazy) {
        this.dlgCatalogoPresupuestoEgresoLazy = dlgCatalogoPresupuestoEgresoLazy;
    }

    public LazyModel<ProformaPresupuestoPlanificado> getDlgCatalogoPresupuestoEgresoLazyModel() {
        return dlgCatalogoPresupuestoEgresoLazyModel;
    }

    public void setDlgCatalogoPresupuestoEgresoLazyModel(LazyModel<ProformaPresupuestoPlanificado> dlgCatalogoPresupuestoEgresoLazyModel) {
        this.dlgCatalogoPresupuestoEgresoLazyModel = dlgCatalogoPresupuestoEgresoLazyModel;
    }

    public LazyModel<CatalogoProformaPresupuesto> getCatalogoProformaPresupuestoLazyModel() {
        return catalogoProformaPresupuestoLazyModel;
    }

    public void setCatalogoProformaPresupuestoLazyModel(LazyModel<CatalogoProformaPresupuesto> catalogoProformaPresupuestoLazyModel) {
        this.catalogoProformaPresupuestoLazyModel = catalogoProformaPresupuestoLazyModel;
    }

    public CatalogoPresupuestoLazy getDlgCatalogoPresupuestoIngresoLazy() {
        return dlgCatalogoPresupuestoIngresoLazy;
    }

    public void setDlgCatalogoPresupuestoIngresoLazy(CatalogoPresupuestoLazy dlgCatalogoPresupuestoIngresoLazy) {
        this.dlgCatalogoPresupuestoIngresoLazy = dlgCatalogoPresupuestoIngresoLazy;
    }

    public CatalogoProformaPresupuesto getCatalogoProformaSeleccionado() {
        return catalogoProformaSeleccionado;
    }

    public void setCatalogoProformaSeleccionado(CatalogoProformaPresupuesto catalogoProformaSeleccionado) {
        this.catalogoProformaSeleccionado = catalogoProformaSeleccionado;
    }
//</editor-fold>

}
