/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Nivel;
import com.origami.sigef.common.service.NivelService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.CatalogoPresupuestoLazy;
import com.origami.sigef.contabilidad.service.CatalogoProformaPresupuestoService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author kriiz
 */
@Named(value = "dlgProformaPresupuestoView")
@ViewScoped
public class DlgProformaPresupuesto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Inject
    private CatalogoProformaPresupuestoService catalogoProformaPresupuestoService;
    @Inject
    private NivelService nivelService;

    private CatalogoPresupuestoLazy dlgCatalogoPresupuestoLazy;

    private List<Nivel> niveles;

    private CatalogoProformaPresupuesto catalogoProformaPresupuesto;
    private OpcionBusqueda opcionBusqueda;

    @PostConstruct
    public void init() {
        /*Inicializando las List*/
        dlgCatalogoPresupuestoLazy = new CatalogoPresupuestoLazy(catalogoProformaPresupuesto);
        niveles = nivelService.findByNamedQuery("Nivel.findByCatalogoAndCodigo", new Object[]{"tipo_cuenta", "CP"});
        opcionBusqueda = new OpcionBusqueda();
        dlgCatalogoPresupuestoLazy = new CatalogoPresupuestoLazy(catalogoProformaPresupuesto);
    }

    public void showDlgProformaPresupuesto(CatalogoProformaPresupuesto catalogoProformaPresupuesto) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("width", 1200);
        options.put("height", 550);
        options.put("contentWidth", "100%");
        options.put("contentHeight", "100%");
        options.put("headerElement", "customheader");

        if (catalogoProformaPresupuesto.getTipoFlujo() == true) {
            System.out.println("*************************************************************************");
            System.out.println("ES INGRESO");
            System.out.println("showDlgProformaPresupuesto ENVIA: " + catalogoProformaPresupuesto);
            dlgCatalogoPresupuestoLazy = new CatalogoPresupuestoLazy(catalogoProformaPresupuesto);
        } else {
            System.out.println("*************************************************************************");
            System.out.println("Es EGRESO");
            System.out.println("showDlgProformaPresupuesto ENVIA: " + catalogoProformaPresupuesto);
            dlgCatalogoPresupuestoLazy = new CatalogoPresupuestoLazy(catalogoProformaPresupuesto);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public CatalogoProformaPresupuestoService getCatalogoProformaPresupuestoService() {
        return catalogoProformaPresupuestoService;
    }

    public void setCatalogoProformaPresupuestoService(CatalogoProformaPresupuestoService catalogoProformaPresupuestoService) {
        this.catalogoProformaPresupuestoService = catalogoProformaPresupuestoService;
    }

    public NivelService getNivelService() {
        return nivelService;
    }

    public void setNivelService(NivelService nivelService) {
        this.nivelService = nivelService;
    }

    public CatalogoPresupuestoLazy getDlgCatalogoPresupuestoLazy() {
        return dlgCatalogoPresupuestoLazy;
    }

    public void setDlgCatalogoPresupuestoLazy(CatalogoPresupuestoLazy dlgCatalogoPresupuestoLazy) {
        this.dlgCatalogoPresupuestoLazy = dlgCatalogoPresupuestoLazy;
    }

    public List<Nivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<Nivel> niveles) {
        this.niveles = niveles;
    }

    public CatalogoProformaPresupuesto getCatalogoProformaPresupuesto() {
        return catalogoProformaPresupuesto;
    }

    public void setCatalogoProformaPresupuesto(CatalogoProformaPresupuesto catalogoProformaPresupuesto) {
        this.catalogoProformaPresupuesto = catalogoProformaPresupuesto;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
//</editor-fold>

}
