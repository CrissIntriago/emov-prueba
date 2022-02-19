/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.Presupuesto.Model.GrupoPresupuestoModel;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import java.io.Serializable;
import java.util.Calendar;
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
@Named(value = "validadorContPresupuestoView")
@ViewScoped
public class ValidadorContablePresupuestarioController implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoPresupuestoService catalogoPresupuestoService;

    private Boolean parametrosFecha;
    private Boolean grupoPresupuestario;

    private Date fechaDesde;
    private Date fechaHasta;

    private String tipoSeleccionado;
    private String tipoArchivo;

    private OpcionBusqueda opcionBusqueda;

    private Short periodoSeleccionado;

    private List<MasterCatalogo> periodos;
    private List<GrupoPresupuestoModel> grupoPresupuestoList;

    private String grupoSeleccionado;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        vaciarParametros();
    }

    public void generarReporte(String tipoArchivo) {
        /*Validar Fechas*/
        if (!parametrosFecha) {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUtil.addWarningMessage("AVISO", "Revisar los parámetro del rango de las fechas");
                return;
            }
        }
        /*Validar Tipo de reporte*/
        if (tipoSeleccionado == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Debe seleccionar uno de los tipos de reportes a generar");
            return;
        }
        /*Imprimir reporte seleccionado*/
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        String nombreReporte = "";
        switch (tipoSeleccionado) {
            case "1":
                nombreReporte = "ValidacionCtaContablePartidaPresupuestaria";
                break;
            case "2":
                nombreReporte = "ValidacionContablePresupuestoDevengado";
                Integer sendTitulo = 0;
                Integer sendNaturaleza = 0;
                if (grupoSeleccionado != null && !grupoPresupuestario) {
                    sendTitulo = Integer.parseInt(grupoSeleccionado.substring(0, 1));
                    sendNaturaleza = Integer.parseInt(grupoSeleccionado.substring(1));
                } else {
                    grupoPresupuestario = true;
                }
                servletSession.addParametro("grupoPresupuestario", grupoPresupuestario);
                servletSession.addParametro("titulo", sendTitulo);
                servletSession.addParametro("naturaleza", sendNaturaleza);
                break;
            case "3":
                nombreReporte = "ValidacionContablePresupuestoEjecutado_DIARIO";
                break;
            case "4":
                nombreReporte = "ValidacionContablePresupuestoEjecutado_CP";
                break;
        }
        servletSession.setNombreReporte(nombreReporte);
        servletSession.addParametro("fecha_desde", fechaDesde);
        servletSession.addParametro("fecha_hasta", fechaHasta);
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        vaciarParametros();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void vaciarParametros() {
        this.parametrosFecha = Boolean.TRUE;
        this.tipoSeleccionado = "";
        this.tipoArchivo = "";
        this.grupoPresupuestario = Boolean.TRUE;
        this.grupoSeleccionado = null;
        this.opcionBusqueda = new OpcionBusqueda();
        asignarRangoPeriodos();
    }

    public void asignarRangoPeriodos() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(opcionBusqueda.getAnio(), 0, 1);
        fechaDesde = calendar.getTime();
        if (opcionBusqueda.getAnio().intValue() == Utils.getAnio(new Date()).intValue()) {
            fechaHasta = new Date();
        } else {
            calendar.set(opcionBusqueda.getAnio(), 11, 31);
            fechaHasta = calendar.getTime();
        }
        grupoPresupuestoList = catalogoPresupuestoService.getListadoGruposPresupuestio(opcionBusqueda.getAnio());
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

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public Short getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(Short periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public Boolean getGrupoPresupuestario() {
        return grupoPresupuestario;
    }

    public void setGrupoPresupuestario(Boolean grupoPresupuestario) {
        this.grupoPresupuestario = grupoPresupuestario;
    }

    public String getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(String grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public List<GrupoPresupuestoModel> getGrupoPresupuestoList() {
        return grupoPresupuestoList;
    }

    public void setGrupoPresupuestoList(List<GrupoPresupuestoModel> grupoPresupuestoList) {
        this.grupoPresupuestoList = grupoPresupuestoList;
    }

}
