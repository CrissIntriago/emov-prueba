/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.procesos.pagos;

import com.origami.sigef.activos.service.procesoService.ProcesoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.TipoTramiteRequisitoHistorial;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
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
 * @author jintr
 */
@Named(value = "revisionDocumentoView")
@ViewScoped
public class RevisionDocumentoController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ClienteService clienteService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private TramiteRequisitoHistorialService tramiteRequisitoHistorialService;
    @Inject
    private ProcesoService procesoService;
    @Inject
    private ServletSession servletSession;

    private List<ListarequisitosModel> requisitosTramite;

    private LazyModel<DiarioGeneral> diarioGeneralLazyModel;

    private String observaciones;

    private Boolean btnRechazar;
    private Boolean btnAfavor;

    private ListarequisitosModel requisitosObjeto;
    private OpcionBusqueda busqueda;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                btnAfavor = false;
                if (tarea.getTaskDefinitionKey().equals("control_previo")) {
                    btnRechazar = true;
                } else {
                    btnRechazar = false;
                }
                listRequisitos();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
            diarioGeneral();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void listRequisitos() {
        List<ProcedimientoRequisito> listaRequisitos = procesoService.getListaRequisito(tramite.getTipoTramite().getId());
        if (listaRequisitos != null) {
            requisitosTramite = new ArrayList<>(listaRequisitos.size() + 1);
            for (ProcedimientoRequisito tipoTramite : listaRequisitos) {
                ListarequisitosModel req = new ListarequisitosModel();
                req.setRequisitos(tipoTramite);
                requisitosTramite.add(req);
            }
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        Boolean accion = false;
        switch (tarea.getTaskDefinitionKey()) {
            case "validadorContable":
                if (getTramite().getTipoTramite().getAbreviatura().equals("proceso_jgvis_servicios")) {
                    btnAfavor = true;
                }
                break;
            case "control_previo":
                if (getTramite().getTipoTramite().getAbreviatura().equals("procesos_pagos_noClasficados")) {
                    accion = true;
                }
                break;
            case "autorizacionGasto":
                switch (getTramite().getTipoTramite().getAbreviatura()) {
                    case "PPRM":
                    case "PAG_ANTI_VIATICOS":
                    case "PAGORMU":
                        accion = true;
                        break;
                }
                break;

        }
        if (accion) {
            PrimeFaces.current().executeScript("PF('dlgObservaciones2').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser2");
        } else {
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
        }
    }

    public void completarTarea(int valor) {
        try {
            Boolean accion = false;
            observacion.setObservacion(observaciones);
            switch (tarea.getTaskDefinitionKey()) {
                case "solicitudAnticipo":
                    getParamts().put("usuarioAnalista", clienteService.getrolsUser(RolUsuario.analista));
                    break;
                case "revisionDirector":
                    getParamts().put("usuarioAsignacion", clienteService.getrolsUser(RolUsuario.asignacionControlPrevio));
                    break;
                case "asignacion_previo":
                    getParamts().put("usuarioControl", clienteService.getrolsUser(RolUsuario.controlPrevio));
                    break;
                case "control_previo":
                    String codeRol = "";
                    String codeUsuario = "";
                    switch (getTramite().getTipoTramite().getAbreviatura()) {
                        case "PAG_ANTI_VIATICOS":
                            codeRol = RolUsuario.titularTH;
                            codeUsuario = "usuarioTalentoHumano";
                            break;
                        case "procesos_rf_caja_chica":
                        case "procesp_fc_liquidacion":
                        case "proceso_jgvis_servicios":
                            codeRol = RolUsuario.registroContable;
                            codeUsuario = "usuarioContabilidad";
                            break;
                        case "proceso_af_caja_chica":
                        case "PPPI":
                            codeRol = RolUsuario.autorizacionPago;
                            codeUsuario = "usuarioPago";
                            break;
                        case "procesos_pagos_noClasficados":
                            if (valor == 1) {
                                codeRol = RolUsuario.autorizacionGasto;
                                codeUsuario = "usuarioGasto";
                            } else if (valor == 2) {
                                codeRol = RolUsuario.autorizacionPago;
                                codeUsuario = "usuarioPago";
                            }
                            break;
                        case "PAGORMU":
                            accion = true;
                            if (valor == 0) {
                                codeRol = RolUsuario.analista;
                                codeUsuario = "usuarioAnalista";
                            } else if (valor == 1) {
                                codeRol = RolUsuario.autorizacionGasto;
                                codeUsuario = "usuarioGasto";
                            }
                            break;
                        default:
                            codeRol = RolUsuario.autorizacionGasto;
                            codeUsuario = "usuarioGasto";
                            break;
                    }
                    getParamts().put("aprobado", valor);
                    if (accion) {
                        getParamts().put(codeUsuario, clienteService.getrolsUser(codeRol));
                    } else {
                        if (valor > 0) {
                            getParamts().put(codeUsuario, clienteService.getrolsUser(codeRol));
                        }
                    }

                    break;
                case "autorizacionGasto":
                    switch (getTramite().getTipoTramite().getAbreviatura()) {
                        case "PAG_ANTI_BIENES":
                        case "PAG_ANTI_SERVICIOS":
                        case "PAG_ANTI_OBRAPUBLICA":
                        case "PAG_ANTI_CONSULTORIA":
                            getParamts().put("usuarioPolizas", clienteService.getrolsUser(RolUsuario.registroPolizas));
                            break;
                        case "RET_JUD":
                        case "proceso_jgvis_servicios":
                            getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
                            break;
                        case "PPRM":
                            getParamts().put("aprobado", valor);
                            if (valor == 1) {
                                getParamts().put("usuarioContabilidad", clienteService.getrolsUser(RolUsuario.registroContable));
                            } else {
                                getParamts().put("usuarioAnalista", clienteService.getrolsUser(RolUsuario.analista));
                            }
                            break;
                        case "PAG_ANTI_VIATICOS":
                        case "PAGORMU":
                            getParamts().put("aprobado", valor);
                            if (valor == 1) {
                                getParamts().put("usuarioPago", clienteService.getrolsUser(RolUsuario.autorizacionPago));
                            } else {
                                getParamts().put("usuarioAsignacion", clienteService.getrolsUser(RolUsuario.asignacionControlPrevio));
                            }
                            break;
                        default:
                            getParamts().put("usuarioContabilidad", clienteService.getrolsUser(RolUsuario.registroContable));
                            break;
                    }
                    break;
                case "autorizacionPago":
                    getParamts().put("usuarioComprobante", clienteService.getrolsUser(RolUsuario.comprobantePago));
                    break;
                case "revisionDocumentos":
                    getParamts().put("usuarioDirectorFinanciero", clienteService.getrolsUser(RolUsuario.directorFinanciero));
                    break;
                case "autorizacionServiciosInstitucionales":
                    getParamts().put("usuarioGasto", clienteService.getrolsUser(RolUsuario.autorizacionGasto));
                    break;
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.executeJS("PF('dlgObservaciones2').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            uploadDoc.upload(this.tramite, Arrays.asList(event.getFile()));
            if (requisitosObjeto != null) {
                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
                objeto.setTipoTramite(this.tramite.getTipoTramite());
                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
                tramiteRequisitoHistorialService.edit(objeto);
                requisitosObjeto = null;
            }
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
            LOG.log(Level.SEVERE, "Error al subir archivo", e);
        }
    }

    //verificadorContable
    private void diarioGeneral() {
        busqueda = new OpcionBusqueda();
        this.diarioGeneralLazyModel = new LazyModel<>(DiarioGeneral.class);
        this.diarioGeneralLazyModel.getSorteds().put("id", "ASC");
        this.diarioGeneralLazyModel.getFilterss().put("estado", true);
        this.diarioGeneralLazyModel.getFilterss().put("periodo", busqueda.getAnio());
        this.diarioGeneralLazyModel.getFilterss().put("numTramite", tramite.getNumTramite());
    }

    public void imprimirReporte(DiarioGeneral diarioGeneral) {
        servletSession.addParametro("id_diario_general", diarioGeneral.getId());
        servletSession.setNombreReporte("diarioGeneralIntegrado");
        servletSession.setNombreSubCarpeta("LibroDiarioIntegrado");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void completarTareaVerificador(int valor) {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("aprobado", valor);
            if (valor == 1) {
                getParamts().put("usuarioAnalista", clienteService.getrolsUser(RolUsuario.analista));
            } else if (valor == 2) {
                getParamts().put("usuarioGasto", clienteService.getrolsUser(RolUsuario.autorizacionGasto));
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.executeJS("PF('dlgObservaciones2').hide()");
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(Boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

    public LazyModel<DiarioGeneral> getDiarioGeneralLazyModel() {
        return diarioGeneralLazyModel;
    }

    public void setDiarioGeneralLazyModel(LazyModel<DiarioGeneral> diarioGeneralLazyModel) {
        this.diarioGeneralLazyModel = diarioGeneralLazyModel;
    }

    public Boolean getBtnAfavor() {
        return btnAfavor;
    }

    public void setBtnAfavor(Boolean btnAfavor) {
        this.btnAfavor = btnAfavor;
    }

}
