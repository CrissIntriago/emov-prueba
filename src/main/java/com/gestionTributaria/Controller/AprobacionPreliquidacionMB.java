/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.Documentos;
import com.gestionTributaria.Services.DocumentosServiceRS;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.IOException;
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
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class AprobacionPreliquidacionMB extends ReportGenealModel implements Serializable {

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
        titulosCredito.getFilterss().put("tipoLiquidacion.necesitaValidacionRentas", true);
        titulosCredito.getSorteds().put("fechaIngreso", "desc");
    }

    public void validarLiquidacion(FinaRenLiquidacion liq) {
        liq.setValidada(Boolean.TRUE);
        liq.setUsuarioValida(session.getNameUser());
        liq.setLiquidadorAprobador(clienteService.getUsuarioNombre(session.getNameUser()));
        liquidacionService.edit(liq);
        JsfUtil.addInformationMessage("Liquidacion " + liq.getIdLiquidacion() + " validada con exito.", "");
    }

    public void openDialogAnularLiquidacion(FinaRenLiquidacion liq) {
        //geDocumentosList = new ArrayList();
        liquidacionAnular = new FinaRenLiquidacion();
        liquidacionAnular = liq;
        documento = new Documentos();
        listaDocumentos = new ArrayList<>();
        observacion = null;
        System.out.println("->" + liquidacionAnular.toString());

    }

    public void handleFileDocumentBySave(FileUploadEvent event) {
        try {

            String ruta = SisVars.RUTA_EVIDENCIA + Utils.getAnio(new Date()) + Utils.getHour(new Date())
                    + Utils.getMinute(new Date()) + Utils.getSecond(new Date()) + Utils.getMiliSecond(new Date()) + "_" + event.getFile().getFileName();
            documento = new Documentos();
            documento.setDepartamento(session.getDepartamento());
            documento.setRutaDocumento(ruta);
            documento.setFechaCreacion(new Date());
            documento.setNombre(event.getFile().getFileName());
            documento.setDescripcion(event.getFile().getContentType());
            documento.setEstado(Boolean.TRUE);

            documento.setClaseNombre(liquidacionAnular.getClass().getPackage().getName() + "." + liquidacionAnular.getClass().getSimpleName());
            documento.setIdentificador(liquidacionAnular.getId());

            listaDocumentos.add(documento);

            Utils.copyFileServerDoc(ruta, event.getFile().getInputstream());
            JsfUtil.addInformationMessage("Nota1", "Archivo cargado Satisfactoriamente");
        } catch (IOException e) {
            JsfUtil.addWarningMessage("", "Ocurrió un error al momento de subri el documento");
        }
    }

    public void inactivarDocumento(Documentos doc, int index) {
        doc = listaDocumentos.get(index);
        if (doc.getId() != null) {
            doc.setEstado(Boolean.FALSE);
            documentosServiceRS.edit(doc);
        }

        listaDocumentos.remove(index);
        JsfUtil.addInformationMessage("", "El documento se inactivo con exito");
    }

    public void anularLiquidacion() {
        if (observacion == null || observacion.equals("")) {
            JsfUtil.addErrorMessage("Información", "Las observaciones son obligatorias");
            return;
        }
        if (liquidacionAnular.getObservacion() != null) {
            liquidacionAnular.setObservacion(liquidacionAnular.getObservacion() + "; ANULACION: " + observacion);
        } else {
            liquidacionAnular.setObservacion("ANULACION: " + observacion);
        }
        if (liquidacionAnular.getIpUserSession() == null || liquidacionAnular.getIpUserSession().equals("")) {
            liquidacionAnular.setIpUserSession(session.getIpClient());
        } else {
            liquidacionAnular.setIpUserSession(liquidacionAnular.getIpUserSession() + " - ANULA" + session.getIpClient());
        }
        if (liquidacionAnular.getMacAddresUsuarioIngreso() == null || liquidacionAnular.getMacAddresUsuarioIngreso().equals("")) {
            liquidacionAnular.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        } else {
            liquidacionAnular.setMacAddresUsuarioIngreso(liquidacionAnular.getMacAddresUsuarioIngreso() + " - ANULA" + session.getMACAddressEquipo());
        }
        liquidacionAnular.setUsuarioAnular(session.getNameUser());
        liquidacionAnular.setFechaAnulacion(new Date());
        liquidacionAnular.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
        liquidacionService.edit(liquidacionAnular);
        for (Documentos doc : listaDocumentos) {
            if (doc.getId() != null) {
                documentosServiceRS.edit(doc);
            } else {
                documentosServiceRS.create(doc);
            }
        }
        emisiones = new ArrayList<>();
        liquidacion = new FinaRenLiquidacion();
        cobrosGenerales = new FinaRenLiquidacion();
        JsfUtil.addInformationMessage("ANULADO", "Liquidacion : " + liquidacionAnular.getIdLiquidacion() + " anulada con exito");

        JsfUtil.executeJS("PF('dlgAnular').hide()");
        JsfUtil.update("frmMain");
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
