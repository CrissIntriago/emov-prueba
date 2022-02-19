/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThRetencionesImpuestoRenta;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThRetencionesImpuestoRentaService;
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
 * @author Jonathan Choez
 */
@Named(value = "thRetencionesImpuestoRentaView")
@ViewScoped
public class ThRetencionesImpuestoRentaController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThRetencionesImpuestoRentaService thRetencionesImpuestoRentaService;
    @Inject
    private ServletSession servletSession;

    private LazyModel<ThRetencionesImpuestoRenta> thRetencionesImpuestoRentaLazy;

    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;
    private List<CatalogoItem> listCatalogo;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThConfig thConfig;

    private Boolean btnCargarDatos;

    @PostConstruct
    public void init() {
        btnCargarDatos = true;
        periodos = thInterfaces.getPeriodos();
        opcionBusqueda = new OpcionBusqueda();
        findThConfig(true);
        updateTipoRolList();
        listCatalogo = thInterfaces.listaRubrosEgreso();
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
            btnCargarDatos = !thTipoRol.getAprobado();
            thRetencionesImpuestoRentaLazy = new LazyModel<>(ThRetencionesImpuestoRenta.class);
            thRetencionesImpuestoRentaLazy.getSorteds().put("servidor.persona.apellido", "ASC");
            thRetencionesImpuestoRentaLazy.getFilterss().put("estado", true);
            thRetencionesImpuestoRentaLazy.getFilterss().put("idTipoRol", thTipoRol);
            thRetencionesImpuestoRentaLazy.setDistinct(false);
        } else {
            thRetencionesImpuestoRentaLazy = null;
            btnCargarDatos = true;
        }
        PrimeFaces.current().ajax().update("thRetencionesImpuestoRentaTable");
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thInterfaces.findThConfig(CONFIG.CONFIG_TH_IMPUESTO_RENTA);
        if (thConfig == null) {
            mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
        } else {
            if (accion) {
                if (thConfig.getCodConfig() == null) {
                    mensaje = "No existe la configuración para cargar los rubros";
                }
            }
        }
        return mensaje;
    }

    public void openDlgConfig() {
        String msj = findThConfig(false);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        JsfUtil.executeJS("PF('thConfigDlg').show()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public void saveConfig() {
        thInterfaces.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public void loadData() {
        if (thConfig == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "No existe una configuracón necesaria para el calculo del impuesto a la renta");
            return;
        }
        if (thConfig.getCodConfig() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "En la configuración debe asociar un rubro antes de cargar los datos");
            return;
        }
        if (thRetencionesImpuestoRentaService.loadData(thTipoRol, thConfig.getCodConfig(), thInterfaces.getUser(), new Date())) {
            JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado los datos correctamente");
            updateLazyModel();
        } else {
            JsfUtil.addSuccessMessage("INFO!!!", "No se puede cargar la información debido a que no hay servidores que tengan asociado al rubro seleccionado en la configuración");
        }
    }

    public void edit(ThRetencionesImpuestoRenta thRetencionesImpuestoRenta, Boolean accion) {
        if (thRetencionesImpuestoRenta != null) {
            thRetencionesImpuestoRenta.setEstado(accion);
            thRetencionesImpuestoRenta.setUsuarioModifica(thInterfaces.getUser());
            thRetencionesImpuestoRenta.setFechaModifica(new Date());
            thRetencionesImpuestoRentaService.edit(thRetencionesImpuestoRenta);
        }
        if (accion) {
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha eliminado correctamente la información");
        } else {
            JsfUtil.addSuccessMessage("INFO!!!", "Se ha cambiado correctamente la información");
        }
        PrimeFaces.current().ajax().update("thRetencionesImpuestoRentaTable");
    }

    public void printReport(String tipoDocumento, Boolean accion) {
        servletSession.addParametro("id_tipo_rol", thTipoRol.getId());
        servletSession.addParametro("parametro", accion);
        servletSession.setNombreReporte("retencion_impuesto_renta");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public LazyModel<ThRetencionesImpuestoRenta> getThRetencionesImpuestoRentaLazy() {
        return thRetencionesImpuestoRentaLazy;
    }

    public void setThRetencionesImpuestoRentaLazy(LazyModel<ThRetencionesImpuestoRenta> thRetencionesImpuestoRentaLazy) {
        this.thRetencionesImpuestoRentaLazy = thRetencionesImpuestoRentaLazy;
    }

    public List<Short> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<Short> periodos) {
        this.periodos = periodos;
    }

    public List<ThTipoRol> getTipoRolList() {
        return tipoRolList;
    }

    public void setTipoRolList(List<ThTipoRol> tipoRolList) {
        this.tipoRolList = tipoRolList;
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

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

    public Boolean getBtnCargarDatos() {
        return btnCargarDatos;
    }

    public void setBtnCargarDatos(Boolean btnCargarDatos) {
        this.btnCargarDatos = btnCargarDatos;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

}
