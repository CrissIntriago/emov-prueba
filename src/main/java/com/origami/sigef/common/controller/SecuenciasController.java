/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.controller;

import com.origami.sigef.common.entities.SecuenciaGeneral;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.SecuenciaGeneralService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "secuenciasView")
@ViewScoped
public class SecuenciasController implements Serializable {

    @Inject
    private SecuenciaGeneralService secuenciaGeneralService;

    private OpcionBusqueda opcionBusqueda;

    private List<Short> listadoPeriodo;

    private LazyModel<SecuenciaGeneral> secuenciaLazy;

    private SecuenciaGeneral secuenciaGeneral;

    @PostConstruct
    public void initialize() {
        opcionBusqueda = new OpcionBusqueda();
        initLazy();
        secuenciaGeneral = new SecuenciaGeneral();
        listadoPeriodo = secuenciaGeneralService.getListPeriodos();
    }

    public void actualizarPeriodo() {
        if (opcionBusqueda.getAnio() != null) {
            initLazy();
        } else {
            secuenciaLazy = null;
        }
    }

    private void initLazy() {
        secuenciaLazy = new LazyModel<>(SecuenciaGeneral.class);
        secuenciaLazy.getSorteds().put("id", "ASC");
        secuenciaLazy.getFilterss().put("anio", opcionBusqueda.getAnio());
    }

    public void openDlg(SecuenciaGeneral secuencia) {
        if (secuencia != null) {
            this.secuenciaGeneral = secuencia;
        }
        PrimeFaces.current().executeScript("PF('dlgFormuarioSecuencia').show()");
        PrimeFaces.current().ajax().update("formSecuencia");
    }

    public void closeDlg() {
        if (secuenciaGeneral.getId() != null) {
            secuenciaGeneralService.edit(secuenciaGeneral);
            JsfUtil.addSuccessMessage("INFO!!!", "Secuencia editado correctamente");
        }
        PrimeFaces.current().executeScript("PF('dlgFormuarioSecuencia').hide()");
        PrimeFaces.current().ajax().update("dataTableSecuencias");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public List<Short> getListadoPeriodo() {
        return listadoPeriodo;
    }

    public void setListadoPeriodo(List<Short> listadoPeriodo) {
        this.listadoPeriodo = listadoPeriodo;
    }

    public LazyModel<SecuenciaGeneral> getSecuenciaLazy() {
        return secuenciaLazy;
    }

    public void setSecuenciaLazy(LazyModel<SecuenciaGeneral> secuenciaLazy) {
        this.secuenciaLazy = secuenciaLazy;
    }

    public SecuenciaGeneral getSecuenciaGeneral() {
        return secuenciaGeneral;
    }

    public void setSecuenciaGeneral(SecuenciaGeneral secuenciaGeneral) {
        this.secuenciaGeneral = secuenciaGeneral;
    }

}
