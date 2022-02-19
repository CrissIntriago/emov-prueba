/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.SolicitudRequisitoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.Procedimiento;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.SolicitudRequisito;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "revisioncertificadobeans")
@ViewScoped
public class RevisionCertificadoReservaProceso extends BpmnBaseRoot implements Serializable {

    @Inject
    private ReservaCompromisoService reservaService;
    @Inject
    private ProcedimientoRequisitoService procedimientoRequisitoService;
    @Inject
    private SolicitudRequisitoService solicitudRequisitoService;

    private SolicitudReservaCompromiso solcitudReserva;
    private DetalleSolicitudCompromiso detalleReservaCompromiso;
    private LazyModel<SolicitudReservaCompromiso> lazy;
    private List<DetalleSolicitudCompromiso> solicitudesDetalles;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<SolicitudRequisito> solicitudRequisitoList;
    private Procedimiento procedimientoSeleccionado;

    private String fileName;

    @PostConstruct
    public void incializador() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
        }
        this.solcitudReserva = new SolicitudReservaCompromiso();
        this.detalleReservaCompromiso = new DetalleSolicitudCompromiso();
        lazy = new LazyModel(SolicitudReservaCompromiso.class);
        lazy.getFilterss().put("estado.codigo", Arrays.asList("E"));
        solicitudRequisitoList = new ArrayList<>();
        procedimientoRequisitoList = new ArrayList<>();
        procedimientoSeleccionado = new Procedimiento();
    }

    public String formatoCodigo(Integer i) {
        Formatter fmt = new Formatter();
        String a = fmt.format("%05d", i).toString();

        return a;
    }

    public void completar() {
        getParamts().put("contabilidad", session.getName());
        getParamts().put("idServidor", session.getUserId());
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public void openDlgoVisualizacion(SolicitudReservaCompromiso s) {
        this.solcitudReserva = new SolicitudReservaCompromiso();
        this.solcitudReserva = s;
        this.procedimientoSeleccionado = s.getProcedimiento();

        procedimientoRequisitoList = procedimientoRequisitoService.getRequisitosDelProcedimiento(procedimientoSeleccionado);
        List<SolicitudRequisito> solititudRequisitoListTemp = solicitudRequisitoService.getRequisitosRegistrados(s);
        if (solititudRequisitoListTemp == null || solititudRequisitoListTemp.isEmpty()) {
            solicitudRequisitoList = new ArrayList<>();
        } else {
            solititudRequisitoListTemp.forEach((sr) -> {
                solicitudRequisitoList.add(sr);
            });
        }
        this.solicitudesDetalles = reservaService.getListaDetlleSolciitud(s);
        PrimeFaces.current().executeScript("PF('DlgoVisualizacionRevision').show()");
        PrimeFaces.current().ajax().update(":formVisualizacionRevision");

    }

    public void enviarRevisionsolicitud(SolicitudReservaCompromiso s) {
        this.solcitudReserva = s;
        this.solcitudReserva.setEstado(reservaService.getestados("REVIS", "estado_solicitud"));
        reservaService.edit(solcitudReserva);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "Solcitad " + formatoCodigo(this.solcitudReserva.getSecuencial()) + "-" + this.solcitudReserva.getPeriodo() + " enviada con éxito");
        this.solcitudReserva = new SolicitudReservaCompromiso();

    }

    public void vizualizarPDF(ProcedimientoRequisito procedimientoRequisito) {
        if (solicitudRequisitoList.isEmpty() || solicitudRequisitoList == null) {
            JsfUtil.addErrorMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO A LOS REQUISITOS");
        } else {
            for (SolicitudRequisito pr : solicitudRequisitoList) {
                if (Objects.equals(pr.getIdProcedimientoRequisito().getId(), procedimientoRequisito.getId())) {
                    setFileName("/resources/demo/media/" + pr.getUrl());
                    break;
                } else {
                    setFileName("");
                }
            }
            if (getFileName().equals("")) {
                JsfUtil.addWarningMessage("INFORMACIÓN", "NO EXISTE NINGUN DOCUMENTO ADJUNTO AL REQUISITO SELECIONADO");
            } else {
                PrimeFaces.current().executeScript("PF('viewPDF').show()");
            }
        }
    }

    public ReservaCompromisoService getReservaService() {
        return reservaService;
    }

    public void setReservaService(ReservaCompromisoService reservaService) {
        this.reservaService = reservaService;
    }

    public SolicitudReservaCompromiso getSolcitudReserva() {
        return solcitudReserva;
    }

    public void setSolcitudReserva(SolicitudReservaCompromiso solcitudReserva) {
        this.solcitudReserva = solcitudReserva;
    }

    public DetalleSolicitudCompromiso getDetalleReservaCompromiso() {
        return detalleReservaCompromiso;
    }

    public void setDetalleReservaCompromiso(DetalleSolicitudCompromiso detalleReservaCompromiso) {
        this.detalleReservaCompromiso = detalleReservaCompromiso;
    }

    public LazyModel<SolicitudReservaCompromiso> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<SolicitudReservaCompromiso> lazy) {
        this.lazy = lazy;
    }

    public List<DetalleSolicitudCompromiso> getSolicitudesDetalles() {
        return solicitudesDetalles;
    }

    public void setSolicitudesDetalles(List<DetalleSolicitudCompromiso> solicitudesDetalles) {
        this.solicitudesDetalles = solicitudesDetalles;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public List<SolicitudRequisito> getSolicitudRequisitoList() {
        return solicitudRequisitoList;
    }

    public void setSolicitudRequisitoList(List<SolicitudRequisito> solicitudRequisitoList) {
        this.solicitudRequisitoList = solicitudRequisitoList;
    }

    public Procedimiento getProcedimientoSeleccionado() {
        return procedimientoSeleccionado;
    }

    public void setProcedimientoSeleccionado(Procedimiento procedimientoSeleccionado) {
        this.procedimientoSeleccionado = procedimientoSeleccionado;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
