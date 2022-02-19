/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.services.ContComprobantePagoService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Criss Intriago
 * @author Luis PoZo Gonzabay
 */
@Named(value = "reporteComprobanteView")
@ViewScoped
public class reporteComprobantePagoControlles implements Serializable {

    @Inject
    private ServletSession servletSession;
    @Inject
    private ContComprobantePagoService contComprobantePagoService;
    @Inject
    private ServidorService servidorService;

    private Boolean parametrosFecha, parametrosNumComprobante, parametroBeneficiario;
    private Date fechaDesde;
    private Date fechaHasta;
    private int numDesde;
    private int numHasta;
    private String beneficiario, tipoEstado;
    private Cliente clienteSelect;
    private LazyModel<Servidor> servidorLazy;
    private LazyModel<Proveedor> proveedorLazy;
    private OpcionBusqueda opcionBusqueda;
    private ContComprobantePago ultimoComprobantePago;

    @PostConstruct
    public void initialize() {
        this.opcionBusqueda = new OpcionBusqueda();
        vaciarParametros();
    }

    public void generarReporte(String tipoArchivo) {
        if (ultimoComprobantePago == null) {
            JsfUtil.addErrorMessage("ERROR", "No hay ningún Pago Registrado");
            return;
        }
        if (numHasta > ultimoComprobantePago.getNumRegistro()) {
            JsfUtil.addWarningMessage("AVISO", "El ultimo Comprobante de Pago es el No." + ultimoComprobantePago.getNumRegistro());
            return;
        }
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("ComprobantePago");
        servletSession.addParametro("fecha_desde", fechaDesde);
        servletSession.addParametro("fecha_hasta", fechaHasta);
        servletSession.addParametro("num_desde", numDesde);
        servletSession.addParametro("num_hasta", numHasta);
        servletSession.addParametro("beneficiario", beneficiario);
        servletSession.addParametro("tipoEstado", tipoEstado);
        servletSession.addParametro("tipoBeneficiario", parametroBeneficiario == null ? Boolean.TRUE : parametroBeneficiario);
        servletSession.setNombreReporte("ReporteComprobantePago");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void vaciarParametros() {
//        this.ultimoComprobantePago = comprobantePagoService.getUltimoComprobantePago(opcionBusqueda.getAnio());
        this.ultimoComprobantePago = contComprobantePagoService.getLastContComprobantePago(opcionBusqueda.getAnio());
        this.parametrosFecha = Boolean.TRUE;
        this.parametrosNumComprobante = Boolean.TRUE;
        this.parametroBeneficiario = Boolean.TRUE;
        beneficiario = "";
        clienteSelect = new Cliente();
        if (ultimoComprobantePago != null) {
            this.numDesde = 1;
            this.numHasta = ultimoComprobantePago.getNumRegistro();
        }
        String fecha = "01-01-" + opcionBusqueda.getAnio();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            fechaDesde = dateFormat.parse(fecha);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        this.fechaHasta = new Date();
        tipoEstado = "";
    }

    public void buscarBeneficiario(boolean tipo) {
        if (tipo) {//Servidor
            clienteSelect = servidorService.findByServidorByIdentificacion(beneficiario);
            if (clienteSelect != null) {
                beneficiario = clienteSelect.getIdentificacion();
                JsfUtil.addInformationMessage("Información", "Servidor Encontrado Correctamente.");
            } else {
                loadLazy(tipo);
                JsfUtil.executeJS("PF('beneficiarioDlg').show()");
            }
        } else {//Proveedor
            clienteSelect = servidorService.findByProveedorByIdentificacionCompleta(beneficiario);
            if (clienteSelect != null) {
                beneficiario = clienteSelect.getIdentificacionCompleta();
                JsfUtil.addInformationMessage("Información", "Proveedor Encontrado Correctamente.");
            } else {
                loadLazy(tipo);
                JsfUtil.executeJS("PF('beneficiarioDlg').show()");
            }
        }
    }

    public void loadLazy(boolean tipo) {
        if (tipo) {
            servidorLazy = new LazyModel<>(Servidor.class);
            servidorLazy.getSorteds().put("persona.apellido", "ASC");
            servidorLazy.getFilterss().put("estado", true);
            servidorLazy.getFilterss().put("persona.estado", true);
            servidorLazy.setDistinct(false);
        } else {
            proveedorLazy = new LazyModel<>(Proveedor.class);
            proveedorLazy.getSorteds().put("cliente.nombre", "ASC");
            proveedorLazy.getFilterss().put("estado", true);
            proveedorLazy.setDistinct(false);
        }
        PrimeFaces.current().ajax().update("beneficiarioForm");
    }

    public void initClienteSelect() {
        clienteSelect = new Cliente();
        beneficiario = "";
    }

    public void closeDlg(Cliente cliente) {
        clienteSelect = cliente;
        beneficiario = parametroBeneficiario ? clienteSelect.getIdentificacion() : clienteSelect.getIdentificacionCompleta();
        JsfUtil.executeJS("PF('beneficiarioDlg').hide()");
        PrimeFaces.current().ajax().update("fieldsetBeneficiario");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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

    public Boolean getParametroBeneficiario() {
        return parametroBeneficiario;
    }

    public void setParametroBeneficiario(Boolean parametroBeneficiario) {
        this.parametroBeneficiario = parametroBeneficiario;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Cliente getClienteSelect() {
        return clienteSelect;
    }

    public void setClienteSelect(Cliente clienteSelect) {
        this.clienteSelect = clienteSelect;
    }

    public LazyModel<Servidor> getServidorLazy() {
        return servidorLazy;
    }

    public void setServidorLazy(LazyModel<Servidor> servidorLazy) {
        this.servidorLazy = servidorLazy;
    }

    public LazyModel<Proveedor> getProveedorLazy() {
        return proveedorLazy;
    }

    public void setProveedorLazy(LazyModel<Proveedor> proveedorLazy) {
        this.proveedorLazy = proveedorLazy;
    }
//</editor-fold>

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

}
