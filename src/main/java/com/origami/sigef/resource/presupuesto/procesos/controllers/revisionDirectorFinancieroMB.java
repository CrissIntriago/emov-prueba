/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.presupuesto.procesos.controllers;

import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import com.origami.sigef.resource.presupuesto.procesos.services.ClienteServices;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.activiti.engine.task.Task;
import org.ocpsoft.rewrite.config.Log;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
@Named
@ViewScoped
public class revisionDirectorFinancieroMB implements Serializable {

    private List<ListarequisitosModel> requisitosTramite;
    private ListarequisitosModel requisitosObjeto;
    private Observaciones observacion;
    private String observaciones;
    private HistoricoTramites tramite;
    private Task tarea;
    private boolean btnAprobar, btnRechazar;
    private Boolean btnAfavor;
    private Map<String, Object> paramts;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteServices clienteServices;

    @PostConstruct
    public void initView() {

    }

    @Override
    public String toString() {
        return "revisionDirectorFinancieroMB{" + "requisitosTramite=" + requisitosTramite + '}';
    }

    public void handleFileUpload(FileUploadEvent event) {
//        try {
//            uploadDoc.upload(tramite, Arrays.asList(event.getFile()));
//            if (requisitosObjeto != null) {
//                TipoTramiteRequisitoHistorial objeto = new TipoTramiteRequisitoHistorial();
//                objeto.setTipoTramite(this.tramite.getTipoTramite());
//                objeto.setProcedimientoRequisito(requisitosObjeto.getRequisitos());
//                tramiteRequisitoHistorialService.edit(objeto);
//                requisitosObjeto = null;
//            }
//            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
//        } catch (Exception e) {
//            JsfUtil.addErrorMessage("Archivo", "Ocurrió un error al subir archivo.");
//            LOG.log(Level.SEVERE, "Error al subir archivo", e);
//        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(userSession.getName());
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
                    getParamts().put("usuarioAnalista", clienteServices.getrolsUser(RolUsuario.analista));
                    break;
                case "revisionDirector":
                    getParamts().put("usuarioAsignacion", clienteServices.getrolsUser(RolUsuario.asignacionControlPrevio));
                    break;
                case "asignacion_previo":
                    getParamts().put("usuarioControl", clienteServices.getrolsUser(RolUsuario.controlPrevio));
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
                        getParamts().put(codeUsuario, clienteServices.getrolsUser(codeRol));
                    } else {
                        if (valor > 0) {
                            getParamts().put(codeUsuario, clienteServices.getrolsUser(codeRol));
                        }
                    }

                    break;
                case "autorizacionGasto":
                    switch (getTramite().getTipoTramite().getAbreviatura()) {
                        case "PAG_ANTI_BIENES":
                        case "PAG_ANTI_SERVICIOS":
                        case "PAG_ANTI_OBRAPUBLICA":
                        case "PAG_ANTI_CONSULTORIA":
                            getParamts().put("usuarioPolizas", clienteServices.getrolsUser(RolUsuario.registroPolizas));
                            break;
                        case "RET_JUD":
                        case "proceso_jgvis_servicios":
                            getParamts().put("usuarioPago", clienteServices.getrolsUser(RolUsuario.autorizacionPago));
                            break;
                        case "PPRM":
                            getParamts().put("aprobado", valor);
                            if (valor == 1) {
                                getParamts().put("usuarioContabilidad", clienteServices.getrolsUser(RolUsuario.registroContable));
                            } else {
                                getParamts().put("usuarioAnalista", clienteServices.getrolsUser(RolUsuario.analista));
                            }
                            break;
                        case "PAG_ANTI_VIATICOS":
                        case "PAGORMU":
                            getParamts().put("aprobado", valor);
                            if (valor == 1) {
                                getParamts().put("usuarioPago", clienteServices.getrolsUser(RolUsuario.autorizacionPago));
                            } else {
                                getParamts().put("usuarioAsignacion", clienteServices.getrolsUser(RolUsuario.asignacionControlPrevio));
                            }
                            break;
                        default:
                            getParamts().put("usuarioContabilidad", clienteServices.getrolsUser(RolUsuario.registroContable));
                            break;
                    }
                    break;
                case "autorizacionPago":
                    getParamts().put("usuarioComprobante", clienteServices.getrolsUser(RolUsuario.comprobantePago));
                    break;
                case "revisionDocumentos":
                    getParamts().put("usuarioDirectorFinanciero", clienteServices.getrolsUser(RolUsuario.directorFinanciero));
                    break;
                case "autorizacionServiciosInstitucionales":
                    getParamts().put("usuarioGasto", clienteServices.getrolsUser(RolUsuario.autorizacionGasto));
                    break;
            }
//            if (saveTramite() == null) {
//                return;
//            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            JsfUtil.executeJS("PF('dlgObservaciones2').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            userSession.setVarTemp(null);
//             completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Get And sET">
    public void addReqTramite(ListarequisitosModel requisito) {
        requisitosObjeto = requisito;
    }

    public List<ListarequisitosModel> getRequisitosTramite() {
        return requisitosTramite;
    }

    public void setRequisitosTramite(List<ListarequisitosModel> requisitosTramite) {
        this.requisitosTramite = requisitosTramite;
    }

    public ListarequisitosModel getRequisitosObjeto() {
        return requisitosObjeto;
    }

    public void setRequisitosObjeto(ListarequisitosModel requisitosObjeto) {
        this.requisitosObjeto = requisitosObjeto;
    }

    public Observaciones getObservacion() {
        return observacion;
    }

    public void setObservacion(Observaciones observacion) {
        this.observacion = observacion;
    }

    public HistoricoTramites getTramite() {
        return tramite;
    }

    public void setTramite(HistoricoTramites tramite) {
        this.tramite = tramite;
    }

    public Task getTarea() {
        return tarea;
    }

    public void setTarea(Task tarea) {
        this.tarea = tarea;
    }

    public Boolean getBtnAfavor() {
        return btnAfavor;
    }

    public void setBtnAfavor(Boolean btnAfavor) {
        this.btnAfavor = btnAfavor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Map<String, Object> getParamts() {
        return paramts;
    }

    public void setParamts(Map<String, Object> paramts) {
        this.paramts = paramts;
    }

    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public ClienteServices getClienteServices() {
        return clienteServices;
    }

    public void setClienteServices(ClienteServices clienteServices) {
        this.clienteServices = clienteServices;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
//</editor-fold>

}
