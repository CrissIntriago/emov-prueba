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
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.talento_humano.entities.ThBeneficiosSociales;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.services.ThBeneficiosSocialesService;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
import com.origami.sigef.resource.talento_humano.services.ThTipoRolService;
import java.io.Serializable;
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
@Named(value = "thBeneficiosSocialesView")
@ViewScoped
public class ThBeneficiosSocialesController implements Serializable {

    @Inject
    private ThBeneficiosSocialesService thBeneficiosSocialesService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThTipoRolService thTipoRolService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private ServletSession servletSession;

    private ThBeneficiosSociales thBeneficiosSociales;
    private OpcionBusqueda opcionBusqueda;
    private ThTipoRol thTipoRolSeleccionado;

    private LazyModel<ThBeneficiosSociales> thBeneficiosSocialesLazyModel;

    private List<ThTipoRol> thTipoRolList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> listCatalogo;

    private ThConfig thConfig;

    private int codeBeneficiacio;

    private Boolean view, btnreport;

    @PostConstruct
    public void init() {
        listaPeriodo = catalogoItemService.getPeriodo();
        opcionBusqueda = new OpcionBusqueda();
        actualizarTipoRol();
        codeBeneficiacio = 0;
        listCatalogo = catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
    }

    public void loadLazy() {
        if (thTipoRolSeleccionado != null) {
            findThConfig(false);
            thBeneficiosSocialesLazyModel = new LazyModel<>(ThBeneficiosSociales.class);
            thBeneficiosSocialesLazyModel.getSorteds().put("idServidor.persona.apellido", "ASC");
            switch (codeBeneficiacio) {
                case 1:
                    //decimoTercero
                    thBeneficiosSocialesLazyModel.getFilterss().put("decimoTercero", true);
                    break;
                case 2:
                    //decimoCuarto
                    thBeneficiosSocialesLazyModel.getFilterss().put("decimoCuarto", true);
                    break;
                case 3:
                    //fondoReserva
                    thBeneficiosSocialesLazyModel.getFilterss().put("fondosReserva", true);
                    break;
            }
            thBeneficiosSocialesLazyModel.getFilterss().put("estado", true);
            thBeneficiosSocialesLazyModel.getFilterss().put("idThTipoRol", thTipoRolSeleccionado);
            thBeneficiosSocialesLazyModel.setDistinct(false);
            activarReporte();
        } else {
            btnreport = Boolean.FALSE;
            thBeneficiosSocialesLazyModel = null;
            thConfig = null;
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un rol");
        }
    }

    private void activarReporte() {
        if (thConfig.getId() != null && thConfig.getCodConfig() != null && thTipoRolSeleccionado.getId() != null && codeBeneficiacio > 0) {
            btnreport = Boolean.TRUE;
        } else {
            btnreport = Boolean.FALSE;
        }
    }

    public void actualizarTipoRol() {
        if (opcionBusqueda.getAnio() == null) {
            thTipoRolList = null;
        } else {
            thTipoRolList = thTipoRolService.getRolAnio(opcionBusqueda.getAnio());
        }
    }

    public void loadData() {
        if (codeBeneficiacio == 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar el tipo de beneficio social");
            return;
        }
        if (opcionBusqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un periodo");
            return;
        }
        if (thTipoRolSeleccionado == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe selecionar un tipo de rol");
            return;
        }
        if (thTipoRolSeleccionado.getAprobado()) {
            JsfUtil.addWarningMessage("AVISO!!!", "no se puede actualizar la información debido al que el rol seleccionado ya fue aprobado");
            return;
        }
        String msj = findThConfig(true);
        if (!msj.equals("")) {
            JsfUtil.addWarningMessage("AVISO!!!", msj);
            return;
        }
        if (thBeneficiosSocialesService.getFindData(thTipoRolSeleccionado)) {
            create();
        } else {
            thBeneficiosSocialesService.delete(thTipoRolSeleccionado, codeBeneficiacio);
            create();
        }
        loadLazy();
        activarReporte();
        PrimeFaces.current().ajax().update("thBeneficioSocialTable");
        PrimeFaces.current().ajax().update("gridReporte");
        JsfUtil.addSuccessMessage("INFO!!!", "Se han cargado los datos correctamente");
    }

    private void create() {
        switch (codeBeneficiacio) {
            case 1:
                //decimoTercero
                thBeneficiosSocialesService.createBlock(thTipoRolSeleccionado.getId(), true, false, false, userSession.getNameUser(), thConfig.getCodConfig(), codeBeneficiacio);
                break;
            case 2:
                //decimoCuarto
                thBeneficiosSocialesService.createBlock(thTipoRolSeleccionado.getId(), false, true, false, userSession.getNameUser(), thConfig.getCodConfig(), codeBeneficiacio);
                break;
            case 3:
                //fondoReserva
                thBeneficiosSocialesService.createBlock(thTipoRolSeleccionado.getId(), false, false, true, userSession.getNameUser(), thConfig.getCodConfig(), codeBeneficiacio);
                break;
        }
    }

    private String findThConfig(boolean accion) {
        String mensaje = "";
        switch (codeBeneficiacio) {
            case 1:
                thConfig = thConfigService.findCode(CONFIG.CONFIG_DECIMO_TERCERO);
                if (thConfig == null) {
                    mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
                } else {
                    if (accion) {
                        if (thConfig.getCodConfig() == null) {
                            mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado ";
                        }
                    }
                }
                break;
            case 2:
                thConfig = thConfigService.findCode(CONFIG.CONFIG_DECIMO_CUARTO);
                if (thConfig == null) {
                    mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
                } else {
                    if (accion) {
                        if (thConfig.getCodConfig() == null) {
                            mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado";

                        }
                    }
                }
                break;

            case 3:
                thConfig = thConfigService.findCode(CONFIG.CONFIG_FONDOS_RESERVA);
                if (thConfig == null) {
                    mensaje = "No existe los parametros ingresados en la tabla de configuración de talento humano, comuniquese con el administrador";
                } else {
                    if (accion) {
                        if (thConfig.getCodConfig() == null) {
                            mensaje = "No existe la configuración para cargar los rubros en el rol seleccionado";
                        }
                    }
                }
                break;
            default:
                break;
        }
        return mensaje;
    }

    public void form(ThBeneficiosSociales thBeneficiosSociales, boolean view) {
        this.view = view;
        this.thBeneficiosSociales = thBeneficiosSociales;
        JsfUtil.executeJS("PF('thBeneficioSocialDlg').show()");
        PrimeFaces.current().ajax().update("thBeneficioSocialForm");
    }

    public void edit() {
        thBeneficiosSociales.setUserModificacion(userSession.getNameUser());
        thBeneficiosSociales.setFechaModificacion(new Date());
        thBeneficiosSocialesService.edit(thBeneficiosSociales);
        JsfUtil.executeJS("PF('thBeneficioSocialDlg').hide()");
        PrimeFaces.current().ajax().update("thBeneficioSocialTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha editado correctamente");
    }

    public void delete(ThBeneficiosSociales thBeneficiosSociales) {
        thBeneficiosSociales.setUserModificacion(userSession.getNameUser());
        thBeneficiosSociales.setFechaModificacion(new Date());
        thBeneficiosSociales.setEstado(false);
        thBeneficiosSocialesService.edit(thBeneficiosSociales);
        PrimeFaces.current().ajax().update("thBeneficioSocialTable");
        JsfUtil.addInformationMessage("INFO!!!", "Se ha eliminado correctamente");
    }

    public void openDlgConfig() {
        if (codeBeneficiacio == 0) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un tipo de beneficio social");
            return;
        }
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

    public void report(String typeReport,Integer codTipo) {
        servletSession.addParametro("cod_report", codeBeneficiacio);
        servletSession.addParametro("cod_tipo", codTipo);
        servletSession.addParametro("id_tipo_rol", thTipoRolSeleccionado.getId());
        servletSession.addParametro("nombre_reporte", thConfig.getCodConfig());
        servletSession.addParametro("mes", thTipoRolSeleccionado.getIdMes().getTexto().toUpperCase());
        servletSession.addParametro("periodo", thTipoRolSeleccionado.getPeriodo());
        servletSession.setContentType(typeReport);
        servletSession.setNombreReporte("beneficio_sociales");
        servletSession.setNombreSubCarpeta("_talento_humano");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ThBeneficiosSociales> getThBeneficiosSocialesLazyModel() {
        return thBeneficiosSocialesLazyModel;
    }

    public void setThBeneficiosSocialesLazyModel(LazyModel<ThBeneficiosSociales> thBeneficiosSocialesLazyModel) {
        this.thBeneficiosSocialesLazyModel = thBeneficiosSocialesLazyModel;
    }

    public List<ThTipoRol> getThTipoRolList() {
        return thTipoRolList;
    }

    public void setThTipoRolList(List<ThTipoRol> thTipoRolList) {
        this.thTipoRolList = thTipoRolList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public int getCodeBeneficiacio() {
        return codeBeneficiacio;
    }

    public void setCodeBeneficiacio(int codeBeneficiacio) {
        this.codeBeneficiacio = codeBeneficiacio;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public ThTipoRol getThTipoRolSeleccionado() {
        return thTipoRolSeleccionado;
    }

    public void setThTipoRolSeleccionado(ThTipoRol thTipoRolSeleccionado) {
        this.thTipoRolSeleccionado = thTipoRolSeleccionado;
    }

    public ThBeneficiosSociales getThBeneficiosSociales() {
        return thBeneficiosSociales;
    }

    public void setThBeneficiosSociales(ThBeneficiosSociales thBeneficiosSociales) {
        this.thBeneficiosSociales = thBeneficiosSociales;
    }

    public ThConfig getThConfig() {
        return thConfig;
    }

    public void setThConfig(ThConfig thConfig) {
        this.thConfig = thConfig;
    }

    public List<CatalogoItem> getListCatalogo() {
        return listCatalogo;
    }

    public void setListCatalogo(List<CatalogoItem> listCatalogo) {
        this.listCatalogo = listCatalogo;
    }

    public Boolean getBtnreport() {
        return btnreport;
    }

    public void setBtnreport(Boolean btnreport) {
        this.btnreport = btnreport;
    }

}
