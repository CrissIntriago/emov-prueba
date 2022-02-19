/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThCauciones;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCaucionesService;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "thCaucionesView")
@ViewScoped
public class ThCaucionesController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThCaucionesService thCaucionesService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<ThCauciones> thCaucionesLazyModel;

    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;
    private List<CatalogoItem> listCatalogo;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThCauciones thCauciones;
    private ThConfig thConfig;

    private Boolean btnLoadData;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        thCauciones = new ThCauciones();
        btnLoadData = Boolean.FALSE;
        listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
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
            if (!thTipoRol.getAprobado()) {
                btnLoadData = Boolean.TRUE;
            }
            thCaucionesLazyModel = new LazyModel<>(ThCauciones.class);
            thCaucionesLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
            thCaucionesLazyModel.getFilterss().put("estado", true);
            thCaucionesLazyModel.getFilterss().put("idTipoRol", thTipoRol);
            thCaucionesLazyModel.setDistinct(false);
        } else {
            thCaucionesLazyModel = null;
            btnLoadData = Boolean.FALSE;
        }
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thConfigService.findCode(CONFIG.CONFIG_TH_CAUCIONES);
        if (thConfig == null) {
            mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
        } else {
            if (accion) {
                if (thConfig.getCodConfig() == null) {
                    mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado ";
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

    public void save() {
        thConfigService.edit(thConfig);
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha guardado la configuración");
        JsfUtil.executeJS("PF('thConfigDlg').hide()");
        PrimeFaces.current().ajax().update("thConfigForm");
    }

    public void openDlg() {
        thCauciones = new ThCauciones();
        updateTotalServidores();
        JsfUtil.executeJS("PF('thCaucionesDlg').show()");
        PrimeFaces.current().ajax().update("thCaucionesForm");
    }

    public void updateTotalServidores() {
        thCauciones.setCantidadServidores(thInterfaces.getCantidadServidores(thCauciones.getModoCalculo(), thTipoRol));
        if (thCauciones.getValorPrimaNeta() != null && thCauciones.getValorPrimaNeta().doubleValue() > 0) {
            if (thCauciones.getPorcentaje() != null && thCauciones.getPorcentaje().doubleValue() > 0) {
                Double base = (thCauciones.getValorPrimaNeta().doubleValue() * thCauciones.getPorcentaje().doubleValue()) / 100;
                thCauciones.setBaseImponible(new BigDecimal(base));
                thCauciones.setCuotaPropocional(calculoCuotaPropocional(thCauciones.getBaseImponible(), thCauciones.getCantidadServidores()));
            }
        }
    }

    public BigDecimal calculoCuotaPropocional(BigDecimal valor, int numservidores) {
        double total;
        if (numservidores == 0) {
            total = (valor.doubleValue() / 1) / 12;
        } else {
            total = (valor.doubleValue() / numservidores) / 12;
        }
        return new BigDecimal(total);
    }

    public void loadData() {
        String msj = findThConfig(true);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        if (thCauciones.getValorPrimaNeta() == null || thCauciones.getValorPrimaNeta().doubleValue() == 0) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar un valor prima neta");
            return;
        }
        if (thCauciones.getPorcentaje() == null || thCauciones.getPorcentaje().doubleValue() == 0) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar un porcentaje");
            return;
        }
        if (thCauciones.getBaseImponible() == null || thCauciones.getBaseImponible().doubleValue() == 0) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar un porcentaje");
            return;
        }
        if (thCauciones.getCuotaPropocional() == null || thCauciones.getCuotaPropocional().doubleValue() == 0) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe ingresar un porcentaje");
            return;
        }
        int aux = thCaucionesService.loadData(thCauciones, opcionBusqueda.getAnio(), thInterfaces.getUser(), thTipoRol, thConfig.getCodConfig());
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha actualizado correctamente la información");
        updateLazyModel();
        JsfUtil.executeJS("PF('thCaucionesDlg').hide()");
        PrimeFaces.current().ajax().update("thCaucionesTable");
    }

    public void delete(ThCauciones thCauciones) {
        if (thCauciones.getId() != null) {
            thCauciones.setEstado(Boolean.FALSE);
            thCauciones.setFechaModificacion(new Date());
            thCauciones.setUsuarioModifica(thInterfaces.getUser());
            thCaucionesService.edit(thCauciones);
            PrimeFaces.current().ajax().update("thCaucionesTable");
            JsfUtil.addInformationMessage("INFO!!!", "Se elimino correctarente los datos de " + thCauciones.getIdServidor().getPersona().getNombreCompleltoSql());
        }
    }

    public LazyModel<ThCauciones> getThCaucionesLazyModel() {
        return thCaucionesLazyModel;
    }

    public void setThCaucionesLazyModel(LazyModel<ThCauciones> thCaucionesLazyModel) {
        this.thCaucionesLazyModel = thCaucionesLazyModel;
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

    public ThCauciones getThCauciones() {
        return thCauciones;
    }

    public void setThCauciones(ThCauciones thCauciones) {
        this.thCauciones = thCauciones;
    }

    public Boolean getBtnLoadData() {
        return btnLoadData;
    }

    public void setBtnLoadData(Boolean btnLoadData) {
        this.btnLoadData = btnLoadData;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

}
