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
import com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSindicales;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThBeneficiosSindicalesService;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
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
@Named(value = "thBeneficiosSindicalesView")
@ViewScoped
public class ThBeneficiosSindicalesController implements Serializable {

    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThBeneficiosSindicalesService thBeneficiosSindicalesService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private LazyModel<ThBeneficiosSindicales> thBeneficiosSindicalesLazy;

    private List<Short> periodos;
    private List<ThTipoRol> tipoRolList;
    private List<CatalogoItem> listCatalogo;

    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRol;
    private ThConfig thConfig;
    private ThBeneficiosSindicales thBeneficiosSindicales;

    private Boolean btnLoadData, dataView;

    @PostConstruct
    public void init() {
        opcionBusqueda = new OpcionBusqueda();
        periodos = thInterfaces.getPeriodos();
        thBeneficiosSindicales = new ThBeneficiosSindicales();
        btnLoadData = Boolean.FALSE;
        listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
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
            thBeneficiosSindicalesLazy = new LazyModel<>(ThBeneficiosSindicales.class);
            thBeneficiosSindicalesLazy.getSorteds().put("idServidor.persona.apellido", "ASC");
            thBeneficiosSindicalesLazy.getFilterss().put("estado", true);
            thBeneficiosSindicalesLazy.getFilterss().put("idTipoRol", thTipoRol);
            thBeneficiosSindicalesLazy.setDistinct(false);
        } else {
            thBeneficiosSindicalesLazy = null;
            btnLoadData = Boolean.FALSE;
        }
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        thConfig = thConfigService.findCode(CONFIG.CONFIG_TH_BENEFICIOS_SINDICALES);
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

    public void delete(ThBeneficiosSindicales thBeneficiosSindicales) {
        if (thBeneficiosSindicales.getId() != null) {
            thBeneficiosSindicales.setEstado(Boolean.FALSE);
            thBeneficiosSindicales.setFechaModificacion(new Date());
            thBeneficiosSindicales.setUsuarioModificacion(thInterfaces.getUser());
            thBeneficiosSindicalesService.edit(thBeneficiosSindicales);
            PrimeFaces.current().ajax().update("thBeneficiosSindicalesTable");
            JsfUtil.addInformationMessage("INFO!!!", "Se elimino correctarente los datos de " + thBeneficiosSindicales.getIdServidor().getPersona().getNombreCompleltoSql());
        }
    }

    public void loadData() {
        String msj = findThConfig(true);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        int aux = thBeneficiosSindicalesService.loadData(opcionBusqueda.getAnio(), thInterfaces.getUser(), thTipoRol, thConfig.getCodConfig());
        JsfUtil.addSuccessMessage("INFO!!!", "Se ha actualizado correctamente la información");
        updateLazyModel();
        PrimeFaces.current().ajax().update("thBeneficiosSindicalesTable");
    }

    public void openDlgForm(ThBeneficiosSindicales thBeneficiosSindicales, Boolean view) {
        dataView = view;
        this.thBeneficiosSindicales = thBeneficiosSindicales;
        JsfUtil.executeJS("PF('thBeneficiosSindicalesDlg').show()");
        PrimeFaces.current().ajax().update("thBeneficiosSindicalesForm");
    }

    public void closeDlgForm() {
        if (thBeneficiosSindicales.getId() != null) {
            if (thBeneficiosSindicales.getValor() == null || thBeneficiosSindicales.getValor().doubleValue() <= 0) {
                JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar los valores correspondientes diferente o mayor a cero");
                return;
            }
            thBeneficiosSindicales.setFechaModificacion(new Date());
            thBeneficiosSindicales.setUsuarioModificacion(thInterfaces.getUser());
            thBeneficiosSindicalesService.edit(thBeneficiosSindicales);
            JsfUtil.addInformationMessage("INFO!!!", "Se ha actualizado correctamente la información");
        }
        JsfUtil.executeJS("PF('thBeneficiosSindicalesDlg').hide()");
        PrimeFaces.current().ajax().update("thBeneficiosSindicalesTable");
    }

    public LazyModel<ThBeneficiosSindicales> getThBeneficiosSindicalesLazy() {
        return thBeneficiosSindicalesLazy;
    }

    public void setThBeneficiosSindicalesLazy(LazyModel<ThBeneficiosSindicales> thBeneficiosSindicalesLazy) {
        this.thBeneficiosSindicalesLazy = thBeneficiosSindicalesLazy;
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

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
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

    public ThBeneficiosSindicales getThBeneficiosSindicales() {
        return thBeneficiosSindicales;
    }

    public void setThBeneficiosSindicales(ThBeneficiosSindicales thBeneficiosSindicales) {
        this.thBeneficiosSindicales = thBeneficiosSindicales;
    }

    public Boolean getBtnLoadData() {
        return btnLoadData;
    }

    public void setBtnLoadData(Boolean btnLoadData) {
        this.btnLoadData = btnLoadData;
    }

    public Boolean getDataView() {
        return dataView;
    }

    public void setDataView(Boolean dataView) {
        this.dataView = dataView;
    }

}
