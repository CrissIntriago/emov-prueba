/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author OrigamiEc
 */
@Named(value = "reporteVentasView")
@ViewScoped
public class reporteVentasController implements Serializable{

    @Inject
    private ServletSession servletSession;

    private Boolean parametrosFecha;
    private Date fechaDesde;
    private Date fechaHasta;
    
    private OpcionBusqueda busqueda;
    
    @PostConstruct
    public void initialize() {
        this.busqueda = new OpcionBusqueda();
        vaciarParametros();
    }
    
    public void vaciarParametros() {
        this.parametrosFecha = Boolean.TRUE;
        String fecha = "01-01-" + busqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaDesde = dateFormat.parse(fecha);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        this.fechaHasta = new Date();
    }
    
    public void generarReporte(String tipoArchivo) {
        if (!parametrosFecha) {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUtil.addWarningMessage("AVISO", "Revisar los parámetro del rango de las fechas");
                return;
            }
        }
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        servletSession.addParametro("fecha_desde", Utils.dateFormatPattern("yyyy-MM-dd", fechaDesde));
        servletSession.addParametro("fecha_hasta", Utils.dateFormatPattern("yyyy-MM-dd", fechaHasta));
        servletSession.addParametro("periodo", busqueda.getAnio());
        servletSession.setNombreReporte("reporteVentas");
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("tributacion");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        vaciarParametros();
    }
    

    public Boolean getParametrosFecha() {
        return parametrosFecha;
    }

    public void setParametrosFecha(Boolean parametrosFecha) {
        this.parametrosFecha = parametrosFecha;
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

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }
    
    
    
}
