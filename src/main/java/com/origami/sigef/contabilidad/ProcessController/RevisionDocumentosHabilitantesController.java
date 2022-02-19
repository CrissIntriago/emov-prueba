/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.ProcessController;

import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "revisionDocumentosHabiliView")
@ViewScoped
public class RevisionDocumentosHabilitantesController extends BpmnBaseRoot implements Serializable {

    @Inject
    private ProcedimientoRequisitoService requisitosService;
    @Inject
    private UserSession user;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ClienteService clienteService;

    private List<UploadedFile> files;
    private String observaciones;
    private List<ProcedimientoRequisito> procedimientoRequisitoList;

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
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            switch (tramite.getTipoTramite().getAbreviatura()) {
                case "RET_JUD":
                case "PPPI":
                case "PPS_profesionales":
                case "proceso_pago_ceb":
                case "procesos_pagos_noClasficados":
                case "proceso_pce_servicios":
                case "proceso_pi_cuantia_bienes":
                case "proceso_pi_cuantia_servicios":
                case "PAG_INF_CUANT_OB_MENOR":
                case "proceso_pca_bienes":
                case "proceso_pc_consultoria":
                case "proceso_pco_publica":
                case "proceso_pc_servicios":
                case "PAG_DEC":
                case "proceso_jgvis_servicios":
                case "procesp_fc_liquidacion":
                    getParamts().put("usuarioCon", clienteService.getrolsUser(RolUsuario.financiero));
                    break;
                case "PAG_ANTI_VIATICOS":
                    getParamts().put("usuarioFinanciero", clienteService.getrolsUser(RolUsuario.financiero));
                    break;
                case "PAG_ANTI_LIQUI_HABER":
                    getParamts().put("usuarioFinanciero", clienteService.getrolsUser(RolUsuario.financiero));
                    break;
                case "PAG_SERV_NOTARIALES":
                    getParamts().put("usuarioFinanciero", clienteService.getrolsUser(RolUsuario.financiero));
                    break;
                default:
                    getParamts().put("usuario", user.getNameUser());
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

    public void adjuntarDucumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(event.getFile());
            if (files != null) {
                uploadDoc.upload(tramite, files);
                files.clear();
            }
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("requisitoDialogForm");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
        }
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

}
