/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Models.SolicitudServiciosDTO;
import com.ventanilla.Services.ReporteVentanillaService;
import com.ventanilla.Services.SolicitudServiciosService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ricardo
 */
@Named
@ViewScoped
public class ReporteVentanillaMB implements Serializable {

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;
    @Inject
    private ReporteVentanillaService reporteVentanillaService;
    @Inject
    private SolicitudServiciosService solicitudServiciosService;
    private Date fechaDesde, fechaHasta;
    private LocalDateTime localTime;
    private String tipoReporte;
    private List<SolicitudServicios> solicitudServiciosReporte;
    private List<SolicitudServiciosDTO> solicitudServiciosDTO;

    @PostConstruct
    public void initView() {
        loadData();
    }

    private void loadData() {
        fechaDesde = Utils.obtenerElPrimerDiaDelMes("yyyy-MM-dd", Calendar.DAY_OF_MONTH);
        fechaHasta = new Date();
        solicitudServiciosReporte = new ArrayList<>();
        solicitudServiciosDTO = new ArrayList<>();
    }

    public void imprimirReporte() {
        ss.borrarDatos();
        ss.borrarParametros();
        String nombreReporte = "";
        solicitudServiciosReporte = new ArrayList<>();
        solicitudServiciosDTO = new ArrayList<>();
        if (Utils.isEmptyString(tipoReporte)) {
            JsfUtil.addErrorMessage("", "Debe escoger el tipo de búsqueda");
            return;
        }
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("", "Escoja una fecha para realizar la búsqueda");
            return;
        }
        switch (tipoReporte) {
            case "rango_fecha":
                nombreReporte = "reporteVentanilla";
                solicitudServiciosReporte = reporteVentanillaService.findByNamedQuery("SolicitudServicios.findByFechaBetween", fechaDesde, fechaHasta);
                break;
            case "tramite":
                nombreReporte = "reporteVentanillaAgrupado";
                solicitudServiciosReporte = reporteVentanillaService.findByNamedQuery("SolicitudServicios.findByFechaBetween", fechaDesde, fechaHasta);
                break;
            case "consolidado":
                nombreReporte = "reporteVentanillaConsolidado";
                solicitudServiciosDTO = solicitudServiciosService.findAllSolicitudGroupByServicio(fechaDesde, fechaHasta);
                break;
            case "analista":
                nombreReporte = "reporteVentanillaAnalista";
                solicitudServiciosReporte = reporteVentanillaService.findByNamedQuery("SolicitudServicios.findByFechaBetweenOrderByUsuario", fechaDesde, fechaHasta);
                break;
            case "juridico":
                break;
        }
        ss.setDataSource(tipoReporte.equals("consolidado") ? solicitudServiciosDTO : solicitudServiciosReporte);
        ss.setNombreSubCarpeta("ventanilla");
        ss.setNombreReporte(nombreReporte);
        System.out.println("nombreReporte " + nombreReporte + " size " + solicitudServiciosReporte.size());
        ss.addParametro("usuario_emite", session.getNameUser());
        JsfUtil.update("formMain");
    }

    public Boolean renderedReport() {
        if (!Utils.isEmptyString(tipoReporte) && tipoReporte.equals("consolidado") && !Utils.isEmpty(solicitudServiciosDTO)) {
            return Boolean.TRUE;
        }
        if (!Utils.isEmptyString(tipoReporte) && !tipoReporte.equals("consolidado") && !Utils.isEmpty(solicitudServiciosReporte)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public LocalDateTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalDateTime localTime) {
        this.localTime = localTime;
    }

    public List<SolicitudServicios> getSolicitudServiciosReporte() {
        return solicitudServiciosReporte;
    }

    public void setSolicitudServiciosReporte(List<SolicitudServicios> solicitudServiciosReporte) {
        this.solicitudServiciosReporte = solicitudServiciosReporte;
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

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
//</editor-fold>
}
