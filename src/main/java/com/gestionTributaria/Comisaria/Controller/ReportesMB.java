/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Comisaria.Controller;

import com.gestionTributaria.Comisaria.Service.ComisariaServices;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class ReportesMB implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private ComisariaServices comisariaServices;
    @Inject
    private ManagerService service;
    @Inject
    private ServletSession ss;

    private Date fechaInicio = new Date();
    private Date fechaFina = new Date();

    private String tipoComisaria;
    private Long[] tipoLiquidaciones;

    @PostConstruct
    public void init() {
        tipoComisaria = "";
    }

    public void imprimir(boolean tipo) {
        /*
            172 ACTIVOS TOTALES
            177 PERMISO DE FUNCIONAMIENTO
            9 PATENTE ANUAL
            67 PATENTE ANUAL
         */
        if (!tipo) {
            ss.setContentType("xlsx");
        }
        ss.addParametro("fecha_inicio", fechaInicio);
        ss.addParametro("fecha_fin", fechaFina);
        ss.setNombreReporte("liquidacion_comisaria");
        ss.setNombreSubCarpeta("GestionTributatia/comisaria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirInd(boolean tipo) {
        /*
            172 ACTIVOS TOTALES
            177 PERMISO DE FUNCIONAMIENTO
            9 PATENTE ANUAL
            67 PATENTE ANUAL
        por cedula, predio, o cedula catastrala
         */
        if (!tipo) {
            ss.setContentType("xlsx");
        }
        ss.addParametro("fecha_inicio", fechaInicio);
        ss.addParametro("fecha_fin", fechaFina);
        ss.setNombreReporte("liquidacion_comisaria_ind");
        ss.setNombreSubCarpeta("GestionTributatia/comisaria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public ComisariaServices getComisariaServices() {
        return comisariaServices;
    }

    public void setComisariaServices(ComisariaServices comisariaServices) {
        this.comisariaServices = comisariaServices;
    }

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFina() {
        return fechaFina;
    }

    public void setFechaFina(Date fechaFina) {
        this.fechaFina = fechaFina;
    }

    public String getTipoComisaria() {
        return tipoComisaria;
    }

    public void setTipoComisaria(String tipoComisaria) {
        this.tipoComisaria = tipoComisaria;
    }

    public Long[] getTipoLiquidaciones() {
        return tipoLiquidaciones;
    }

    public void setTipoLiquidaciones(Long[] tipoLiquidaciones) {
        this.tipoLiquidaciones = tipoLiquidaciones;
    }
//</editor-fold>

}
