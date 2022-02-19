/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRolDetalle;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThLiquidacionRolDetalleService;
import com.origami.sigef.resource.talento_humano.services.ThLiquidacionRolService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
@Named(value = "thLiquidacionRolView")
@ViewScoped
public class ThLiquidacionRolController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThLiquidacionRolService thLiquidacionRolService;
    @Inject
    private ThLiquidacionRolDetalleService thLiquidacionRolDetalleService;
    @Inject
    private ServletSession servletSession;

    private LazyModel<ThLiquidacionRol> thliquidacionRolLazy;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThLiquidacionRol thLiquidacionRol;

    private List<ThTipoRol> tipoRolList;
    private List<ThLiquidacionRolDetalle> thLiquidacionRolDetalleList;
    private List<Short> periodos;

    private Boolean btnCargarDatos, view;

    @PostConstruct
    public void init() {
        btnCargarDatos = Boolean.TRUE;
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        updateTipoRolList();
    }

    public void updateTipoRolList() {
        if (opcionBusqueda.getAnio() != null) {
            tipoRolList = thInterfaces.tipoRol(opcionBusqueda.getAnio());
        } else {
            tipoRolList = new ArrayList<>();
        }
    }

    public void updateLazyModel() {
        if (thTipoRol != null) {
            thliquidacionRolLazy = new LazyModel<>(ThLiquidacionRol.class);
            thliquidacionRolLazy.getSorteds().put("idServidorCargo.idServidor.persona.apellido", "ASC");
            thliquidacionRolLazy.getFilterss().put("estado", true);
            thliquidacionRolLazy.getFilterss().put("idTipoRol", thTipoRol);
            thliquidacionRolLazy.setDistinct(false);
        } else {
            thliquidacionRolLazy = null;
        }
    }

    public void loadDatos() {
        if (thTipoRol == null) {
            return;
        }
        if (thTipoRol.getAprobado()) {
            JsfUtil.addWarningMessage("AVISO!!!", "No se pueden cargar datos porque el rol seleccionado ya se encuentra registrado");
            return;
        }
        int valor = thLiquidacionRolService.createLiquidacion(thTipoRol, thInterfaces.getUser(), new Date());
        updateLazyModel();
        PrimeFaces.current().ajax().update("thLiquidacionRolTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se han cargado " + valor + " datos correctamente");
    }

    public void form(ThLiquidacionRol thLiquidacionRol, Boolean view) {
        this.view = view;
        this.thLiquidacionRol = thLiquidacionRol;
        thLiquidacionRolDetalleList = thLiquidacionRolDetalleService.findByNamedQuery("ThLiquidacionRolDetalle.findByLiquidacion", thLiquidacionRol);
        PrimeFaces.current().ajax().update("thLiquidacionRolDetalleTable");
        JsfUtil.executeJS("PF('thLiquidacionDetalleDlg').show()");
        PrimeFaces.current().ajax().update("thLiquidacionDetalleForm");
    }

    public void updateRubros(ThLiquidacionRol thLiquidacionRol) {
        int aux = thLiquidacionRolService.updateLiquidacion(thLiquidacionRol, thInterfaces.getUser());
        updateLazyModel();
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha actualizado los datos correctamente");
        PrimeFaces.current().ajax().update("thLiquidacionRolTable");
    }

    public void reporte(ThLiquidacionRol thLiquidacionRol, String tipoArchivo) {
        servletSession.addParametro("id_liquidacion", thLiquidacionRol.getId());
        servletSession.addParametro("mes", thLiquidacionRol.getIdTipoRol().getIdMes().getTexto());
        servletSession.addParametro("periodo", thLiquidacionRol.getIdTipoRol().getPeriodo());
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_talento_humano");
        servletSession.setNombreReporte("liquidacion_rol_individual");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void updateValores(ThLiquidacionRolDetalle thLiquidacionRolDetalle) {
        thLiquidacionRolDetalleService.edit(thLiquidacionRolDetalle);
        thLiquidacionRolService.updateLiquidacion(thLiquidacionRolDetalle.getIdLiquidacionRol(), thInterfaces.getUser());
        thLiquidacionRolDetalleList = thLiquidacionRolDetalleService.findByNamedQuery("ThLiquidacionRolDetalle.findByLiquidacion", thLiquidacionRol);
        PrimeFaces.current().ajax().update("thLiquidacionRolTable");
    }

    public LazyModel<ThLiquidacionRol> getThliquidacionRolLazy() {
        return thliquidacionRolLazy;
    }

    public void setThliquidacionRolLazy(LazyModel<ThLiquidacionRol> thliquidacionRolLazy) {
        this.thliquidacionRolLazy = thliquidacionRolLazy;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public ThTipoRol getThTipoRol() {
        return thTipoRol;
    }

    public void setThTipoRol(ThTipoRol thTipoRol) {
        this.thTipoRol = thTipoRol;
    }

    public List<ThTipoRol> getTipoRolList() {
        return tipoRolList;
    }

    public void setTipoRolList(List<ThTipoRol> tipoRolList) {
        this.tipoRolList = tipoRolList;
    }

    public List<ThLiquidacionRolDetalle> getThLiquidacionRolDetalleList() {
        return thLiquidacionRolDetalleList;
    }

    public void setThLiquidacionRolDetalleList(List<ThLiquidacionRolDetalle> thLiquidacionRolDetalleList) {
        this.thLiquidacionRolDetalleList = thLiquidacionRolDetalleList;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public Boolean getBtnCargarDatos() {
        return btnCargarDatos;
    }

    public void setBtnCargarDatos(Boolean btnCargarDatos) {
        this.btnCargarDatos = btnCargarDatos;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public ThLiquidacionRol getThLiquidacionRol() {
        return thLiquidacionRol;
    }

    public void setThLiquidacionRol(ThLiquidacionRol thLiquidacionRol) {
        this.thLiquidacionRol = thLiquidacionRol;
    }

}
