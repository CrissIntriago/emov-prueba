/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.ActivoFijoCustodioService;
import com.origami.sigef.activos.service.ActivoFijoServidorService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "bienReporteView")
@ViewScoped
public class BienReporteController implements Serializable {

    /*Inject*/
    @Inject
    private ServletSession servletSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ActivoFijoCustodioService activoFijoCustodioService;
    @Inject
    private ActivoFijoServidorService activoFijoServidorService;


    /*Objetos*/
    private Servidor custodioSeleccionado;
    private OpcionBusqueda opcionBusqueda;
    /*Variables auxiliares*/
    private String estadoBien;
    private String unidadAdministrativa;

    /*Lista*/
    private List<Servidor> listaDeCustodio;
    private List<UnidadAdministrativa> listaDeDepartamento;
    private List<CatalogoItem> listaDeTiposDeEstado;
    private Date fechadesde, fechahasta;

    @PostConstruct
    public void initialize() {
        this.custodioSeleccionado = new Servidor();
        this.opcionBusqueda = new OpcionBusqueda();
        this.listaDeCustodio = activoFijoCustodioService.getCustodios();
        this.listaDeDepartamento = activoFijoCustodioService.getDepartamentos();
        this.listaDeTiposDeEstado = catalogoItemService.findCatalogoItems("estado_bien");
        try {
            fechadesde = Utils.getPrimerDiaAnio(Utils.getAnio(new Date()));
            fechahasta = new Date();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void imprimirPorCustodio(String tipoReporte) {
        if (custodioSeleccionado != null) {
            if (activoFijoServidorService.verificarBienesCustodio(custodioSeleccionado)) {
                JsfUtil.addWarningMessage("AVISO", "No hay bienes asignados al custodio seleccionado");
            } else {
                servletSession.addParametro("id_servidor", custodioSeleccionado.getId());
                servletSession.addParametro("periodo", opcionBusqueda.getAnio());
                servletSession.setNombreReporte("ReportePorCustodio");
                servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
                /*se guarda el tipo de reporte a generar*/
                servletSession.setContentType(tipoReporte);
                if (tipoReporte.equalsIgnoreCase("xlsx")) {
                    servletSession.setOnePagePerSheet(true);
                }
                restablecerFormulario();
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
        } else {
            JsfUtil.addWarningMessage("AVISO", "Se debe seleccionar un custodio antes de generar el reporte");
        }
    }

    public void imprimirPorDepartamento(String tipoReporte) {
        /*COMPARA SI SE SELECCIONO UNA OPCION */
        if (unidadAdministrativa == null) {
            JsfUtil.addWarningMessage("REPORTE POR UNIDAD", "Se debe seleccionar una unidad administrativa antes de generar el reporte");
        } else {
            if (unidadAdministrativa.equals("TODOS")) {
                servletSession.addParametro("unidad_administrativa", "");
                servletSession.addParametro("name_reporte", "TODOS LOS BIENES DE LAS UNIDADES ADMINISTRATIVAS");
            } else {
                servletSession.addParametro("name_reporte", "BIENES POR UNIDAD ADMINISTRATIVA");
                if (activoFijoServidorService.verificarBienesDepartamento(unidadAdministrativa)) {
                    JsfUtil.addWarningMessage("REPORTE POR UNIDAD", "No se puede generar el reporte porque no existen Bienes asignados a la unidad seleccionada");
                    return;
                } else {
                    servletSession.addParametro("unidad_administrativa", unidadAdministrativa);
                }
            }
            servletSession.setNombreReporte("ReportePorDepartamento");
            servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
            servletSession.addParametro("periodo", opcionBusqueda.getAnio());
            servletSession.setContentType(tipoReporte);
            if (tipoReporte.equals("xlsx")) {
                servletSession.setOnePagePerSheet(true);
            }
            restablecerFormulario();
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void imprimirPorEstado(String tipoReporte) {
        /*COMPARA SI SE SELECCIONO UNA OPCION */
        if (estadoBien == null) {
            JsfUtil.addWarningMessage("REPORTE POR ESTADO", "Se debe seleccionar un estado del bien antes de generar el reporte");
        } else {
            if (estadoBien.equals("TODOS")) {
                servletSession.addParametro("estado_bien", "");
                servletSession.addParametro("name_reporte", "TODOS LOS BIENES POR ESTADO");
            } else {
                servletSession.addParametro("name_reporte", "BIENES POR ESTADO");
                if (activoFijoServidorService.verificarEstadoBienes(getEstadoBien())) {
                    servletSession.addParametro("estado_bien", estadoBien);
                } else {
                    JsfUtil.addWarningMessage("REPORTE POR ESTADO", "No hay bienes asignados con el estado: " + estadoBien);
                    return;
                }
            }
            servletSession.setNombreReporte("ReportePorEstado");
            servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
            servletSession.addParametro("periodo", opcionBusqueda.getAnio());
            servletSession.setContentType(tipoReporte);
            if (tipoReporte.equals("xlsx")) {
                servletSession.setOnePagePerSheet(true);
            }
            restablecerFormulario();
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void imprimitEstadoPeriodo(String tipoReporte) {
        if (fechadesde.compareTo(fechahasta) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha DESDE no puede ser mayor a la fecha HASTA");
            return;
        }
        if (estadoBien.equals("TODOS")) {
            servletSession.addParametro("estado_bien", "");
            servletSession.addParametro("name_reporte", "TODOS LOS BIENES POR ESTADO");
        } else {
            servletSession.addParametro("name_reporte", "BIENES POR ESTADO");
            if (activoFijoServidorService.verificarEstadoBienes(getEstadoBien())) {
                servletSession.addParametro("estado_bien", estadoBien);
            } else {
                JsfUtil.addWarningMessage("REPORTE POR ESTADO", "No hay bienes asignados con el estado: " + estadoBien);
                return;
            }
        }
        servletSession.setNombreReporte("ReporteEstadoPeriodo");
        servletSession.setNombreSubCarpeta("ActivoFijoCustodio");
        servletSession.addParametro("fecha_desde", fechadesde);
        servletSession.addParametro("fecha_hasta", fechahasta);
        servletSession.setContentType(tipoReporte);

        if (tipoReporte.equals("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        restablecerFormulario();
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void restablecerFormulario() {
        this.custodioSeleccionado = new Servidor();
        this.estadoBien = null;
        this.unidadAdministrativa = null;
    }

    //<editor-fold defaultstate="collapsed" desc="GET - SET">
    public Servidor getCustodioSeleccionado() {
        return custodioSeleccionado;
    }

    public void setCustodioSeleccionado(Servidor custodioSeleccionado) {
        this.custodioSeleccionado = custodioSeleccionado;
    }

    public List<Servidor> getListaDeCustodio() {
        return listaDeCustodio;
    }

    public void setListaDeCustodio(List<Servidor> listaDeCustodio) {
        this.listaDeCustodio = listaDeCustodio;
    }

    public List<UnidadAdministrativa> getListaDeDepartamento() {
        return listaDeDepartamento;
    }

    public void setListaDeDepartamento(List<UnidadAdministrativa> listaDeDepartamento) {
        this.listaDeDepartamento = listaDeDepartamento;
    }

    public List<CatalogoItem> getListaDeTiposDeEstado() {
        return listaDeTiposDeEstado;
    }

    public void setListaDeTiposDeEstado(List<CatalogoItem> listaDeTiposDeEstado) {
        this.listaDeTiposDeEstado = listaDeTiposDeEstado;
    }

    public String getEstadoBien() {
        return estadoBien;
    }

    public void setEstadoBien(String estadoBien) {
        this.estadoBien = estadoBien;
    }

    public String getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(String unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }
}
//</editor-fold>

