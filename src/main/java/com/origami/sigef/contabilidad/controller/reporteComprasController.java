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
import com.origami.sigef.tesoreria.service.LiquidacionService;
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
@Named(value = "reporteComprasView")
@ViewScoped
public class reporteComprasController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private LiquidacionService liquidacionService;

    private Boolean parametrosFecha;
    private Boolean parametrosEstado;
    private Date fechaDesde;
    private Date fechaHasta;
    private String estadoSeleccionado;

    private OpcionBusqueda opcionBusqueda;

    private List<String> estadosRegistrados;

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
        if (!parametrosEstado) {
            if (estadoSeleccionado == null) {
                JsfUtil.addWarningMessage("AVISO", "Debe selecionar un estado");
                return;
            }
        }
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        if (estadoSeleccionado == null) {
            estadoSeleccionado = "";
        }
        servletSession.addParametro("fecha_desde", Utils.dateFormatPattern("yyyy-MM-dd", fechaDesde));
        servletSession.addParametro("fecha_hasta", Utils.dateFormatPattern("yyyy-MM-dd", fechaHasta));
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.addParametro("estado", estadoSeleccionado);
        servletSession.setNombreReporte("reporteCompras");
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("tributacion");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        vaciarParametros();
    }

    public void vaciarParametros() {
        this.parametrosFecha = Boolean.TRUE;
        this.parametrosEstado = Boolean.TRUE;
        String fecha = "01-01-" + opcionBusqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaDesde = dateFormat.parse(fecha);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        this.fechaHasta = new Date();
        this.estadoSeleccionado = "";
        estadosRegistrados = liquidacionService.getEstadosLiquidacion();
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

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Boolean getParametrosEstado() {
        return parametrosEstado;
    }

    public void setParametrosEstado(Boolean parametrosEstado) {
        this.parametrosEstado = parametrosEstado;
    }

    public String getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public void setEstadoSeleccionado(String estadoSeleccionado) {
        this.estadoSeleccionado = estadoSeleccionado;
    }

    public List<String> getEstadosRegistrados() {
        return estadosRegistrados;
    }

    public void setEstadosRegistrados(List<String> estadosRegistrados) {
        this.estadosRegistrados = estadosRegistrados;
    }

}
