/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.service.RubroTipoService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contRetencionesContablesView")
@ViewScoped
public class ContRetencionesContablesController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private RubroTipoService rubroTipoService;

    private Boolean parametrosFecha;
    private Boolean parametroEstado;
    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoSeleccionado;

    private OpcionBusqueda opcionBusqueda;

    private List<String> tiposRegistrados;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        vaciarParametros();
    }

    public void generarReporte(String tipoArchivo) {
        if (!parametrosFecha) {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUtil.addWarningMessage("AVISO", "Revisar los parámetro del rango de las fechas");
                return;
            }
        }
        if (tipoSeleccionado == null) {
            tipoSeleccionado = "";
        }
        servletSession.addParametro("fecha_inicio", fechaDesde);
        servletSession.addParametro("str_fecha_inicio", Utils.dateFormatPattern("dd-MM-yyyy", fechaDesde));
        servletSession.addParametro("fecha_fin", fechaHasta);
        servletSession.addParametro("str_fecha_fin", Utils.dateFormatPattern("dd-MM-yyyy", fechaHasta));
        servletSession.addParametro("tipo", tipoSeleccionado);
        servletSession.setNombreReporte("retenciones_contables");
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_contabilidad");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        vaciarParametros();
    }

    public void vaciarParametros() {
        this.tiposRegistrados= rubroTipoService.getTipos("RETENCION");
        this.parametrosFecha = Boolean.TRUE;
        this.parametroEstado = Boolean.TRUE;
        String fecha = "01-01-" + opcionBusqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaDesde = dateFormat.parse(fecha);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        this.fechaHasta = new Date();
        this.tipoSeleccionado = "";
    }

    public Boolean getParametrosFecha() {
        return parametrosFecha;
    }

    public void setParametrosFecha(Boolean parametrosFecha) {
        this.parametrosFecha = parametrosFecha;
    }

    public Boolean getParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(Boolean parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<String> getTiposRegistrados() {
        return tiposRegistrados;
    }

    public void setTiposRegistrados(List<String> tiposRegistrados) {
        this.tiposRegistrados = tiposRegistrados;
    }
    
    
}
