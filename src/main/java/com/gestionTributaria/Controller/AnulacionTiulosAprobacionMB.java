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
import com.gestionTributaria.Services.FinaRenPagoService;
import com.gestionTributaria.Services.FnSolicitudExoneracionServices;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ReportGenealModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
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
public class AnulacionTiulosAprobacionMB extends ReportGenealModel implements Serializable {

    @Inject
    private ManagerService services;
    @Inject
    private UserSession uSession;
    @Inject
    private ServletSession ss;
    @Inject
    private FinaRenPagoService finaRenPagoService;
    @Inject
    private FnSolicitudExoneracionServices fnSolicitudExoneracionServices;
    @Inject
    private ContRegistroContable contableRegistro;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private DocumentosServiceRS documentosServiceRS;

    private LazyModel<FinaRenLiquidacion> liquidacionesLazy;
    private FinaRenLiquidacion liquidacion;
    private FinaRenLiquidacion original, posterior, cemLiquidacion;
    private Documentos documentoDescargar;

    @PostConstruct
    public void init() {
        liquidacionesLazy = new LazyModel(FinaRenLiquidacion.class);
        liquidacionesLazy.getSorteds().put("fechaIngreso", "DESC");
        liquidacionesLazy.getFilterss().put("tipoLiquidacion.id:notEqual", Arrays.asList(2L, 3L));
        liquidacionesLazy.getFilterss().put("estadoLiquidacion.id", Arrays.asList(2L, 4L));
        liquidacionesLazy.getFilterss().put("preAnulacion", Boolean.TRUE);
        documentoDescargar = new Documentos();
        liquidacion = new FinaRenLiquidacion();
        original = new FinaRenLiquidacion();
        posterior = new FinaRenLiquidacion();
        cemLiquidacion = new FinaRenLiquidacion();

    }

    public void aprobarAnulacion(FinaRenLiquidacion liqui) {

        if (contableRegistro.isEmisionContabilizada(liqui)) {
            JsfUtil.update("La Emisión " + liqui.getIdLiquidacion() + " ya se encuentra contabilizada");
            return;
        } else {
            contableRegistro.anularEmision(liqui);
        }

        liqui.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));
        liqui.setFechaAnulacion(new Date());
        finaRenLiquidacionService.edit(liqui);
        JsfUtil.addInformationMessage("", "La Transacción se realizo con exito");
    }

    public void reversar(FinaRenLiquidacion liqui) {
        liqui.setPreAnulacion(Boolean.FALSE);
        finaRenLiquidacionService.edit(liqui);
        JsfUtil.addInformationMessage("", "La Transacción se realizo con exito");
    }

    public void descargarDocumento(FinaRenLiquidacion r) throws ClassNotFoundException {
        original = new FinaRenLiquidacion();
        original = r;
        documentoDescargar = new Documentos();
        documentoDescargar = (Documentos) services.documentoGestionTribtaria(r.getClass().getPackage().getName() + "." + r.getClass().getSimpleName(), r.getId());
        System.out.println("documentoDescargar " + documentoDescargar.getRutaDocumento());
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "descargaDoc?ruta=" + documentoDescargar.getRutaDocumento());

    }

    public void viewDocumento(FinaRenLiquidacion r) throws ClassNotFoundException {
        original = new FinaRenLiquidacion();
        original = r;
        documentoDescargar = new Documentos();
        documentoDescargar = (Documentos) services.documentoGestionTribtaria(r.getClass().getPackage().getName() + "." + r.getClass().getSimpleName(), r.getId());
        System.out.println("documentoDescargar " + documentoDescargar.getRutaDocumento());

    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public ManagerService getServices() {
        return services;
    }

    public void setServices(ManagerService services) {
        this.services = services;
    }

    public UserSession getuSession() {
        return uSession;
    }

    public void setuSession(UserSession uSession) {
        this.uSession = uSession;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public FinaRenPagoService getFinaRenPagoService() {
        return finaRenPagoService;
    }

    public void setFinaRenPagoService(FinaRenPagoService finaRenPagoService) {
        this.finaRenPagoService = finaRenPagoService;
    }

    public FnSolicitudExoneracionServices getFnSolicitudExoneracionServices() {
        return fnSolicitudExoneracionServices;
    }

    public void setFnSolicitudExoneracionServices(FnSolicitudExoneracionServices fnSolicitudExoneracionServices) {
        this.fnSolicitudExoneracionServices = fnSolicitudExoneracionServices;
    }

    public FinaRenLiquidacionService getFinaRenLiquidacionService() {
        return finaRenLiquidacionService;
    }

    public void setFinaRenLiquidacionService(FinaRenLiquidacionService finaRenLiquidacionService) {
        this.finaRenLiquidacionService = finaRenLiquidacionService;
    }

    public DocumentosServiceRS getDocumentosServiceRS() {
        return documentosServiceRS;
    }

    public void setDocumentosServiceRS(DocumentosServiceRS documentosServiceRS) {
        this.documentosServiceRS = documentosServiceRS;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidacionesLazy() {
        return liquidacionesLazy;
    }

    public void setLiquidacionesLazy(LazyModel<FinaRenLiquidacion> liquidacionesLazy) {
        this.liquidacionesLazy = liquidacionesLazy;
    }

    public FinaRenLiquidacion getOriginal() {
        return original;
    }

    public void setOriginal(FinaRenLiquidacion original) {
        this.original = original;
    }

    public FinaRenLiquidacion getPosterior() {
        return posterior;
    }

    public void setPosterior(FinaRenLiquidacion posterior) {
        this.posterior = posterior;
    }

    public FinaRenLiquidacion getCemLiquidacion() {
        return cemLiquidacion;
    }

    public void setCemLiquidacion(FinaRenLiquidacion cemLiquidacion) {
        this.cemLiquidacion = cemLiquidacion;
    }

    public Documentos getDocumentoDescargar() {
        return documentoDescargar;
    }

    public void setDocumentoDescargar(Documentos documentoDescargar) {
        this.documentoDescargar = documentoDescargar;
    }

    public FinaRenLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(FinaRenLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }
//</editor-fold>

}
