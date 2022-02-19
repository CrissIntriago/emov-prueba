/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Services.DocumentosServiceRS;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class EnviarConvenioMB implements Serializable {

    @Inject
    private UserSession session;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private DocumentosServiceRS documentosServiceRS;
    @Inject
    private ManagerService service;
    private List<FinaRenLiquidacion> emisiones = new ArrayList<>();
    private LazyModel<FinaRenLiquidacion> titulosCredito;
    private FinaRenLiquidacion liquidacionAnular;
    private List<Documentos> listaDocumentos;
    private Documentos documento;
    private String observacion;
    private FinaRenLiquidacion cobrosGenerales;
    private FinaRenLiquidacion liquidacion;

    @PostConstruct
    public void init() {
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();
        titulosCredito = new LazyModel<>(FinaRenLiquidacion.class);
        titulosCredito.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(2L));
        titulosCredito.getSorteds().put("fechaIngreso", "desc");
    }

    public void enviarConvenio(FinaRenLiquidacion liq) {

        // if (liq.getTipoLiquidacion().getCodigoTituloReporte() == 260 || liq.getTipoLiquidacion().getCodigoTituloReporte() == 261) {
        //     JsfUtil.addErrorMessage("Convenio", "Liquidacion : " + liq.getIdLiquidacion() + " forma parte de los convenios de pago, no puede ser procesada.");
        //     return;
        // }
        System.out.println("enviando a convenio");
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(6L));
        liquidacionService.edit(liq);

        emisiones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();

        JsfUtil.addInformationMessage("Convenio", "Liquidacion : " + liq.getIdLiquidacion() + " enviada a convenio de pago.");

    }

    public void calculosAdicionales() {

        interes(liquidacion);
        liquidacion.setPagoFinal(liquidacion.getTotalPago().add(liquidacion.getRecargo()).add(liquidacion.getInteres()));
    }

    public boolean renderAlcabalaSolicitante(FinaRenLiquidacion liq) {
        return liq.getTipoLiquidacion() != null && liq.getTipoLiquidacion().getPrefijo().equals("ALC") && liq.getPredio() == null;
    }

    public BigDecimal interes(FinaRenLiquidacion l) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = service.valoresInteres(l, new Date());
        interes = interesMap.get("interesCalculado");
        if (l.getTipoLiquidacion().getId().equals(2L) || l.getTipoLiquidacion().getId().equals(3L)) {
            l.setDescuento(interesMap.get("descuento"));
            l.setRecargo(interesMap.get("recargo"));
        }
        if (l.getRecargo() == null) {
            l.setRecargo(BigDecimal.ZERO);
        }
        if (l.getDescuento() == null) {
            l.setDescuento(BigDecimal.ZERO);
        }

        if (!l.getEstadoLiquidacion().getId().equals(2L)) {
            interes = l.getInteresFina();
        }

        l.setInteres(interes);
        return l.getInteres();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public FinaRenLiquidacionService getLiquidacionService() {
        return liquidacionService;
    }

    public void setLiquidacionService(FinaRenLiquidacionService liquidacionService) {
        this.liquidacionService = liquidacionService;
    }

    public DocumentosServiceRS getDocumentosServiceRS() {
        return documentosServiceRS;
    }

    public void setDocumentosServiceRS(DocumentosServiceRS documentosServiceRS) {
        this.documentosServiceRS = documentosServiceRS;
    }

    public List<FinaRenLiquidacion> getEmisiones() {
        return emisiones;
    }

    public void setEmisiones(List<FinaRenLiquidacion> emisiones) {
        this.emisiones = emisiones;
    }

    public LazyModel<FinaRenLiquidacion> getTitulosCredito() {
        return titulosCredito;
    }

    public void setTitulosCredito(LazyModel<FinaRenLiquidacion> titulosCredito) {
        this.titulosCredito = titulosCredito;
    }

    public FinaRenLiquidacion getLiquidacionAnular() {
        return liquidacionAnular;
    }

    public void setLiquidacionAnular(FinaRenLiquidacion liquidacionAnular) {
        this.liquidacionAnular = liquidacionAnular;
    }

    public List<Documentos> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documentos> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public FinaRenLiquidacion getCobrosGenerales() {
        return cobrosGenerales;
    }

    public void setCobrosGenerales(FinaRenLiquidacion cobrosGenerales) {
        this.cobrosGenerales = cobrosGenerales;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
//</editor-fold>

}
