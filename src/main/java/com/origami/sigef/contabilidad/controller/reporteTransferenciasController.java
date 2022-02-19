/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.BeneficiarioTransferenciasModel;
import com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle;
import com.origami.sigef.resource.contabilidad.services.ContTransferenciasDetalleService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 * @author Luis Pozo Gonzaabay
 */
@Named(value = "reporteTransferenciasView")
@ViewScoped
public class reporteTransferenciasController implements Serializable {

    @Inject
    private ServletSession servletSession;
//    @Inject
//    private DetalleTransferenciasService detalleTransferenciaService;
    @Inject
    private ContTransferenciasDetalleService contTransferenciasDetalleService;
            
    @Inject
    private CatalogoItemService catalogoItemService;

    private Boolean parametrosFecha;
    private Boolean parametrosNumComprobante;
    private Boolean parametroBeneficiario;
    private Boolean parametroEstado;
    private Date fechaDesde;
    private Date fechaHasta;
    private int numDesde;
    private int numHasta;
    private String estadoSeleccionado;
    private String beneficiarioSeleccionado;

    private OpcionBusqueda opcionBusqueda;
//    private DetalleTransferencias ultimaTransferencia;
    private ContTransferenciasDetalle ultimaTransferencia;

    private List<String> estadosRegistrados;
    private List<BeneficiarioTransferenciasModel> beneficiariosRegistrados;
    private List<Short> listaPeriodo;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        listaPeriodo = catalogoItemService.getPeriodo();
        vaciarParametros();
    }

    public void generarReporte(String tipoArchivo) {
        /*VALIDACION DE LOS PARAMETROS*/
        if (ultimaTransferencia == null) {
            JsfUtil.addErrorMessage("ERROR", "No hay ningún Pago Registrado");
            return;
        }
        if (numHasta > ultimaTransferencia.getReferencia().intValue()) {
            JsfUtil.addWarningMessage("AVISO", "La última Transferencia es el No." + ultimaTransferencia.getReferencia().intValue());
            return;
        }
        if (!parametrosFecha) {
            if (fechaDesde == null || fechaHasta == null) {
                JsfUtil.addWarningMessage("AVISO", "Revisar los parámetro del rango de las fechas");
                return;
            }
        }
        if (!parametrosNumComprobante) {
            if (numDesde == 0 || numHasta == 0) {
                JsfUtil.addWarningMessage("AVISO", "Revisar los parámetro del rango de los No. de los Comprobantes");
                return;
            }
        }
        if (!parametroBeneficiario) {
            if (beneficiarioSeleccionado == null) {
                JsfUtil.addWarningMessage("AVISO", "Debe selecionar un beneficiario");
                return;
            }
        }
        if (!parametroEstado) {
            if (estadoSeleccionado == null) {
                JsfUtil.addWarningMessage("AVISO", "Debe selecionar un estado");
                return;
            }
        }
        /*FORMATO DEL REPORTE*/
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        if (beneficiarioSeleccionado == null) {
            beneficiarioSeleccionado = "";
        }
        if (estadoSeleccionado == null) {
            estadoSeleccionado = "";
        }
        /*ADD PARAMETROS AL REPOSTE*/
        if (fechaDesde != null) {
            servletSession.addParametro("fecha_desde", Utils.dateFormatPattern("yyyy-MM-dd", fechaDesde));
        } else {
            servletSession.addParametro("fecha_desde", "");
        }
        if (fechaHasta != null) {
            servletSession.addParametro("fecha_hasta", Utils.dateFormatPattern("yyyy-MM-dd", fechaHasta));
        } else {
            servletSession.addParametro("fecha_hasta", "");
        }
        servletSession.addParametro("identificacion_beneficiario", beneficiarioSeleccionado);
        servletSession.addParametro("estado_transferencia", estadoSeleccionado);
        servletSession.addParametro("num_desde", numDesde);
        servletSession.addParametro("num_hasta", numHasta);
        servletSession.addParametro("periodo", opcionBusqueda.getAnio());
        servletSession.setNombreReporte("reporteDeTransferencias");
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("_tesoreria");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void vaciarParametros() {
        this.parametrosFecha = Boolean.TRUE;
        this.parametrosNumComprobante = Boolean.TRUE;
        this.parametroBeneficiario = Boolean.TRUE;
        this.parametroEstado = Boolean.TRUE;
        actualizarEntidadesPorPeriodo();
        this.estadoSeleccionado = "";
        this.beneficiarioSeleccionado = "";
    }

    
    public void actualizarEntidadesPorPeriodo(){
        this.ultimaTransferencia = contTransferenciasDetalleService.getLastContTransferenciasDetalleByPeriodo(opcionBusqueda.getAnio());
        if (ultimaTransferencia != null) {
            this.numDesde = 1;
            this.numHasta = ultimaTransferencia.getReferencia().intValue();
            this.beneficiariosRegistrados = contTransferenciasDetalleService.getBeneficiarioContTransferencias(opcionBusqueda.getAnio());
            this.estadosRegistrados = contTransferenciasDetalleService.getEstadosContTransferencias(opcionBusqueda.getAnio());
            PrimeFaces.current().ajax().update("fieldsetBeneficiario");
        }else{
            beneficiariosRegistrados = new ArrayList<>();
            estadosRegistrados = new ArrayList<>();
            PrimeFaces.current().ajax().update("fieldsetBeneficiario");
        }
        
        String fecha = "01-01-" + opcionBusqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Integer dia = new Date().getDate();
        Integer mes  = new Date().getMonth()+1;
        try {
            fechaDesde = dateFormat.parse(fecha);
            String fecha_hasta = dia.toString()+ "-"+mes.toString()+"-"+ opcionBusqueda.getAnio();
            fechaHasta = dateFormat.parse(fecha_hasta);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Getter and Setters">
    public Boolean getParametrosFecha() {
        return parametrosFecha;
    }

    public void setParametrosFecha(Boolean parametrosFecha) {
        this.parametrosFecha = parametrosFecha;
    }

    public Boolean getParametrosNumComprobante() {
        return parametrosNumComprobante;
    }

    public void setParametrosNumComprobante(Boolean parametrosNumComprobante) {
        this.parametrosNumComprobante = parametrosNumComprobante;
    }

    public Boolean getParametroBeneficiario() {
        return parametroBeneficiario;
    }

    public void setParametroBeneficiario(Boolean parametroBeneficiario) {
        this.parametroBeneficiario = parametroBeneficiario;
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

    public int getNumDesde() {
        return numDesde;
    }

    public void setNumDesde(int numDesde) {
        this.numDesde = numDesde;
    }

    public int getNumHasta() {
        return numHasta;
    }

    public void setNumHasta(int numHasta) {
        this.numHasta = numHasta;
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

    public List<BeneficiarioTransferenciasModel> getBeneficiariosRegistrados() {
        return beneficiariosRegistrados;
    }

    public void setBeneficiariosRegistrados(List<BeneficiarioTransferenciasModel> beneficiariosRegistrados) {
        this.beneficiariosRegistrados = beneficiariosRegistrados;
    }

    public String getBeneficiarioSeleccionado() {
        return beneficiarioSeleccionado;
    }

    public void setBeneficiarioSeleccionado(String beneficiarioSeleccionado) {
        this.beneficiarioSeleccionado = beneficiarioSeleccionado;
    }
    
    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }
    
    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }
    
//</editor-fold>

    
}
