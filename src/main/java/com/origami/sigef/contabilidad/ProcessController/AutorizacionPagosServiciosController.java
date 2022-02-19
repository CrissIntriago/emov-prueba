/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.resource.procesos.services.TramiteRequisitoHistorialService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "autorizacionServiciosPagosProfView")
@ViewScoped
public class AutorizacionPagosServiciosController extends BpmnBaseRoot implements Serializable {

    @Inject
    private UserSession user;
    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private ProcesoService tramiteServiceu;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private ProcedimientoRequisitoService requisitosService;
    @Inject
    private CatalogoItemService catalogoItemService;

    private String observaciones;
    private Boolean btnRenderObservacion;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;
    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                procedimientoRequisitoList = requisitosService.getListaRequisitos(tramite.getTipoTramite().getId());
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
            listRequisitos();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }

    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = tramiteServiceu.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                if (!req.getRequisitos().getObligatorio()) {
                    if (req.getRequisitos().getIdRequisito().getNombre().toUpperCase().contains("GASTO")) {
                        requisitosTramite.add(req);
                    }
                }
            }
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        getBtnObservacion();
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea(int numero) {
        try {
            observacion.setObservacion(observaciones);
            switch (tramite.getTipoTramite().getAbreviatura()) {
                case "PAG_ANTI_VIATICOS":
                    getParamts().put("aprobado", numero);
                    if (numero == 0) {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.financiero));
                    } else {
                        getParamts().put("usuarioFin", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                    }
                    break;
                case "PPS_profesionales":
                case "procesos_pagos_noClasficados":
                    getParamts().put("aprobado", numero);
                    if (numero == 0) {
                        getParamts().put("usuarioFin", clienteService.getrolsUser(RolUsuario.financiero));
                    } else {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                case "PAG_SERV_BASIC":
                case "PAG_SERV_NOTARIALES":
                    getParamts().put("aprobado", numero);
                    if (numero == 1) {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                    } else {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.financiero));
                    }
                    break;
                case "PRHEXSU":
                case "PPRM":
                    getParamts().put("aprobado", numero);
                    if (numero == 0) {
                        getParamts().put("usuarioATTHH", clienteService.getrolsUser(RolUsuario.analista));
                        TipoRol tipoRol = tipoRolService.getRolRechazado(this.tramite.getNumTramite(), Utils.getAnio(new Date()).shortValue());
                        CatalogoItem estadoAprobacion = catalogoItemService.getEstadoRol("registrado-rol");
                        tipoRol.setEstadoAprobacion(estadoAprobacion);
                        tipoRolService.edit(tipoRol);
                    } else {
                        getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                    }
                    break;
                case "RET_JUD":
                    getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                    break;
                case "proceso_liquidacion_pterceros":
                    getParamts().put("usuarioCp", clienteService.getrolsUser(RolUsuario.contador));
                    break;
                case "proceso_jgvis_servicios":
                    getParamts().put("usuarioDir", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                    break;

                default:
                    getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.contador));
                    break;

            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public void getBtnObservacion() {
        switch (tramite.getTipoTramite().getAbreviatura()) {
            case "PAG_SERV_BASIC":
            case "PAG_SERV_NOTARIALES":
            case "PPS_profesionales":
            case "procesos_pagos_noClasficados":
            case "PRHEXSU":
            case "PPRM":
            case "PAG_ANTI_VIATICOS":
                btnRenderObservacion = true;
                break;
            default:
                btnRenderObservacion = false;
                break;
        }
    }

    public void handleFileUploadCertificadoGerente(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archivo se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    public Boolean getBtnRenderObservacion() {
        return btnRenderObservacion;
    }

    public void setBtnRenderObservacion(Boolean btnRenderObservacion) {
        this.btnRenderObservacion = btnRenderObservacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<ProcedimientoRequisito> getProcedimientoRequisitoList() {
        return procedimientoRequisitoList;
    }

    public void setProcedimientoRequisitoList(List<ProcedimientoRequisito> procedimientoRequisitoList) {
        this.procedimientoRequisitoList = procedimientoRequisitoList;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }
}
